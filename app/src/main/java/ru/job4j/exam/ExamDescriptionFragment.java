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

import ru.job4j.exam.store.ExamBaseHelper;

public class ExamDescriptionFragment extends Fragment {
    private ExamBaseHelper store = ExamBaseHelper.getInstance(getContext());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exam_description_fragment, container, false);
        int examID = Objects.requireNonNull(getArguments()).getInt(ExamBaseHelper.EXAM_ID, 0);
        Button start = view.findViewById(R.id.description_fragment_start);
        start.setOnClickListener(view1 -> clickOnStartExam(view1, examID));
        Exam exam = store.getExams().stream().filter(e -> e.getId() == examID).findFirst().orElse(null);
        ((TextView) view.findViewById(R.id.description_fragment_name)).setText(Objects.requireNonNull(exam).getName());
        ((TextView) view.findViewById(R.id.description_fragment_desc)).setText(exam.getDesc());
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
