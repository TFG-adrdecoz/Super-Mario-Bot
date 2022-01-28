package org.mario.ai.agents.doNothing;

import org.mario.ai.engine.core.MarioAgent;
import org.mario.ai.engine.core.MarioForwardModel;
import org.mario.ai.engine.core.MarioTimer;
import org.mario.ai.engine.helper.MarioActions;

public class Agent implements MarioAgent {
    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {

    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        return new boolean[MarioActions.numberOfActions()];
    }

    @Override
    public String getAgentName() {
        return "DoNothingAgent";
    }
}
