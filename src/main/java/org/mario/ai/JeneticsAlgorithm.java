package org.mario.ai;

import org.mario.ai.engine.core.MarioGame;
import org.mario.ai.engine.core.MarioResult;

import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.IntegerGene;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;

/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */

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
        PlayLevel.getLevel("./levels/original/lvl-1.txt"), 20, 0, false);
        System.out.println("El porcentaje de completado es del " + String.valueOf((int) Math.ceil(juego.getCompletionPercentage() * 100)) + "%");
        System.out.println("Tiempo restante: " + (int) Math.ceil(juego.getRemainingTime() / 1000f) + " segundos");

    }

    public static void main(final String[] args) {
        final Genotype<IntegerGene> gt = Genotype.of(IntegerChromosome.of(0, 3, 5),1);
           final Engine<IntegerGene, Float> engine = Engine.builder(JeneticsAlgorithm::eval, gt).populationSize(10).build();
        final Genotype<IntegerGene> result = engine.stream().limit(30).collect(EvolutionResult.toBestGenotype());
        pintarResultados(result);
        // new MarioGame().runGame(new org.mario.ai.agents.genetic.Agent(result),
        // PlayLevel.getLevel("./levels/original/lvl-1.txt"), 20, 0, true);
    }

    

}