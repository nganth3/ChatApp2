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
import android.widget.ListView;
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
    public static EditText edtIP;
    Button btnGetData, btnConect,btnStart;
    RadioButton rdoSpeech,rdoCall;
    public static ConnectSV connectSV;
    public static  int solanSpeech = 0;
    public static int soID;
    static String  Loai;
    public static TextView txtLoai,txtNoidung;
    private static MainActivity instance;
    public TextToSpeech textToSpeech;
    public ArrayList<DataCall> arrayList;
    public static   DataCallAdapter dataCallAdapter;
    public ListView lsDataCall;
    String IP;
    GetData getData;
    public static DataCall dataCalle_First;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        instance = this;
        anhXa();
       // arrayList = new ArrayList<>();
        btnConect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("soID", String.valueOf(soID));

                connectSV = new ConnectSV();
                if (rdoSpeech.isChecked()) {
                    Loai = "Speech";
                }
                else{
                    Loai = "Call";
                }
                connectSV.setmSocket(Loai);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectSV.sendValue("Start_with ID:" + soID);
            }


        });

        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soID = 0;
                arrayList = new ArrayList<>();
                IP=edtIP.getText().toString();
                getData = new GetData(MainActivity.this,IP,arrayList);
                getData.ReadJSON();
                dataCallAdapter = new DataCallAdapter(MainActivity.this,R.layout.layout_listview,arrayList);
                lsDataCall.setAdapter(dataCallAdapter);
                // dataCallAdapter.notifyDataSetChanged();
                //  getData.GetOneData();
            }
        });

    }

    public void startting(int ID){
       int soluong = arrayList.size();

       if (ID <= soluong - 1) {
           if (txtLoai.getText().equals("Call")) {
               txtNoidung.setText(arrayList.get(ID).getSodienthoai());
              // txtNoidung.setText(arrayList.get(ID).getNoidung());
           } else {
               txtNoidung.setText(arrayList.get(ID).getNoidung());
           }
           soID++;
       }else{
           txtNoidung.setText("Đã thực hiện hết DS");
       }

    }


    private void anhXa(){
        rdoCall = findViewById(R.id.radioButton_Calls);
        rdoSpeech = findViewById(R.id.radioButton_Speech);
        btnConect = findViewById(R.id.button_Connect);
        txtLoai = findViewById(R.id.textView_tiltle);
        btnStart = findViewById(R.id.button_Start);
        edtIP = findViewById(R.id.editText_IP);
        txtNoidung = findViewById(R.id.textView_noidung);
        lsDataCall = findViewById(R.id.ListView_Datacall);
        btnGetData = findViewById(R.id.button_GetData);
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
        Uri uri = Uri.parse("tel:0"+txtNoidung.getText());
        Log.d("DDD","XXXXX");
        startActivity(new Intent(Intent.ACTION_CALL, uri));/*
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
                    textToSpeech.speak(txtNoidung.getText(), TextToSpeech.QUEUE_ADD, null, null);
                    solanSpeech = solanSpeech + 1;

                }
            }

        },"com.google.android.tts");

    }


}
