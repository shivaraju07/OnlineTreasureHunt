package com.example.onlinetreasurehunt2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Objects;

public class ReaderActivity extends AppCompatActivity {

    private Button scan_btn;
    public Integer b;
    public Firebase mqrAnswer;
    public String Mqranswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        final String a= result.getContents();
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,result.getContents() + "1245678", Toast.LENGTH_LONG).show();
                //Toast.makeText(this,Quiz_Activity.mQuestionNumber, Toast.LENGTH_SHORT).show();
                 b = Quiz_Activity.mQuestionNumber;
                Toast.makeText(this, b.toString(), Toast.LENGTH_SHORT).show();
                mqrAnswer= new Firebase("https://onlinetreasurehunt2.firebaseio.com/Quiz_Questions/" +(b-1)+ "/qrAnswer");
                mqrAnswer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Mqranswer = dataSnapshot.getValue(String.class);
                        Toast.makeText(ReaderActivity.this,a + "1", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ReaderActivity.this,Mqranswer + "2", Toast.LENGTH_SHORT).show();
                        if (Objects.equals(a, Mqranswer)){
                            Toast.makeText(ReaderActivity.this, "lol", Toast.LENGTH_SHORT).show();
                            //Quiz_Activity.mQuestionNumber -=1;
                            Toast.makeText(ReaderActivity.this,Integer.toString(Quiz_Activity.mQuestionNumber), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ReaderActivity.this,Quiz_Activity.class);
                            startActivity(i);

                        }else{
                            Toast.makeText(ReaderActivity.this, "Go to correct place", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}