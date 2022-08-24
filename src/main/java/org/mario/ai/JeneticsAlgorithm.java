package org.mario.ai;

import org.mario.ai.engine.core.MarioGame;
import org.mario.ai.engine.core.MarioResult;

import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.IntegerGene;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;

public final class JeneticsAlgorithm {
    static int MIN = 0;
    static int MAX = 4;
    static int LENGTH = 4;
    static int LEVEL = 2;
    // INDICES DEL CROMOSOMA PRIORIZADOS SEGÚN ALGORITMO ARTESANAL
    // i=0 es de AVANZA A SALTA
    // i=1 es de SALTA A VOLANDO
    // i=2 es de VOLANDO A SALTA
    // i=3 es de VOLANDO A AVANZA

    // VALORES DEL CROMOSOMA
    // 0 es PASO DE i a i+1 SI HAY ENEMIGO, OBSTACULO O AGUJERO Y NO HAY PLANTA
    // FUERA
    // 1 es PASO DE i a i+1 SI NI HAY ENEMIGO NI AGUJERO O HAY OBSTACULO
    // 2 es PASO DE i a i+1 SI HAY PLANTA FUERA Y HAY ENEMIGO DETRÁS. O HAY ENEMIGO, OBSTACULO O AGUJERO Y MARIO ESTÁ EN EL SUELO
    // 3 es PASO DE i a i+1 SI MARIO ESTÁ EN EL SUELO

    private static float eval(final Genotype<IntegerGene> gt) {
        MarioGame game = new MarioGame();
        MarioResult resultado = game.runGame(new org.mario.ai.agents.genetic.Agent(gt),
                PlayLevel.getLevel("./levels/original/lvl-" + LEVEL + ".txt"), 20, 0, false);

        return resultado.getCompleted() * 10000 + resultado.getCompletionPercentage() * 10000;
    }

    public static void pintarResultados(Genotype<IntegerGene> result) {
        System.out.println("\nEl mejor cromosoma es: " + result.chromosome());
        MarioResult juego = new MarioGame().runGame(new org.mario.ai.agents.genetic.Agent(result),
                PlayLevel.getLevel("./levels/original/lvl-" + LEVEL + ".txt"), 20, 0, false);
        System.out.println("El porcentaje de completado es del "
                + String.valueOf((int) Math.ceil(juego.getCompletionPercentage() * 100)) + "%");
        System.out.println("Tiempo restante: " + (int) Math.ceil(juego.getRemainingTime() / 1000f) + " segundos");

    }

    public static void main(final String[] args) {
        final Genotype<IntegerGene> gt = Genotype.of(IntegerChromosome.of(MIN, MAX, LENGTH));
        final Engine<IntegerGene, Float> engine = Engine.builder(JeneticsAlgorithm::eval, gt).populationSize(15)
                .build();
        final Genotype<IntegerGene> result = engine.stream().limit(20).collect(EvolutionResult.toBestGenotype());
        pintarResultados(result);
    }

}