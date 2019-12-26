package com.example.chatapp;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentSender;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetData {
    Context context;
    String url;

    private ArrayList<DataCall> arrayListDataCall =  new ArrayList<>() ;

    public void setArrayListDataCall(ArrayList<DataCall> arrayListDataCall) {
        this.arrayListDataCall = arrayListDataCall;
    }

    public ArrayList<DataCall> getArrayListDataCall() {
        return arrayListDataCall;
    }


    public GetData(Context context, String url) {
        this.context = context;
        this.url = url;


    }




    public void ReadJSON() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray> () {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                MainActivity.arrayList.add(new DataCall(jsonObject.getString("ID"),
                                        jsonObject.getString("SoDienThoai"),
                                        jsonObject.getString("NoiDung"),
                                        jsonObject.getString("KetQua")));
                                MainActivity.arrayList.clone();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }


        );
        requestQueue.add(jsonArrayRequest);

        requestQueue.addRequestFinishedListener(request -> { jsonArrayRequest.getTag();
        });

    }



}
