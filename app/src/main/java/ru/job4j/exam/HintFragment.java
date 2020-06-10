package ru.job4j.exam;

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

public class HintFragment extends Fragment {
    private final QuestionStore store = QuestionStore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_and_hint_activity, container, false);
        TextView text = view.findViewById(R.id.hint_or_result);
        TextView hintQuestion = view.findViewById(R.id.hintQuestion);
        int question = Objects.requireNonNull(getArguments()).getInt(QuestionStore.HINT_FOR, 0);
        text.setText(this.store.getAnswers().get(question));
        hintQuestion.setText(QuestionStore.getInstance().get(question).getText());
        Button back = view.findViewById(R.id.previous);
        back.setOnClickListener(
                v -> Objects.requireNonNull(getActivity()).onBackPressed()
        );
        return view;
    }

    static HintFragment of(int index) {
        HintFragment hint = new HintFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(QuestionStore.HINT_FOR, index);
        hint.setArguments(bundle);
        return hint;
    }
}

