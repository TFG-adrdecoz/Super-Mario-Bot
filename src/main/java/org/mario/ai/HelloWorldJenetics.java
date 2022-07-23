package org.mario.ai;

import java.util.Arrays;

import javax.swing.tree.TreeNode;

import org.mario.ai.engine.core.MarioGame;
import org.mario.ai.engine.core.MarioResult;
import org.mario.ai.PlayLevel;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.DoubleChromosome;
import io.jenetics.DoubleGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;

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

public final class HelloWorldJenetics {
    private static int eval(final Genotype<BitGene> gtf) {
        System.out.println(gtf);
        System.out.println(gtf.chromosome().as(BitChromosome.class).bitCount());
        return gtf.chromosome().as(BitChromosome.class).bitCount();
    }

    public static void main(final String[] args) {
        MarioGame game = new MarioGame();
        MarioResult juego = game.runGame(new org.mario.ai.agents.crafty.Agent(),
                PlayLevel.getLevel("./levels/original/lvl-1.txt"), 20, 0, false);
        // printResults(game.playGame(getLevel("./levels/original/lvl-1.txt"), 200, 0));
        PlayLevel.printResults(juego);


        final Genotype<BitGene> gt = Genotype.of(BitChromosome.of(3), 3);
        final int[][] array = gt.stream().map(c -> c.stream().mapToInt(gene -> gene.bit() ? 1 : 0).toArray())
                .toArray(int[][]::new);

        System.out.println(
                "Before:\n" + Arrays.deepToString(array));

        final Engine<BitGene, Integer> engine = Engine.builder(HelloWorldJenetics::eval, gt).build();
        final Genotype<BitGene> result = engine.stream().limit(100).collect(EvolutionResult.toBestGenotype());
        System.out.println("After:\n" + result);

    }

}