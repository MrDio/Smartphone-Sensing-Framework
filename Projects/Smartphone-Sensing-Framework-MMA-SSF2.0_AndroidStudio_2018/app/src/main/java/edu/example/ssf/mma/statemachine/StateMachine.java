/*
 *  This file is part of the Multimodal Mobility Analyser(MMA), based
 *  on the Smartphone Sensing Framework (SSF)

    MMA (also SSF) is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    MMA (also SSF) is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU v3 General Public License for more details.

    Released under GNU v3
    
    You should have received a copy of the GNU General Public License
    along with MMA.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.example.ssf.mma.statemachine;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import edu.example.ssf.mma.Tracker.Trackings;
import edu.example.ssf.mma.hardwareAdapter.ClapDetector;


public class StateMachine implements IStateMachine {

    public Consumer<String> onWriteToAppLog = null;
    /**
     * state of the state machine when not in the state "WALKING" or "DRIVING". Also the state the state machine is in at the start.
     */
    /**
     * setting the abstract state in the beginning to null.
     */
    private AbstractState actState = null;
    private Date lastClapDetected = null;
    private ArrayList<Transition> transitions = new ArrayList<>();

    private Timer timer = new Timer();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public StateMachine() {
        AbstractState idle = new StateIdle();
        AbstractState trackingActivity = new StateTrackingActivity();
        AbstractState trackingPrecondition = new StateTrackingPreCondition();
        AbstractState stoppingPrecondition = new StateStoppingPreCondition();

        transitions.add(new Transition(idle, Action.CLAP_DETECTED, trackingPrecondition, () -> {
            startTimeoutTimer();
        }));

        transitions.add(new Transition(trackingPrecondition, Action.CLAP_DETECTED, trackingActivity, () -> {
            safeCancelTimeoutTimer();
            Trackings.startNewTracking();
        }));

        transitions.add(new Transition(trackingActivity, Action.CLAP_DETECTED, stoppingPrecondition, () -> {
            startTimeoutTimer();
        }));
        transitions.add(new Transition(stoppingPrecondition, Action.CLAP_DETECTED, idle, () -> {
            safeCancelTimeoutTimer();
            Trackings.endTracking();
        }));

        transitions.add(new Transition(trackingPrecondition, Action.PRECONDITION_TIMEOUT, idle));
        transitions.add(new Transition(stoppingPrecondition, Action.PRECONDITION_TIMEOUT, trackingActivity));


        this.actState = idle;

        new ClapDetector((time, salience) -> {
            onWriteToAppLog.accept("Clap detected");
            transitionCheck(Action.CLAP_DETECTED);
        });
    }

    private void startTimeoutTimer() {
        safeCancelTimeoutTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                transitionCheck(Action.PRECONDITION_TIMEOUT);
            }
        }, 1000);
    }
    private void safeCancelTimeoutTimer(){
        try{
            timer.cancel();
        }
        catch(Exception ex){

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Boolean setAndCheckForDoubleClap() {
        int maxTimeBetweenClapsInMs = 1000;
        // init first clap
        if (lastClapDetected == null) {
            onWriteToAppLog.accept("Single Clap detected");
            lastClapDetected = new Date();
            return false;
        }
        // last clap less then n ms in the past
        if ((new Date()).getTime() - lastClapDetected.getTime() < maxTimeBetweenClapsInMs) {
            onWriteToAppLog.accept("Double Clap detected");
            lastClapDetected = null;
            return true;
        }
        // new first clap
        onWriteToAppLog.accept("Single Clap detected");
        lastClapDetected = new Date();
        return false;
    }

    @Override
    public void transitionCheck(Action trigger) {

        for (Transition transition : transitions) {
            if (transition.trigger.equals(trigger) && transition.preState.equals(actState)) {

                this.actState = transition.newState;
                transition.doTransitionAction();
                return;
            }
        }
    }


}

