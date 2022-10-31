package org.mario.ai;

import org.mario.ai.engine.core.MarioGame;
import org.mario.ai.engine.core.MarioResult;

import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.IntegerGene;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;

public final class JeneticsAlgorithmLauncher {
    static int MIN = 0;
    static int MAX = 12;
    static int LENGTH = 7;
    static int LEVEL = 2;
    // INDICES DEL CROMOSOMA PRIORIZADOS SEGÚN ALGORITMO ARTESANAL
    // i=0 es de AVANZA A SALTA
    // i=1 es de AVANZA A PLANTA
    // i=2 es de SALTA A VOLANDO
    // i=3 es de VOLANDO A PLANTA
    // i=4 es de VOLANDO A SALTA
    // i=5 es de VOLANDO A AVANZA
    // i=6 es de PLANTA A SALTA

    // VALORES DEL CROMOSOMA
    // 0 es PASO DE i a i+1 SI HAY ENEMIGO U OBSTACULO O AGUJERO
    // 1 es PASO DE i a i+1 SI HAY PLANTA FUERA
    // 2 es PASO DE i a i+1 SI NO HAY ENEMIGO NI OBSTACULO NI AGUJERO
    // 3 es PASO DE i a i+1 SI HAY ENEMGIO DETRÁS O SI HAY PLANTA DENTRO
    // 4 es PASO DE i a i+1 SI MARIO NO ESTÁ EN EL SUELO
    // 5 es PASO DE i a i+1 SI MARIO ESTÁ EN EL SUELO
    // 6 es PASO DE i a i+1 SI NO HAY PLANTA FUERA
    // 7 es PASO DE i a i+1 SIEMPRE
    // 8 es PASO DE i a i+1 SI HAY ENEMIGO ATRÁS O DELANTE
    // 9 es PASO DE i a i+1 SI HAY PLANTA FUERA Y HAY ENEMIGO DETRÁS
    // 10 es PASO DE i a i+1 SI HAY ENEMIGO Y OBSTACULO Y AGUJERO
    // 11 es PASO DE i a i+1 SI NO HAY ENEMIGO O NO HAY OBSTACULO O NO HAY AGUJERO

    private static float eval(final Genotype<IntegerGene> gt) {
        float fitness = 0f;
        MarioGame game = new MarioGame();
        MarioResult res = game.runGame(new org.mario.ai.agents.genetic.Agent(gt),
                PlayLevel.getLevel("./levels/original/lvl-" + LEVEL + ".txt"), 20, 0, false);
        if (res.getGameStatus().toString().equals("WIN")) {
            fitness = (float) res.getRemainingTime();
        } else {
            fitness = res.getCompletionPercentage() * 100;
        }
        return fitness;
    }

    public static void imprimirResultados(Genotype<IntegerGene> result) {
        System.out.println("\n**************************************************");
        System.err.println("Ejecucion finalizada para el nivel " + LEVEL);
        System.out.println("El mejor cromosoma es: " + result.chromosome());
        MarioResult juego = new MarioGame().runGame(new org.mario.ai.agents.genetic.Agent(result),
                PlayLevel.getLevel("./levels/original/lvl-" + LEVEL + ".txt"), 20, 0, true);
        System.out.println("El porcentaje de completado es del "
                + String.valueOf((int) Math.ceil(juego.getCompletionPercentage() * 100)) + "%");
        System.out.println("Tiempo restante: " + (int) Math.ceil(juego.getRemainingTime() / 1000f) + " segundos");
        System.out.println("**************************************************");
    }

    public static void main(final String[] args) {
        final Genotype<IntegerGene> gt = Genotype.of(IntegerChromosome.of(MIN, MAX, LENGTH));
        final Engine<IntegerGene, Float> engine = Engine.builder(JeneticsAlgorithmLauncher::eval, gt).populationSize(100)
                .build();
        final Genotype<IntegerGene> result = engine.stream().limit(100).collect(EvolutionResult.toBestGenotype());
        imprimirResultados(result);
    }

}