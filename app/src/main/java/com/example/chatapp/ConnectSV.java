package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class ConnectSV extends AppCompatActivity{
    private static Socket mSocket;
    TextToSpeech textToSpeech;


    public static String rcBroastcast;

    private Emitter.Listener onConfirmDevice = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    try {
                        String Data = object.getString("noidung");
                        MainActivity.txtLoai.setText(Data);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    };
    private Emitter.Listener onRecevData = new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = (JSONObject) args[0];
                try {
                   // String Data = object.getString("noidung");
                    rcBroastcast =object.getString("noidung");
                    action_app(rcBroastcast);
                    Log.d("BBBBB",rcBroastcast);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    };


    public void action_app(String noidung){
        int soID =0 ;

        if(noidung.contains("Start_with ID")){
            soID = Integer.parseInt(noidung.substring(14,noidung.length()));
            noidung = "Start_with ID";
        }

        switch (noidung){
            case "Start_with ID":
            MainActivity.getInstance().startting(soID);
            case "Start":
                if(MainActivity.Loai == "Call"){
                    MainActivity.getInstance().makeCall();
                }
                break;

            case "4":
                if(MainActivity.Loai == "Speech") {
                    MainActivity.getInstance().startSpeech();                }
                break;
            case "7":
                if(MainActivity.Loai == "Speech") {
                   MainActivity.getInstance().stopSpeech();
                }
                break;
            case "8":
                if(MainActivity.Loai == "Call") {
                   OngoingCall.call.disconnect();
                }
                break;
            default:
        }
    }
    public void setmSocket(String Loai) {
        MainActivity.txtLoai.setText("Conectting....");
        try {
            mSocket = IO.socket("http://"+MainActivity.edtIP.getText().toString()+":3000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();
        Log.d("SSKKK", String.valueOf(mSocket));
        mSocket.emit("join",Loai);
        mSocket.on("Xacnhan",onConfirmDevice);
        mSocket.on("Broadcast",onRecevData);
    }
    public void sendValue(String value){
        if(mSocket!=null) {
            mSocket.emit("Send_value", value);
        }
    }


}
