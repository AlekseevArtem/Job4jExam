package ru.job4j.exam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import ru.job4j.exam.store.QuestionStore;

public class ExamActivity extends AppCompatActivity {
    private static final String TAG = "ExamActivity";
    private int flip;
    private int position = 0;
    private Map<Integer, Integer> userAnswers = new HashMap<>();
    public static final String HINT_FOR = "hint_for";
    private final QuestionStore store = QuestionStore.getInstance();

    private void nextBtn(View view) {
        RadioGroup variants = findViewById(R.id.variants);
        showAnswer();
        userAnswers.put(store.get(position).getId(), variants.getCheckedRadioButtonId() == store.get(this.position).getAnswer() ? 1 : 0);
        position++;
        fillForm();
    }

    private void resultBtn(View view) {

        RadioGroup variants = findViewById(R.id.variants);
        userAnswers.put(store.get(position).getId(), variants.getCheckedRadioButtonId() == store.get(this.position).getAnswer() ? 1 : 0);
        int sum = 0;
        for (int value : userAnswers.values()) {
            sum = sum + value;
        }
        Intent intent = new Intent(ExamActivity.this, ResultActivity.class);
        intent.putExtra("current answers", sum);
        startActivity(intent);
    }

    private void previousBtn(View view) {
        position--;
        fillForm();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fillForm();
        if (savedInstanceState != null) {
            flip = savedInstanceState.getInt("Flips");
        }
        Log.d(TAG, "OnCreate");

        Button next = findViewById(R.id.next);
        next.setOnClickListener(this::nextBtn);

        Button previous = findViewById(R.id.previous);
        previous.setOnClickListener(this::previousBtn);

        Button hint = findViewById(R.id.hint);
        hint.setOnClickListener(
                view -> {
                    Intent intent = new Intent(ExamActivity.this, HintActivity.class);
                    intent.putExtra(HINT_FOR, position);
                    startActivity(intent);
                }
        );
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        this.flip++;
        outState.putInt("Flips", flip);
        Log.d(TAG, "Flip " + flip + " times");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private void fillForm() {
        blockButtons();
        Button next = findViewById(R.id.next);
        final TextView text = findViewById(R.id.question);
        Question question = store.get(this.position);
        text.setText(question.getText());
        RadioGroup variants = findViewById(R.id.variants);
        for (int index = 0; index != variants.getChildCount(); index++) {
            RadioButton button = (RadioButton) variants.getChildAt(index);
            Option option = (Option) question.getOptions().get(index);
            button.setId(option.getId());
            button.setText(option.getText());
        }
        if(position == store.size() - 1) {
            findViewById(R.id.next).setOnClickListener(this::resultBtn);
        } else next.setOnClickListener(this::nextBtn);
    }

    private void showAnswer() {
        RadioGroup variants = findViewById(R.id.variants);
        int id = variants.getCheckedRadioButtonId();
        Question question = store.get(this.position);
        Toast.makeText(
                this, "Your answer is " + id + ", correct is " + question.getAnswer(),
                Toast.LENGTH_SHORT
        ).show();
    }

    private void blockButtons() {
        RadioGroup variants = findViewById(R.id.variants);
        variants.clearCheck();
        findViewById(R.id.next).setEnabled(false);
        findViewById(R.id.previous).setEnabled(false);
        variants.setOnCheckedChangeListener(
                (group, checkedId) -> {
                    findViewById(R.id.previous).setEnabled(position != 0);
                    findViewById(R.id.next).setEnabled(true);
                }
        );
    }
}
