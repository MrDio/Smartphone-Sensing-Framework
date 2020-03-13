package edu.example.ssf.mma.statemachine;


public class Transition {
    AbstractState preState;
    Action trigger;
    AbstractState newState;
    private Runnable transitionAction;

    Transition(AbstractState preState, Action trigger, AbstractState newState, Runnable transissionAction) {

        this.preState = preState;
        this.trigger = trigger;
        this.newState = newState;
        this.transitionAction = transissionAction;
    }

    public Transition(AbstractState preState, Action trigger, AbstractState newState) {

        this.preState = preState;
        this.trigger = trigger;
        this.newState = newState;
        this.transitionAction = null;
    }

    void doTransitionAction(){
        if(transitionAction != null){
            transitionAction.run();
        }
    }
}
