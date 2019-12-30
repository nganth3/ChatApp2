package com.example.chatapp;

import android.telecom.Call;
import android.telecom.VideoProfile;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import io.reactivex.subjects.BehaviorSubject;

public class OngoingCall {

    public static BehaviorSubject<Integer> state = BehaviorSubject.create();
    public static Call call;

    public Object callback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int newState) {
            super.onStateChanged(call, newState);
            MainActivity.connectSV.sendValue(String.valueOf(call.getState()));
            state.onNext(newState);


        }
    };



    public final void setCall(@Nullable Call value) {
        if (call != null) {
            call.unregisterCallback((Call.Callback)callback);
        }

        if (value != null) {
            value.registerCallback((Call.Callback)callback);
            state.onNext(value.getState());
        }

        call = value;
    }

    public void answer() {
        assert call != null;
        call.answer(VideoProfile.STATE_AUDIO_ONLY);

    }

    public void hangup() {
        assert call != null;
        call.disconnect();
    }

}
