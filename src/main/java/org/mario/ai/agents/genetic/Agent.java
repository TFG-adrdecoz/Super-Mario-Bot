package org.mario.ai.agents.genetic;

import org.mario.ai.engine.core.MarioAgent;
import org.mario.ai.engine.core.MarioForwardModel;
import org.mario.ai.engine.core.MarioGame;
import org.mario.ai.engine.core.MarioTimer;
import org.mario.ai.engine.helper.MarioActions;

import io.jenetics.Genotype;
import io.jenetics.IntegerGene;

public class Agent implements MarioAgent {
    private boolean[] action;
    private States state;

    private Genotype<IntegerGene> gt;

    public Agent(Genotype<IntegerGene> gt) {
        this.gt = gt;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public enum States {
        AVANZO, SALTO, VOLANDO, PLANTA
    }

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        action = new boolean[MarioActions.numberOfActions()];
        state = States.AVANZO;
        action[MarioActions.RIGHT.getValue()] = true;
        action[MarioActions.SPEED.getValue()] = true;
    }

    private int getLocation(int relX, int relY, int[][] scene) {
        int realX = 8 + relX;
        int realY = 8 + relY;
        return scene[realX][realY];
    }

    private boolean enemyInFront(int[][] enemies) {
        for (int i = 0; i > -2; i--) {
            for (int j = 1; j < 4; j++) {
                if (getLocation(j, i, enemies) > 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean thereIsHole(int[][] mundo) {
        int[] inFrontOf = new int[] { mundo[14][9], mundo[14][10] };
        for (int i = 0; i < inFrontOf.length; i++) {
            if (inFrontOf[i] == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean thereIsPlantFuera(int[][] completo) {
        int[] inFrontOf3 = new int[] { getLocation(1, -4, completo), getLocation(2, -4, completo),
                getLocation(3, -4, completo), getLocation(4, -4, completo), getLocation(5, -4, completo),
                getLocation(6, -4, completo) };
        int[] inFrontOf1 = new int[] { getLocation(1, -3, completo), getLocation(2, -3, completo),
                getLocation(3, -3, completo), getLocation(4, -3, completo), getLocation(5, -3, completo),
                getLocation(6, -3, completo) };
        int[] inFrontOf2 = new int[] { getLocation(1, -2, completo), getLocation(2, -2, completo),
                getLocation(3, -2, completo), getLocation(4, -2, completo), getLocation(5, -2, completo),
                getLocation(6, -2, completo) };
        int[] inFrontOf5 = new int[] { getLocation(1, -1, completo), getLocation(2, -1, completo),
                getLocation(3, -1, completo), getLocation(4, -1, completo), getLocation(5, -1, completo),
                getLocation(6, -1, completo) };
        int[] inFrontOf4 = new int[] { getLocation(1, 0, completo), getLocation(2, 0, completo),
                getLocation(3, 0, completo), getLocation(4, 0, completo), getLocation(5, 0, completo),
                getLocation(6, 0, completo) };
        int[] inFrontOf7 = new int[] { getLocation(1, 2, completo), getLocation(2, 2, completo),
                getLocation(3, 2, completo), getLocation(4, 2, completo), getLocation(5, 2, completo),
                getLocation(6, 2, completo) };
        int[] inFrontOf6 = new int[] { getLocation(1, 1, completo), getLocation(2, 1, completo),
                getLocation(3, 1, completo), getLocation(4, 1, completo), getLocation(5, 1, completo),
                getLocation(6, 1, completo) };
        for (int i = 0; i < inFrontOf3.length - 1; i++) {
            if ((inFrontOf1[i] == 0 && inFrontOf1[i + 1] == 8) || (inFrontOf2[i] == 0 && inFrontOf2[i + 1] == 8)
                    || (inFrontOf3[i] == 0 && inFrontOf3[i + 1] == 8) || (inFrontOf3[i] == 0 && inFrontOf3[i + 1] == 8)
                    || (inFrontOf4[i] == 0 && inFrontOf4[i + 1] == 8) || (inFrontOf5[i] == 0 && inFrontOf5[i + 1] == 8)
                    || (inFrontOf6[i] == 0 && inFrontOf6[i + 1] == 8)
                    || (inFrontOf7[i] == 0 && inFrontOf7[i + 1] == 8)) {
                action[MarioActions.RIGHT.getValue()] = false;
                return true;
            }
        }
        return false;
    }

    private boolean thereIsPlantDentro(int[][] completo) {
        int[] inFrontOf3 = new int[] { getLocation(1, -1, completo), getLocation(2, -1, completo),
                getLocation(3, -1, completo), getLocation(4, -1, completo) };
        int[] inFrontOf2 = new int[] { getLocation(1, -2, completo), getLocation(2, -2, completo),
                getLocation(3, -2, completo), getLocation(4, -2, completo) };
        int[] inFrontOf1 = new int[] { getLocation(1, -3, completo), getLocation(2, -3, completo),
                getLocation(3, -3, completo), getLocation(4, -3, completo) };
        int[] inFrontOf4 = new int[] { getLocation(1, -4, completo), getLocation(2, -4, completo),
                getLocation(3, -4, completo), getLocation(4, -4, completo) };
        int[] inFrontOf5 = new int[] { getLocation(1, -5, completo), getLocation(2, -5, completo),
                getLocation(3, -5, completo), getLocation(4, -5, completo) };
        for (int i = 0; i < inFrontOf3.length - 1; i++) {
            if ((inFrontOf1[i] == 34 && inFrontOf1[i + 1] == 8) || (inFrontOf2[i] == 34 && inFrontOf2[i + 1] == 8)
                    || (inFrontOf3[i] == 34 && inFrontOf3[i + 1] == 8)
                    || (inFrontOf4[i] == 34 && inFrontOf4[i + 1] == 8)
                    || (inFrontOf5[i] == 34 && inFrontOf5[i + 1] == 8)) {
                return true;
            }
        }

        return false;
    }

    private boolean enemyBehind(int[][] enemies) {
        for (int j = -1; j < 1; j++) {
            if (getLocation(j, 0, enemies) > 1) {
                return true;
            }
        }

        return false;
    }

    private boolean thereIsObstacle(int[][] scene) {
        int[] inFrontOf = new int[] { getLocation(1, 0, scene), getLocation(2, 0, scene), getLocation(3, 0, scene),
                getLocation(4, 0, scene), getLocation(5, 0, scene) };
        for (int i = 0; i < inFrontOf.length; i++) {
            if (inFrontOf[i] == 17 || inFrontOf[i] == 23 || inFrontOf[i] == 24 || inFrontOf[i] == 34) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        int[][] mundo = MarioGame.transposeMatrix(model.getScreenSceneObservation());
        int[][] scene = model.getMarioSceneObservation();
        int[][] enemies = model.getMarioEnemiesObservation();
        int[][] completo = model.getMarioCompleteObservation();
        switch (state) {
            case AVANZO:
                for (int i = 0; i < 2; i++) {
                    evalGene(i, mundo, scene, enemies, completo, model);
                    if (!state.equals(state.AVANZO)) {
                        break;
                    }
                }
                action[MarioActions.JUMP.getValue()] = false;
                break;
            case SALTO:
                action[MarioActions.RIGHT.getValue()] = true;
                action[MarioActions.JUMP.getValue()] = true;
                evalGene(2, mundo, scene, enemies, completo, model);
                break;
            case VOLANDO:
                action[MarioActions.RIGHT.getValue()] = true;
                evalGene(3, mundo, scene, enemies, completo, model);

                if (!model.isMarioOnGround()) {
                    break;
                }

                action[MarioActions.JUMP.getValue()] = false;
                for (int i = 4; i < 6; i++) {
                    evalGene(i, mundo, scene, enemies, completo, model);
                    if (state.equals(state.SALTO)) {
                        break;
                    }
                }
                break;
            case PLANTA:
                action[MarioActions.RIGHT.getValue()] = false;
                evalGene(6, mundo, scene, enemies, completo, model);
                break;

        }
        return action;
    }

    public void evalGene(int gene, int[][] mundo, int[][] scene, int[][] enemies, int[][] completo,
            MarioForwardModel model) {
        if (gt.chromosome().get(gene).allele().equals(0)) {
            if ((enemyInFront(enemies) || thereIsObstacle(scene) || thereIsHole(mundo))) {
                getStateByGene(gene);
            }
        } else if (gt.chromosome().get(gene).allele().equals(1)) {
            if (thereIsPlantFuera(completo)) {
                getStateByGene(gene);
            }
        } else if (gt.chromosome().get(gene).allele().equals(2)) {
            if (!enemyInFront(enemies) && !thereIsObstacle(scene) && !thereIsHole(mundo)) {
                getStateByGene(gene);
            }
        } else if (gt.chromosome().get(gene).allele().equals(3)) {
            if (enemyBehind(enemies) || thereIsPlantDentro(completo)) {
                getStateByGene(gene);
            }
        } else if (gt.chromosome().get(gene).allele().equals(4)) {
            if (!model.isMarioOnGround()) {
                getStateByGene(gene);
            }
        } else if (gt.chromosome().get(gene).allele().equals(5)) {
            if (model.isMarioOnGround()) {
                getStateByGene(gene);
            }
        } else if (gt.chromosome().get(gene).allele().equals(6)) {
            if (!thereIsPlantFuera(completo)) {
                getStateByGene(gene);
            }
        } else if (gt.chromosome().get(gene).allele().equals(7)) {
            if (true) {
                getStateByGene(gene);
            }
        } else if (gt.chromosome().get(gene).allele().equals(8)) {
            if (enemyBehind(enemies) || enemyInFront(enemies)) {
                getStateByGene(gene);
            }
        } else if (gt.chromosome().get(gene).allele().equals(9)) {
            if (thereIsPlantFuera(completo) && enemyBehind(enemies)) {
                getStateByGene(gene);
            }
        } else if (gt.chromosome().get(gene).allele().equals(10)) {
            if (((enemyInFront(enemies) && thereIsObstacle(scene) && thereIsHole(mundo)))) {
                getStateByGene(gene);
            }
        } else if (gt.chromosome().get(gene).allele().equals(11)) {
            if (!enemyInFront(enemies) || !thereIsObstacle(scene) || !thereIsHole(mundo)) {
                getStateByGene(gene);
            }
        }

    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void getStateByGene(int gene) {
        if (gene == 0 || gene == 4 || gene == 6)
            state = States.SALTO;
        else if (gene == 2)
            state = States.VOLANDO;
        else if (gene == 1 || gene == 3)
            state = States.PLANTA;
        else if (gene == 5)
            state = States.AVANZO;
    }

    @Override
    public String getAgentName() {
        return "GeneticAgent";
    }

}