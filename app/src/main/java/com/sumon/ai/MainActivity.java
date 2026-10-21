package com.sumon.ai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private TextView status;
    private static final int REQ_SPEECH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TextToSpeech(this, this);

        status = new TextView(this);
        status.setTextSize(18);
        status.setPadding(40, 80, 40, 40);
        status.setText("Sumon AI ready! Tap the button and speak.");

        Button mic = new Button(this);
        mic.setText("Speak now");
        mic.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startListening();
            }
        });

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(status);
        layout.addView(mic);
        setContentView(layout);
    }

    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD");
        try {
            startActivityForResult(intent, REQ_SPEECH);
        } catch (Exception e) {
            Toast.makeText(this, "Please install the Google app", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_SPEECH && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String heard = results.get(0);
                status.setText("You said: " + heard);
                reply(heard);
            }
        }
    }

    private void reply(String heard) {
        String answer;
        String h = heard.toLowerCase(Locale.US);
        if (h.contains("hello") || h.contains("hi sumon")) {
            answer = "Hello Sumon! I am Sumon AI.";
        } else if (h.contains("time")) {
            answer = "The time is " + new java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())
                    .format(new java.util.Date());
        } else if (h.contains("name")) {
            answer = "My name is Sumon AI.";
        } else {
            answer = "You said: " + heard;
        }
        tts.speak(answer, TextToSpeech.QUEUE_FLUSH, null, "sumon");
        status.append("\nSumon AI: " + answer);
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            tts.setLanguage(new Locale("bn", "BD"));
        }
    }
}
