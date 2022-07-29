package org.mario.ai.agents.genetic;

import org.mario.ai.engine.core.MarioAgent;
import org.mario.ai.engine.core.MarioForwardModel;
import org.mario.ai.engine.core.MarioGame;
import org.mario.ai.engine.core.MarioTimer;
import org.mario.ai.engine.helper.MarioActions;

import io.jenetics.Genotype;
import io.jenetics.IntegerGene;

public class AgentSalta implements MarioAgent {
    // ESTADO A AVANZA
    // ESTADO S SALTA
    // CONDICION 0 NO HAY ENEMIGOS
    // CONDICION 1 HAY ENEMIGO
    private boolean[] action;
    private States state;

    private Genotype<IntegerGene> gt;

    public AgentSalta(Genotype<IntegerGene> gt) {
        this.gt = gt;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public enum States {
        AVANZO, SALTO
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
        switch (state) {
            case AVANZO:
            action[MarioActions.JUMP.getValue()] = false;
                if (gt.chromosome().gene().allele().equals(0)) {
                    // SALTO SI NO HAY ENEMIGO NI OBSTACULO
                    if (!enemyInFront(enemies) && !thereIsObstacle(scene)) {
                        state = States.SALTO;
                        break;
                    }
                } else if (gt.chromosome().gene().allele().equals(1)) {
                    // SALTO SI HAY ENEMIGO U OBSTACULO
                    if (enemyInFront(enemies) || thereIsObstacle(scene)) {
                        state = States.SALTO;
                        break;
                    }
                }

                break;

            case SALTO:
                action[MarioActions.JUMP.getValue()] = true;
                if (gt.chromosome().get(1).allele().equals(0)) {
                    // AVANZO SI NO HAY ENEMIGO NI OBSTACULO
                    if (!enemyInFront(enemies) && !thereIsObstacle(scene)) {
                        state = States.AVANZO;
                        break;
                    }
                } else if (gt.chromosome().get(1).allele().equals(1)) {
                    // AVANZO SI HAY ENEMIGO U OBSTACULO
                    if (enemyInFront(enemies) || thereIsObstacle(scene)) {
                        state = States.AVANZO;
                        break;
                    }
                }

                break;

        }

        return action;
    }

    @Override
    public String getAgentName() {
        return "CraftyAgent";
    }

}