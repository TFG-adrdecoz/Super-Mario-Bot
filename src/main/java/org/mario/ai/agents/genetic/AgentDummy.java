package org.mario.ai.agents.genetic;

import org.mario.ai.engine.core.MarioAgent;
import org.mario.ai.engine.core.MarioForwardModel;
import org.mario.ai.engine.core.MarioTimer;
import org.mario.ai.engine.helper.MarioActions;

import io.jenetics.Genotype;
import io.jenetics.IntegerGene;

public class AgentDummy implements MarioAgent {
    private boolean[] action;
    private States state;
    private Genotype<IntegerGene> gt;

    public AgentDummy(Genotype<IntegerGene> gt) {
        this.gt=gt;            
    }

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
        if(gt.chromosome().gene().allele().equals(0))
        state = States.AVANZO;
    }



    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        action[MarioActions.SPEED.getValue()] = true;
        action[MarioActions.RIGHT.getValue()] = true;
        

        return action;
    }

    @Override
    public String getAgentName() {
        return "CraftyAgent";
    }

}