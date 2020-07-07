package ru.job4j.exam;

import android.content.Intent;
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

public class ExamDescriptionFragment extends Fragment {
    private SQLiteDatabase store;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exam_description_fragment, container, false);
        store = new ExamBaseHelper(getContext()).getWritableDatabase();
        int examID = Objects.requireNonNull(getArguments()).getInt(ExamBaseHelper.EXAM_ID, 0);
        Button start = view.findViewById(R.id.description_fragment_start);
        start.setOnClickListener(view1 -> clickOnStartExam(view1, examID));

        Cursor cursor = this.store.query(
                ExamDbSchema.ExamTable.NAME,
                null,
                "id = ?",
                new String[]{String.valueOf(examID)},
                null, null, null
        );
        cursor.moveToFirst();
        ((TextView) view.findViewById(R.id.description_fragment_name)).
                setText(cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.NAME)));
        ((TextView) view.findViewById(R.id.description_fragment_desc)).
                setText(cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DESC)));
        cursor.close();
        return view;
    }

    private void clickOnStartExam(View view, int examID) {
        Intent intent = new Intent(getActivity(), ExamActivity.class);
        intent.putExtra(ExamBaseHelper.EXAM_ID, examID);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }

    static ExamDescriptionFragment of(int examID) {
        ExamDescriptionFragment result = new ExamDescriptionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ExamBaseHelper.EXAM_ID, examID);
        result.setArguments(bundle);
        return result;
    }
}
