package ru.job4j.exam;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import ru.job4j.exam.store.ExamBaseHelper;
import ru.job4j.exam.store.ExamDbSchema;

public class HintFragment extends Fragment {
    private SQLiteDatabase store;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_and_hint_activity, container, false);
        TextView text = view.findViewById(R.id.hint_or_result);
        TextView hintQuestion = view.findViewById(R.id.hintQuestion);
        this.store = new ExamBaseHelper(this.getContext()).getWritableDatabase();
        int questionID = Objects.requireNonNull(getArguments()).getInt(ExamBaseHelper.HINT_FOR, 0);
        int examID = Objects.requireNonNull(getArguments()).getInt(ExamBaseHelper.EXAM_ID, 0);
        Cursor cursor = this.store.query(
                ExamDbSchema.QuestionTable.NAME,
                new String[]{ExamDbSchema.QuestionTable.Cols.NAME,
                        ExamDbSchema.QuestionTable.Cols.HINT},
                ExamDbSchema.QuestionTable.Cols.POSITION + " = ?" + " and " +
                        ExamDbSchema.QuestionTable.Cols.EXAM_ID + " = ?",
                new String[]{String.valueOf(questionID), String.valueOf(examID)},
                null, null, null
        );
        cursor.moveToFirst();
        hintQuestion.setText(cursor.getString(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.NAME)));
        text.setText(cursor.getString(cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.HINT)));
        cursor.close();

        Button back = view.findViewById(R.id.previous);
        back.setOnClickListener(
                v -> Objects.requireNonNull(getActivity()).onBackPressed()
        );
        return view;
    }

    static HintFragment of(int examID, int questionID) {
        HintFragment hint = new HintFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ExamBaseHelper.EXAM_ID, examID);
        bundle.putInt(ExamBaseHelper.HINT_FOR, questionID);
        hint.setArguments(bundle);
        return hint;
    }
}

