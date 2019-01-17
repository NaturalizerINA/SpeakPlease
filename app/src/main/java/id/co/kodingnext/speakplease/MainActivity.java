package id.co.kodingnext.speakplease;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

//3. TODO : Adding Implements Listener RecognitionListener
public class MainActivity extends AppCompatActivity implements RecognitionListener {

    //2. TODO : Make function for speaking from the text

    //2.1 TODO : introducing the component 1
    private TextToSpeech mTTS;
    private Button btn_listen;
    private EditText et_listen;
    //2.1 TODO : introducing the component 1

    //3.1 TODO : introducing the component 1
    int result;
    public SpeechRecognizer speech = null;
    public Intent recognizerIntent;
    private Button btn_check;
    //3.1 TODO : introducing the component 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //2.2 TODO : introducing the component 2
        et_listen = findViewById(R.id.listen_et);
        btn_listen = findViewById(R.id.listen_btn);
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    result = mTTS.setLanguage(new Locale("in"));

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                        btn_listen.setEnabled(false);
                    } else {
                        btn_listen.setEnabled(true);

                        recognizerIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                        recognizerIntent.putExtra("android.speech.extra.LANGUAGE", "in");
                        recognizerIntent.putExtra("calling_package", getPackageName());
                    }
                } else {
                    btn_listen.setEnabled(false);
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        //2.2 TODO : introducing the component 2


        //2.3 TODO : adding the text from shared intent text
        Intent intent = getIntent();
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (!TextUtils.isEmpty(sharedText)) {
            et_listen.setText(sharedText);
        }
        //2.3 TODO : adding the text from shared intent text


        //3.2 TODO : introducing the component 2
        btn_check = findViewById(R.id.check_btn);
        speech = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        speech.setRecognitionListener(MainActivity.this);
        //3.2 TODO : introducing the component 2

        //3.3 TODO : adding touch function
        btn_check.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        Log.e("speech", "startListening");
                        speech.startListening(recognizerIntent);
                        break;
                    case 1:
                        Log.e("speech", "stopListening");
                        speech.stopListening();
                        break;
                }
                return false;
            }
        });
        //3.3 TODO : adding touch function

        //3.4 TODO : adding mic permission
        int MY_PERMISSIONS_RECORD_AUDIO = 1;

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_RECORD_AUDIO);
        }
        //3.4 TODO : adding mic permission
    }


    //2.3 TODO : speak the text from EditText listen
    public void doListen(View view) {
        mTTS.speak(et_listen.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
    }
    //2.3 TODO : speak the text from EditText listen

    //2.4 TODO : change the voice of speaking language, dynamically
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.indonesia_btn:
                if (checked) {
                    result = mTTS.setLanguage(new Locale("in"));
                    recognizerIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                    recognizerIntent.putExtra("android.speech.extra.LANGUAGE", "in");
                    recognizerIntent.putExtra("calling_package", getPackageName());
                }
                break;
            case R.id.english_btn:
                if (checked) {
                    result = mTTS.setLanguage(new Locale("en_US"));
                    recognizerIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                    recognizerIntent.putExtra("android.speech.extra.LANGUAGE", "en_US");
                    recognizerIntent.putExtra("calling_package", getPackageName());
                }
                break;
        }
    }
    //2.4 TODO : change the voice of speaking language, dynamically

    //2.5 TODO : adding the reset function
    public void doReset(View view) {
        mTTS.stop();
        et_listen.setText("");
    }
    //2.5 TODO : adding the reset function

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle bundle) {

        //3.5 TODO : adding the recognition voice function
        String text = "";

        Log.e("speech", "onResults");

        ArrayList data = bundle.getStringArrayList("results_recognition");

        for (int i = 0; i < data.size(); i++) {
            text = (text + data.get(i));
        }

        Log.e("speech", "content speech: " + text);
        Log.e("speech", "content text: " + et_listen.getText().toString().toLowerCase());
        //3.5 TODO : adding the recognition voice function

        //3.6 TODO : true or false when you spell it
        if (text.contains(et_listen.getText().toString().toLowerCase())) {
            showAlertDialog(true);
        } else {
            showAlertDialog(false);
        }
        //3.6 TODO : true or false when you spell it
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    //3.7 TODO : make the custom dialog
    public void showAlertDialog(Boolean answer){
        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alertdialog, viewGroup, false);

        ImageView iv_answer = dialogView.findViewById(R.id.answer_iv);
        TextView tv_title = dialogView.findViewById(R.id.title_tv);

        if(answer) {
            iv_answer.setImageResource(R.drawable.true_answer);
            tv_title.setText("Correct");
        } else {
            iv_answer.setImageResource(R.drawable.false_answer);
            tv_title.setText("Incorrect");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //3.7 TODO : make the custom dialog
}
