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