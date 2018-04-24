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
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import edu.example.ssf.mma.Tracker.Trackings;
import edu.example.ssf.mma.hardwareAdapter.ClapDetector;


public class StateMachine implements IStateMachine {

    private Consumer<String> onWriteToAppLog = null;
    private AbstractState actState = null;
    private ArrayList<Transition> transitions = new ArrayList<>();
    private Timer timeoutTimer = new Timer();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public StateMachine() {
        AbstractState idle = new StateIdle();
        AbstractState trackingActivity = new StateTrackingActivity();
        AbstractState trackingPrecondition = new StateTrackingPreCondition();
        AbstractState stoppingPrecondition = new StateStoppingPreCondition();

        transitions.add(new Transition(idle, Action.CLAP_DETECTED, trackingPrecondition, () -> {
            safeCancelTimeoutTimer();
            startTimeoutTimer();
        }));

        transitions.add(new Transition(trackingPrecondition, Action.CLAP_DETECTED, trackingActivity, () -> {
            onWriteToAppLog.accept("Double clap detected");
            safeCancelTimeoutTimer();
            Trackings.startNewTracking();
        }));

        transitions.add(new Transition(trackingActivity, Action.CLAP_DETECTED, stoppingPrecondition, () -> {
            safeCancelTimeoutTimer();
            startTimeoutTimer();
        }));
        transitions.add(new Transition(stoppingPrecondition, Action.CLAP_DETECTED, idle, () -> {
            onWriteToAppLog.accept("Double clap detected");
            safeCancelTimeoutTimer();
            Trackings.endTracking();
        }));

        transitions.add(new Transition(trackingPrecondition, Action.PRECONDITION_TIMEOUT, idle,
                () -> onWriteToAppLog.accept("Only single clap detected")));
        transitions.add(new Transition(stoppingPrecondition, Action.PRECONDITION_TIMEOUT, trackingActivity,
                () -> onWriteToAppLog.accept("Only single clap detected")));

        this.actState = idle;

        new ClapDetector((time, salience) -> doTransition(Action.CLAP_DETECTED));
    }

    private void startTimeoutTimer() {
        timeoutTimer = new Timer();
        timeoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                doTransition(Action.PRECONDITION_TIMEOUT);
            }
        }, 1000);
    }

    private void safeCancelTimeoutTimer() {
        try {
            timeoutTimer.cancel();
        } catch (Exception ignored) {

        }
    }


    @Override
    public void setLogger(Consumer<String> logger) {
        onWriteToAppLog = logger;
    }

    @Override
    public void doTransition(Action trigger) {

        for (Transition transition : transitions) {
            if (transition.trigger.equals(trigger) && transition.preState.equals(actState)) {

                this.actState = transition.newState;
                transition.doTransitionAction();
                return;
            }
        }
    }


}

