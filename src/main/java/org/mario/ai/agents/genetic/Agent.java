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
        AVANZO, SALTO, TRANSICION
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
                    // System.out.println("ENEMIGO CERCA");
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
                // System.out.println("HAY HOLE CERCA");
                return true;
            }
        }

        return false;
    }

    private boolean isFlying(int[][] scene) {
        int[] inFrontOf = new int[] { getLocation(0, 1, scene), getLocation(0, 2, scene)};

        for (int i = 0; i < inFrontOf.length; i++) {
            if (inFrontOf[i] == 0 ) {
                // System.out.println("VOLANDO");
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
                // System.out.println("OBSTACULO CERCA");
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
        int gene = 0;
        // System.out.println(state);
        switch (state) {
            case AVANZO:
                gene = getRandomNumber(0, 1);
                evalGene(gene, mundo, scene, enemies, model);
                action[MarioActions.JUMP.getValue()] = false;

                break;

            case SALTO:
                action[MarioActions.JUMP.getValue()] = true;
                evalGene(2, mundo, scene, enemies, model);

                break;
            case TRANSICION:
                gene = getRandomNumber(3, 4);
                if (isFlying(scene)) {
                    break;
                }
                action[MarioActions.JUMP.getValue()] = false;

                evalGene(gene, mundo, scene, enemies, model);

                break;

        }

        return action;
    }

    public void evalGene(int gene, int[][] mundo, int[][] scene, int[][] enemies, MarioForwardModel model) {
        if (gt.chromosome().get(gene).allele().equals(0)) {
            // CAMBIO SI NO HAY ENEMIGO NI OBSTACULO
            if (!enemyInFront(enemies) && !thereIsObstacle(scene) && !thereIsHole(mundo)) {
                getStateByGene(gene);
            }
        } else if (gt.chromosome().get(gene).allele().equals(1)) {
            // CAMBIO SI HAY ENEMIGO U OBSTACULO
            if (enemyInFront(enemies) || thereIsObstacle(scene) || thereIsHole(mundo)) {
                getStateByGene(gene);
            }
        } else if (gt.chromosome().get(gene).allele().equals(2)) {
            // CAMBIO SI ESTÁ EN TIERRA
            if (!model.isMarioOnGround()) {
                getStateByGene(gene);
            }
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void getStateByGene(int gene) {
        if (gene == 3)
            state = States.AVANZO;
        else if (gene == 0 || gene == 4)
            state = States.SALTO;
        else if (gene == 1 || gene == 2)
            state = States.TRANSICION;

    }

    @Override
    public String getAgentName() {
        return "CraftyAgent";
    }

}