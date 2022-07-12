package org.mario.ai.agents.dummy;

import java.util.ArrayList;
import java.util.Random;

import org.mario.ai.engine.core.MarioAgent;
import org.mario.ai.engine.core.MarioForwardModel;
import org.mario.ai.engine.core.MarioTimer;

public class Agent implements MarioAgent {
    private Random rnd;
    private ArrayList<boolean[]> choices;

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        rnd = new Random();
        choices = new ArrayList<>();
        //right
        choices.add(new boolean[]{false, false, false, false, false});
        choices.add(new boolean[]{false, false, false, false, true});
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        return choices.get(rnd.nextInt(choices.size()));
    }

    @Override
    public String getAgentName() {
        return "DummyAgent";
    }

}