package org.mario.ai.agents.human;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.mario.ai.engine.core.MarioAgent;
import org.mario.ai.engine.core.MarioForwardModel;
import org.mario.ai.engine.core.MarioTimer;
import org.mario.ai.engine.helper.MarioActions;

public class Agent extends KeyAdapter implements MarioAgent {
    private boolean[] actiones = null;

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        actiones = new boolean[MarioActions.numberOfActions()];
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        return actiones;
    }

    @Override
    public String getAgentName() {
        return "HumanAgent";
    }

    @Override
    public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(), false);
    }

    private void toggleKey(int keyCode, boolean isPressed) {
        if (this.actiones == null) {
            return;
        }
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                this.actiones[MarioActions.LEFT.getValue()] = isPressed;
                break;
            case KeyEvent.VK_RIGHT:
                this.actiones[MarioActions.RIGHT.getValue()] = isPressed;
                break;
            case KeyEvent.VK_DOWN:
                this.actiones[MarioActions.DOWN.getValue()] = isPressed;
                break;
            case KeyEvent.VK_S:
                this.actiones[MarioActions.JUMP.getValue()] = isPressed;
                break;
            case KeyEvent.VK_A:
                this.actiones[MarioActions.SPEED.getValue()] = isPressed;
                break;
        }
    }

}
