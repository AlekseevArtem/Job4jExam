package ru.job4j.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;


public class ResultFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_and_hint_activity, container, false);
        Button back = view.findViewById(R.id.previous);
        back.setText("Back to exams list");
        back.setOnClickListener(
                v -> {
                    Intent intent = new Intent(getContext(), ExamListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
        );
        TextView result = view.findViewById(R.id.hint_or_result);
        int currentAnswers = Objects.requireNonNull(getArguments()).getInt("current answers", 0);
        int allAnswers = Objects.requireNonNull(getArguments()).getInt("answers", 0);
        String textResult = currentAnswers + " correct answers out of " + allAnswers;
        result.setText(textResult);
        return view;
    }

    static ResultFragment of(int currentAnswers, int AllAnswers) {
        ResultFragment result = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("current answers", currentAnswers);
        bundle.putInt("answers", AllAnswers);
        result.setArguments(bundle);
        return result;
    }
}
