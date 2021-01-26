package com.example.bulgariaquiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class ResultActivity extends AppCompatActivity {
    TextView tv, tv2, tv3, resultField;
    Button btnRestart, btnResults;
    String result;
    static final int READ_BLOCK_SIZE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tv = (TextView)findViewById(R.id.tvres);
        tv2 = (TextView)findViewById(R.id.tvres2);
        tv3 = (TextView)findViewById(R.id.tvres3);
        btnRestart = (Button) findViewById(R.id.btnRestart);
        btnResults = (Button) findViewById(R.id.btnResults);
        resultField = (TextView)findViewById(R.id.resultField);

        StringBuffer sb = new StringBuffer();
        sb.append("Correct answers: " + QuestionsActivity.correct + "\n");
        StringBuffer sb2 = new StringBuffer();
        sb2.append("Wrong Answers: " + QuestionsActivity.wrong + "\n");
        StringBuffer sb3 = new StringBuffer();
        sb3.append("Final Score: " + QuestionsActivity.correct + "\n");
        tv.setText(sb);
        tv2.setText(sb2);
        tv3.setText(sb3);
        result = "Your Score: " + QuestionsActivity.correct + '\n';

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }

        try {
            File sdCard = getExternalCacheDir();
            File directory = new File(sdCard.getAbsolutePath() + "/MyFiles");
            directory.mkdirs();
            File file = new File(directory, "score.txt");
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            osw.write(result);
            osw.flush();
            osw.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        QuestionsActivity.correct=0;
        QuestionsActivity.wrong=0;

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
            }
        });

        btnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    File sdCard = getExternalCacheDir();
                    File directory = new File (sdCard.getAbsolutePath() + "/MyFiles");
                    File file = new File(directory, "score.txt");
                    FileInputStream fIn = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(fIn);

                    char[] inputBuffer = new char[READ_BLOCK_SIZE];
                    String s = "";
                    int charRead;
                    while ((charRead = isr.read(inputBuffer))>0)
                    {
                        String readString =
                                String.copyValueOf(inputBuffer, 0,
                                        charRead);
                        s += readString;
                        inputBuffer = new char[READ_BLOCK_SIZE];
                    }
                    resultField.setText(s);
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

        });
    }

}
