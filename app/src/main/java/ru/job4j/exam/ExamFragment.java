package ru.job4j.exam;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import ru.job4j.exam.store.ExamBaseHelper;
import ru.job4j.exam.store.ExamDbSchema;


public class ExamFragment extends Fragment {
    private SQLiteDatabase store;
    private int position = 1;
    private int examID;
    private int size;

    private void nextBtn(View view) {
        int answer = Integer.parseInt(
                (String) Objects.requireNonNull(getView()).findViewById(
                        ((RadioGroup) getView().findViewById(R.id.variants)).getCheckedRadioButtonId()
                ).getTag());
        ContentValues value = new ContentValues();
        value.put(ExamDbSchema.QuestionTable.Cols.ANSWER, answer);
        store.update(ExamDbSchema.QuestionTable.NAME,
                value,
                ExamDbSchema.QuestionTable.Cols.POSITION + " = ?" + " and " +
                        ExamDbSchema.QuestionTable.Cols.EXAM_ID + " = ?",
                new String[]{String.valueOf(position), String.valueOf(examID)});
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
        ContentValues value = new ContentValues();
        value.put(ExamDbSchema.QuestionTable.Cols.ANSWER, answer);
        store.update(ExamDbSchema.QuestionTable.NAME,
                value,
                ExamDbSchema.QuestionTable.Cols.POSITION + " = ?" + " and " +
                        ExamDbSchema.QuestionTable.Cols.EXAM_ID + " = ?",
                new String[]{String.valueOf(position), String.valueOf(examID)});
        int sumAll = 0;
        int sumCurrent = 0;
        Cursor cursor = this.store.query(
                ExamDbSchema.QuestionTable.NAME,
                null,
                ExamDbSchema.QuestionTable.Cols.EXAM_ID + " = ?",
                new String[]{String.valueOf(examID)},
                null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.ANSWER))
                    == cursor.getInt(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.CORRECT))) {
                sumCurrent++;
            }
            sumAll++;
            cursor.moveToNext();
        }
        cursor.close();

        value = new ContentValues();
        value.put(ExamDbSchema.ExamTable.Cols.DATE,
                new SimpleDateFormat("dd.MM.yyyy").format(new Date(System.currentTimeMillis())));
        value.put(ExamDbSchema.ExamTable.Cols.RESULT,
                Math.round((double) sumCurrent / sumAll * Math.pow(10, 2)) / Math.pow(10, 2) * 100);
        store.update(ExamDbSchema.ExamTable.NAME,
                value,
                "id = ?",
                new String[]{String.valueOf(examID)});

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
        this.store = new ExamBaseHelper(this.getContext()).getWritableDatabase();
        examID = getArguments().getInt(ExamBaseHelper.EXAM_ID, -1);
        Cursor cursor = this.store.query(
                ExamDbSchema.QuestionTable.NAME,
                new String[]{ExamDbSchema.QuestionTable.Cols.NAME},
                ExamDbSchema.QuestionTable.Cols.EXAM_ID + " = ?",
                new String[]{String.valueOf(examID)},
                null, null, null
        );
        size = cursor.getCount();
        cursor.close();
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
        int questionID;
        Cursor cursor = this.store.query(
                ExamDbSchema.QuestionTable.NAME,
                new String[]{ExamDbSchema.QuestionTable.Cols.NAME,
                        "id"},
                ExamDbSchema.QuestionTable.Cols.EXAM_ID + " = ?" + " and " +
                        ExamDbSchema.QuestionTable.Cols.POSITION + " = ?",
                new String[]{String.valueOf(examID), String.valueOf(position)},
                null, null, null
        );
        cursor.moveToFirst();
        text.setText(cursor.getString(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.NAME)));
        questionID = cursor.getInt(cursor.getColumnIndex("id"));
        cursor.close();
        RadioGroup variants = view.findViewById(R.id.variants);
        cursor = this.store.query(
                ExamDbSchema.OptionTable.NAME,
                null,
                ExamDbSchema.OptionTable.Cols.QUESTION_ID + " = ?",
                new String[]{String.valueOf(questionID)},
                null, null, null
        );
        cursor.moveToFirst();
        int index = 0;
        while (!cursor.isAfterLast()) {
            RadioButton button = (RadioButton) variants.getChildAt(index);
            button.setText(cursor.getString(cursor.getColumnIndex(ExamDbSchema.OptionTable.Cols.TEXT)));
            index++;
            cursor.moveToNext();
        }
        cursor.close();
        if (size == position) {
            next.setOnClickListener(this::resultBtn);
            next.setText("Result");
        } else {
            next.setOnClickListener(this::nextBtn);
            next.setText("Next");
        }
    }

    private void showAnswer(View view, int answer) {
        Cursor cursor = this.store.query(
                ExamDbSchema.QuestionTable.NAME,
                new String[]{ExamDbSchema.QuestionTable.Cols.CORRECT},
                ExamDbSchema.QuestionTable.Cols.EXAM_ID + " = ?" + " and " +
                        ExamDbSchema.QuestionTable.Cols.POSITION + " = ?",
                new String[]{String.valueOf(examID), String.valueOf(position)},
                null, null, null
        );
        cursor.moveToFirst();
        Toast.makeText(
                getContext(), "Your answer is " + answer + ", correct is " +
                        cursor.getInt(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.CORRECT)),
                Toast.LENGTH_SHORT
        ).show();
        cursor.close();
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