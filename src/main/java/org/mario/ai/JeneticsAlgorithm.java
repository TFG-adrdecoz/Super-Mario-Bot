package org.mario.ai;

import org.mario.ai.engine.core.MarioGame;
import org.mario.ai.engine.core.MarioResult;

import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.IntegerGene;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;

public final class JeneticsAlgorithm {
    private static float eval(final Genotype<IntegerGene> gt) {
        MarioGame game = new MarioGame();
        float res = game.runGame(new org.mario.ai.agents.genetic.Agent(gt),
        PlayLevel.getLevel("./levels/original/lvl-1.txt"), 20, 0, false).getCompletionPercentage();
        // System.out.println(gt.chromosome() + " RESULTADO: " + res*100);
        return res;
    }
    public static void pintarResultados(Genotype<IntegerGene> result ){
        System.out.println("\nEl mejor cromosoma es: " + result.chromosome());
        MarioResult juego = new MarioGame().runGame(new org.mario.ai.agents.genetic.Agent(result),
        PlayLevel.getLevel("./levels/original/lvl-2.txt"), 20, 0, true);
        System.out.println("El porcentaje de completado es del " + String.valueOf((int) Math.ceil(juego.getCompletionPercentage() * 100)) + "%");
        System.out.println("Tiempo restante: " + (int) Math.ceil(juego.getRemainingTime() / 1000f) + " segundos");

    }

    public static void main(final String[] args) {
        final Genotype<IntegerGene> gt = Genotype.of(IntegerChromosome.of(0, 3, 5),1);
           final Engine<IntegerGene, Float> engine = Engine.builder(JeneticsAlgorithm::eval, gt).populationSize(10).build();
        final Genotype<IntegerGene> result = engine.stream().limit(30).collect(EvolutionResult.toBestGenotype());
        pintarResultados(result);
    }

    

}