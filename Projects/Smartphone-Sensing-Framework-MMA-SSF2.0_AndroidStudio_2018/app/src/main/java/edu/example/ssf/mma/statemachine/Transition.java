package edu.example.ssf.mma.statemachine;

/**
 * Created by dennismuller on 23.04.18.
 */

public class Transition {
    public AbstractState preState;
    public Action trigger;
    public AbstractState newState;
    private Runnable transissionAction;

    public Transition(AbstractState preState, Action trigger, AbstractState newState, Runnable transissionAction) {

        this.preState = preState;
        this.trigger = trigger;
        this.newState = newState;
        this.transissionAction = transissionAction;
    }

    public Transition(AbstractState preState, Action trigger, AbstractState newState) {

        this.preState = preState;
        this.trigger = trigger;
        this.newState = newState;
        this.transissionAction = null;
    }

    public void doTransitionAction(){
        if(transissionAction != null){
            transissionAction.run();
        }
    }
}
