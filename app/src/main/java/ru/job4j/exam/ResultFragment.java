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

import ru.job4j.exam.store.QuestionStore;

public class ResultFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_and_hint_activity, container, false);

        Button back = view.findViewById(R.id.previous);
        back.setOnClickListener(
                v -> {
                    Intent intent = new Intent(getActivity(), ExamActivity.class);
                    QuestionStore.getInstance().setPosition(0);
                    QuestionStore.getInstance().getUserAnswers().clear();
                    startActivity(intent);
                }
        );

        TextView result = view.findViewById(R.id.hint_or_result);
        int question = Objects.requireNonNull(getArguments()).getInt("current answers", 0);
        String textResult = question + " correct answers out of " + QuestionStore.getInstance().size();
        result.setText(textResult);
        return view;
    }

    static ResultFragment of(int index) {
        ResultFragment result = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("current answers", index);
        result.setArguments(bundle);
        return result;
    }
}
