package ru.job4j.exam;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.job4j.exam.store.QuestionStore;

public class ExamFragment extends Fragment {
    private final QuestionStore store = QuestionStore.getInstance();

    private void nextBtn(View view) {
        RadioGroup variants = Objects.requireNonNull(getView()).findViewById(R.id.variants);
        showAnswer(getView());
        store.getUserAnswers().put(
                store.get(store.getPosition()).getId(),
                variants.getCheckedRadioButtonId() == store.get(this.store.getPosition()).getAnswer() ? 1 : 0);
        store.setPosition(store.getPosition() + 1);
        fillForm(getView());
    }

    private void resultBtn(View view) {
        RadioGroup variants = Objects.requireNonNull(getView()).findViewById(R.id.variants);
        store.getUserAnswers().put(
                store.get(store.getPosition()).getId(),
                variants.getCheckedRadioButtonId() == store.get(this.store.getPosition()).getAnswer() ? 1 : 0);
        int sum = 0;
        for (int value : store.getUserAnswers().values()) {
            sum = sum + value;
        }
        Intent intent = new Intent(getActivity(), ResultActivity.class);
        intent.putExtra("current answers", sum);
        startActivity(intent);
    }

    private void previousBtn(View view) {
        store.setPosition(store.getPosition() - 1);
        fillForm(getView());
    }

    private void examList(View view) {
        Intent intent = new Intent(getActivity(), ExamsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewGroup viewGroup = (ViewGroup) getView();
        Objects.requireNonNull(viewGroup).removeAllViewsInLayout();
        View view = onCreateView(Objects.requireNonNull(getActivity()).getLayoutInflater(), viewGroup, null);
        viewGroup.addView(view);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        this.fillForm(view);

        Button next = view.findViewById(R.id.next);
        next.setOnClickListener(this::nextBtn);

        Button previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(this::previousBtn);

        Button examList = view.findViewById(R.id.examList);
        examList.setOnClickListener(this::examList);

        Button hint = view.findViewById(R.id.hint);
        hint.setOnClickListener(
                v -> {
                    Intent intent = new Intent(getActivity(), HintActivity.class);
                    intent.putExtra(QuestionStore.HINT_FOR, store.getPosition());
                    startActivity(intent);
                }
        );
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle outState) {
        super.onActivityCreated(outState);
    }

    private void fillForm(View view) {
        blockButtons(view);
        Button next = view.findViewById(R.id.next);
        final TextView text = view.findViewById(R.id.question);
        Question question = store.get(this.store.getPosition());
        text.setText(question.getText());
        RadioGroup variants = view.findViewById(R.id.variants);
        for (int index = 0; index != variants.getChildCount(); index++) {
            RadioButton button = (RadioButton) variants.getChildAt(index);
            Option option = (Option) question.getOptions().get(index);
            button.setId(option.getId());
            button.setText(option.getText());
        }
        if(store.getPosition() == store.size() - 1) {
            view.findViewById(R.id.next).setOnClickListener(this::resultBtn);
        } else next.setOnClickListener(this::nextBtn);
    }

    private void showAnswer(View view) {
        RadioGroup variants = view.findViewById(R.id.variants);
        int id = variants.getCheckedRadioButtonId();
        Question question = store.get(this.store.getPosition());
        Toast.makeText(
                getContext(), "Your answer is " + id + ", correct is " + question.getAnswer(),
                Toast.LENGTH_SHORT
        ).show();
    }

    private void blockButtons(View view) {
        RadioGroup variants = view.findViewById(R.id.variants);
        variants.clearCheck();
        view.findViewById(R.id.next).setEnabled(false);
        view.findViewById(R.id.previous).setEnabled(false);
        variants.setOnCheckedChangeListener(
                (group, checkedId) -> {
                    view.findViewById(R.id.previous).setEnabled(store.getPosition() != 0);
                    view.findViewById(R.id.next).setEnabled(true);
                }
        );
    }

}