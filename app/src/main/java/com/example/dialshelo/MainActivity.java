package com.example.dialshelo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView output;
    final int CALL_PERMISSION_REQUEST=1;
    final int SPEECH_REQUEST=2;
    TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = findViewById(R.id.text_View);
        if(getIntent().getData()!=null){
            String num=getIntent().getData().toString();
            num = num.substring(4);
            output.setText(num);
        }

        Button btn0= findViewById(R.id.btn_0);
        Button btn1 = findViewById(R.id.btn_1);
        Button btn2 = findViewById(R.id.btn_2);
        Button btn3 = findViewById(R.id.btn_3);
        Button btn4 = findViewById(R.id.btn_4);
        Button btn5 = findViewById(R.id.btn_5);
        Button btn6 = findViewById(R.id.btn_6);
        Button btn7 = findViewById(R.id.btn_7);
        Button btn8 = findViewById(R.id.btn_8);
        Button btn9 = findViewById(R.id.btn_9);

        Button star = findViewById(R.id.btn_star);
        Button sulamit = findViewById(R.id.btn_sulamit);
        ImageButton delete = findViewById(R.id.btn_delete);
        ImageButton call = findViewById(R.id.call_btn);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);

        star.setOnClickListener(this);
        sulamit.setOnClickListener(this);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String current = output.getText().toString();
                if(current.length()>0){
                   current = current.substring(0,current.length()-1);
                   output.setText(current);
                }
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(Build.VERSION.SDK_INT <23){
                makeCall();
            }
            else {
                int hasCallPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
                if (hasCallPermission == PackageManager.PERMISSION_GRANTED){
                    makeCall();
                }
                else {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},CALL_PERMISSION_REQUEST);
                }
            }
            }
        });
        ImageButton mic = findViewById(R.id.btn_mic);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                startActivityForResult(intent,SPEECH_REQUEST);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST){
           if( grantResults[0]==PackageManager.PERMISSION_GRANTED){
               makeCall();
           }
           else {
               Toast.makeText(this,getString(R.string.toast), Toast.LENGTH_LONG).show();
           }
        }

    }

    private void makeCall(){
        String number = output.getText().toString();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));
        startActivity(intent);
    }
    @Override
    public void onClick(final View view) {
        output.setText(output.getText().toString() + ((Button)view).getText().toString());
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    tts.speak(((Button)view).getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST && resultCode ==RESULT_OK){
            ArrayList<String>result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            boolean f=true;
            StringBuffer L = new StringBuffer();
            for (int i=0;i<result.size(); i++){
                String K = result.get(i);
                if (K.matches("[0-9]+")){
                  /* L.append(result.get(i));*/
                    output.setText(K);

                }
               /*if (!f){
                   Toast.makeText(MainActivity.this, "PLEASE SPEAK CLEARLY", Toast.LENGTH_SHORT).show();

               }*/
           }



        }
    }
}