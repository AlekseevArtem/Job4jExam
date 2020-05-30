package ru.job4j.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ru.job4j.exam.store.QuestionStore;

public class ExamActivity extends AppCompatActivity {
    private final QuestionStore store = QuestionStore.getInstance();

    private void nextBtn(View view) {
        RadioGroup variants = findViewById(R.id.variants);
        showAnswer();
        store.getUserAnswers().put(
                store.get(store.getPosition()).getId(),
                variants.getCheckedRadioButtonId() == store.get(this.store.getPosition()).getAnswer() ? 1 : 0);
        store.setPosition(store.getPosition() + 1);
        fillForm();
    }

    private void resultBtn(View view) {
        RadioGroup variants = findViewById(R.id.variants);
        store.getUserAnswers().put(
                store.get(store.getPosition()).getId(),
                variants.getCheckedRadioButtonId() == store.get(this.store.getPosition()).getAnswer() ? 1 : 0);
        int sum = 0;
        for (int value : store.getUserAnswers().values()) {
            sum = sum + value;
        }
        Intent intent = new Intent(ExamActivity.this, ResultActivity.class);
        intent.putExtra("current answers", sum);
        startActivity(intent);
    }

    private void previousBtn(View view) {
        store.setPosition(store.getPosition() - 1);
        fillForm();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fillForm();

        Button next = findViewById(R.id.next);
        next.setOnClickListener(this::nextBtn);

        Button previous = findViewById(R.id.previous);
        previous.setOnClickListener(this::previousBtn);

        Button hint = findViewById(R.id.hint);
        hint.setOnClickListener(
                view -> {
                    Intent intent = new Intent(ExamActivity.this, HintActivity.class);
                    intent.putExtra(QuestionStore.HINT_FOR, store.getPosition());
                    startActivity(intent);
                }
        );
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void fillForm() {
        blockButtons();
        Button next = findViewById(R.id.next);
        final TextView text = findViewById(R.id.question);
        Question question = store.get(this.store.getPosition());
        text.setText(question.getText());
        RadioGroup variants = findViewById(R.id.variants);
        for (int index = 0; index != variants.getChildCount(); index++) {
            RadioButton button = (RadioButton) variants.getChildAt(index);
            Option option = (Option) question.getOptions().get(index);
            button.setId(option.getId());
            button.setText(option.getText());
        }
        if(store.getPosition() == store.size() - 1) {
            findViewById(R.id.next).setOnClickListener(this::resultBtn);
        } else next.setOnClickListener(this::nextBtn);
    }

    private void showAnswer() {
        RadioGroup variants = findViewById(R.id.variants);
        int id = variants.getCheckedRadioButtonId();
        Question question = store.get(this.store.getPosition());
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
                    findViewById(R.id.previous).setEnabled(store.getPosition() != 0);
                    findViewById(R.id.next).setEnabled(true);
                }
        );
    }
}
