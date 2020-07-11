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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.job4j.exam.store.ExamBaseHelper;


public class ExamFragment extends Fragment {
    private ExamBaseHelper store;
    private int position = 0;
    private int examID;
    private int size;

    private void nextBtn(View view) {
        int answer = Integer.parseInt(
                (String) Objects.requireNonNull(getView()).findViewById(
                        ((RadioGroup) getView().findViewById(R.id.variants)).getCheckedRadioButtonId()
                ).getTag());
        store.addAnswerToTheQuestion(answer, examID, position + 1);
        showAnswer(getView(), answer);
        position++;
        fillForm(getView());
    }

    private void resultBtn(View view) {
        int answer = Integer.parseInt(
                (String) Objects.requireNonNull(getView()).findViewById(
                        ((RadioGroup) getView().findViewById(R.id.variants)).getCheckedRadioButtonId()
                ).getTag()
        );
        store.addAnswerToTheQuestion(answer, examID, position + 1);
        int sumAll = 0;
        int sumCurrent = 0;
        for (Question question : store.getQuestions(examID)) {
            if (question.getAnswer() == question.getCorrect()) sumCurrent++;
            sumAll++;
        }
        store.setResultOfTheExam(examID, sumCurrent, sumAll);
        Intent intent = new Intent(getActivity(), ResultActivity.class);
        intent.putExtra("current answers", sumCurrent);
        intent.putExtra("answers", sumAll);
        startActivity(intent);
    }

    private void previousBtn(View view) {
        position--;
        fillForm(getView());
    }

    private void examList(View view) {
        Intent intent = new Intent(getActivity(), ExamListActivity.class);
        startActivity(intent);
    }

    private void hint(View view) {
        DialogFragment dialog = new ConfirmHintDialogFragment();
        dialog.show(Objects.requireNonNull(getFragmentManager()), "dialog_hint_tag");
    }

    private void dateTime(View view) {
        Intent intent = new Intent(getActivity(), DateTimeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewGroup viewGroup = (ViewGroup) getView();
        RadioGroup variants = Objects.requireNonNull(getActivity()).findViewById(R.id.variants);
        int button = variants.getCheckedRadioButtonId();
        Objects.requireNonNull(viewGroup).removeAllViewsInLayout();
        View view = onCreateView(Objects.requireNonNull(getActivity())
                .getLayoutInflater(), viewGroup, null);
        if (button != -1) {
            RadioButton checkedButton = Objects.requireNonNull(view)
                    .findViewById(button + variants.getChildCount());
            checkedButton.setChecked(true);
        }
        viewGroup.addView(view);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        this.store = ExamBaseHelper.getInstance(this.getContext());
        examID = Objects.requireNonNull(getArguments()).getInt(ExamBaseHelper.EXAM_ID, -1);
        size = store.getQuestions(examID).size();
        this.fillForm(view);
        view.findViewById(R.id.previous).setOnClickListener(this::previousBtn);
        view.findViewById(R.id.examList).setOnClickListener(this::examList);
        view.findViewById(R.id.hint).setOnClickListener(this::hint);
        view.findViewById(R.id.dateTime).setOnClickListener(this::dateTime);
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

    static ExamFragment of(int index) {
        ExamFragment exam = new ExamFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ExamBaseHelper.EXAM_ID, index);
        exam.setArguments(bundle);
        return exam;
    }

    public void onPositiveDialogClick() {
        Intent intent = new Intent(getContext(), HintActivity.class);
        intent.putExtra(ExamBaseHelper.EXAM_ID, examID);
        intent.putExtra(ExamBaseHelper.HINT_FOR, position);
        startActivity(intent);
    }

    private void fillForm(View view) {
        blockButtons(view);
        Button next = view.findViewById(R.id.next);
        final TextView text = view.findViewById(R.id.question);
        Question question = store.getQuestions(examID).get(position);
        text.setText(question.getText());
        RadioGroup variants = view.findViewById(R.id.variants);
        for (int i = 0; i < variants.getChildCount(); i++) {
            RadioButton button = (RadioButton) variants.getChildAt(i);
            button.setText(((Option) question.getOptions().get(i)).getText());
        }
        if (size == position + 1) {
            next.setOnClickListener(this::resultBtn);
            next.setText(R.string.button_result);
        } else {
            next.setOnClickListener(this::nextBtn);
            next.setText(R.string.button_next);
        }
    }

    private void showAnswer(View view, int answer) {
        Toast.makeText(
                getContext(), getString(R.string.your_answer_is) + " " +
                        answer + ", " + getString(R.string.correct_is) + " " +
                        store.getQuestions(examID).get(position).getCorrect(),
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
                    view.findViewById(R.id.previous).setEnabled(position != 1);
                    view.findViewById(R.id.next).setEnabled(true);
                }
        );
    }
}