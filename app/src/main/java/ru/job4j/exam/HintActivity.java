package ru.job4j.exam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import ru.job4j.exam.store.QuestionStore;

public class HintActivity extends AppCompatActivity {
    private final Map<Integer, String> answers = QuestionStore.getAnswers();


    @Override
    protected void onCreate(@Nullable Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.hint_activity);
        TextView text = findViewById(R.id.hint);
        TextView hintQuestion = findViewById(R.id.hintQuestion);
        int question = getIntent().getIntExtra(QuestionStore.HINT_FOR, 0);
        text.setText(this.answers.get(question));
        hintQuestion.setText(QuestionStore.getInstance().get(question).getText());

        Button back = findViewById(R.id.previous);
        back.setOnClickListener(
                view -> onBackPressed()
        );
    }
}