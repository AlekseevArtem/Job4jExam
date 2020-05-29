package ru.job4j.exam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.job4j.exam.store.QuestionStore;


public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.result_activity);

        Button back = findViewById(R.id.previous);
        back.setOnClickListener(
                view -> {
                    Intent intent = new Intent(ResultActivity.this, ExamActivity.class);
                    startActivity(intent);
                }
        );

        TextView result = findViewById(R.id.result);
        int question = getIntent().getIntExtra("current answers", 0);
        String textResult = question + " correct answers out of " + QuestionStore.getInstance().size();
        result.setText(textResult);
    }
}
