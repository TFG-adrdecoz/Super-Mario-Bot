package org.mario.ai.agents.crafty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Random;
import org.mario.ai.engine.core.MarioAgent;
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

    public enum States {
        AVANZO, SALTO
    }

    public enum Events {
        ENEMIGOS, LIBRE
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
        // System.out.println(scene[realX][realY]);
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

    private boolean thereIsHole(int[][] scene) {
        int[] inFrontOf = new int[] { getLocation(1, 0, scene), getLocation(2, 0, scene), getLocation(2, -1, scene) };

        for (int i = 0; i < inFrontOf.length; i++) {
            if (inFrontOf[i] == 34 || inFrontOf[i] == 17) {
                System.out.println("HAY HOLE CERCA");
                return true;
            }
        }

        return false;
    }

    private boolean thereIsObstacle(int[][] scene) {
        int[] inFrontOf = new int[] { getLocation(1, 0, scene), getLocation(2, 0, scene), getLocation(2, -1, scene) };

        for (int i = 0; i < inFrontOf.length; i++) {
            if (inFrontOf[i] == 17 || inFrontOf[i] == 23 || inFrontOf[i] == 24) {
                System.out.println("OBSTACULO CERCA");
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        // System.out.println(model.getMarioEnemiesObservation());
        action[MarioActions.JUMP.getValue()] = true;
        int[][] scene = model.getMarioSceneObservation();
        // System.out.println(Arrays.deepToString(scene).replace("], ", "]\n"));
        int[][] enemies = model.getMarioEnemiesObservation();
        System.out.println(state);


        switch (state) {
            case AVANZO:
                if (enemyInFront(enemies) || thereIsObstacle(scene) || thereIsHole(scene)) {
                    state = States.SALTO;
                    break;
                }
                action[MarioActions.RIGHT.getValue()] = true;
                action[MarioActions.JUMP.getValue()] = false;
                action[MarioActions.SPEED.getValue()] = true;
                break;

            case SALTO:
                action[MarioActions.JUMP.getValue()] = true;
                action[MarioActions.SPEED.getValue()] = false;
                if (!enemyInFront(enemies) && !thereIsObstacle(scene) && !thereIsHole(scene)) {
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