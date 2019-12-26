package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import kotlin.collections.ArraysKt;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER;
import static android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME;

public class MainActivity extends AppCompatActivity {
    public static int REQUEST_PERMISSION = 0;
    public static EditText edtSothuebao,edtNoidung, edtIP;
    Button btnConect,btnStart;
    RadioButton rdoSpeech,rdoCall;
    public static ConnectSV connectSV;
    public static  int solanSpeech = 0;
    static String  Loai;
    public static TextView txtLoai,txtNoidung;
    private static MainActivity instance;
    public TextToSpeech textToSpeech;
    GetData getData;
    public static ArrayList<DataCall> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        anhXa();
        arrayList= new ArrayList<>();
        getData = new GetData(MainActivity.this,"http://"+edtIP.getText().toString()+"/androidwebservice/getdata.php");
        getData.ReadJSON();

        txtNoidung.setText(""+arrayList.size());

        btnConect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectSV = new ConnectSV();
                txtNoidung.setText(""+arrayList.size());
                txtNoidung.setText(""+arrayList.get(2).getNoidung());

                if (rdoSpeech.isChecked()) {
                    Loai = "Speech";
                }
                else{
                    Loai = "Call";
                }
                connectSV.setmSocket(Loai);
            }
        });
        instance = this;
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectSV.sendValue("Start");
            }
        });

    }
    private void anhXa(){
        rdoCall = findViewById(R.id.radioButton_Calls);
        rdoSpeech = findViewById(R.id.radioButton_Speech);
        btnConect = findViewById(R.id.button_Connect);
        txtLoai = findViewById(R.id.textView_tiltle);
        btnStart = findViewById(R.id.button_Start);
        edtIP = findViewById(R.id.editText_IP);
        txtNoidung = findViewById(R.id.textView_noidung);
    }

    @Override
    protected void onStart() {
        super.onStart();
        offerReplacingDefaultDialer();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        offerReplacingDefaultDialer();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION && ArraysKt.contains(grantResults, PERMISSION_GRANTED)) {
            makeCall();
        }
    }
    private void offerReplacingDefaultDialer() {

        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);

        if (!getPackageName().equals(telecomManager.getDefaultDialerPackage())) {
            Intent intent = new Intent(ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
            startActivity(intent);
        }
    }
    public void makeCall() {
        Uri uri = Uri.parse("tel:"+edtSothuebao.getText());
        Log.d("DDD","XXXXX");
        startActivity(new Intent(Intent.ACTION_CALL, uri));
/*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

       }
*/

    }

    public static MainActivity getInstance(){
        return instance;
    }
    public void stopSpeech(){
        if(textToSpeech!=null){
            textToSpeech.shutdown();
        }
    }

    public void startSpeech(){
        textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
                     @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            textToSpeech.setEngineByPackageName("com.google.android.tts");
                        }

                        @Override
                        public void onStop(String utteranceId, boolean interrupted) {
                            super.onStop(utteranceId, interrupted);
                            Log.d("LTTT", "OK"+ ConnectSV.rcBroastcast);
                            connectSV.sendValue("8");
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            Log.d("LTTT", "OK"+ ConnectSV.rcBroastcast);
                            connectSV.sendValue("8");

                        }
                        @Override
                        public void onError(String utteranceId) {

                        }

                        @Override
                        public void onRangeStart(String utteranceId, int start, int end, int frame) {
                            super.onRangeStart(utteranceId, start, end, frame);
                        }
                    });
                    textToSpeech.speak(edtNoidung.getText(), TextToSpeech.QUEUE_ADD, null, null);
                    solanSpeech = solanSpeech + 1;

                }
            }

        },"com.google.android.tts");

    }


}
