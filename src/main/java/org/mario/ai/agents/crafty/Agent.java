package org.mario.ai.agents.crafty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Random;
import java.util.zip.Inflater;

import org.mario.ai.engine.core.MarioAgent;
import org.mario.ai.engine.core.MarioGame;
import org.mario.ai.engine.core.MarioForwardModel;
import org.mario.ai.engine.core.MarioTimer;
import org.mario.ai.engine.helper.MarioActions;
import org.springframework.beans.factory.annotation.Autowired;

// import org.springframework.statemachine.*;

public class Agent implements MarioAgent {
    private boolean facing_left;
    private int leftCounter;
    private int shootCounter;
    private boolean[] action;
    private States state;

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public enum States {
        AVANZO, SALTO, VOLANDO
    }

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        action = new boolean[MarioActions.numberOfActions()];
        state = States.AVANZO;
        leftCounter = 0;
        shootCounter = 0;
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
        action[MarioActions.SPEED.getValue()] = true;
        int[][] mundo = MarioGame.transposeMatrix(model.getScreenSceneObservation());
        int[][] scene = model.getMarioSceneObservation();
        int[][] enemies = model.getMarioEnemiesObservation();
        switch (state) {
            case AVANZO:
                if (!model.isMarioOnGround()) {
                    state = States.VOLANDO;
                    break;
                }
                if (enemyInFront(enemies) || thereIsObstacle(scene) || thereIsHole(mundo)) {
                    state = States.SALTO;
                    break;
                }
                action[MarioActions.RIGHT.getValue()] = true;
                action[MarioActions.JUMP.getValue()] = false;

                break;

            case SALTO:
                action[MarioActions.JUMP.getValue()] = true;
                if (!enemyInFront(enemies) && !thereIsObstacle(scene) && !thereIsHole(mundo)) {
                    state = States.VOLANDO;
                }

                if (thereIsObstacle(scene)) {
                    state = States.VOLANDO;
                }

                break;
            case VOLANDO:
                if (!model.isMarioOnGround()) {
                    break;
                }
                action[MarioActions.JUMP.getValue()] = false;
                action[MarioActions.RIGHT.getValue()] = true;
                if (enemyInFront(enemies) || thereIsObstacle(scene) || thereIsHole(mundo)) {
                    state = States.SALTO;
                } else {
                    state = States.AVANZO;
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