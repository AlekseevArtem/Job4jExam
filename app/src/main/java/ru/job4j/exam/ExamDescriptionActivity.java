package ru.job4j.exam;

import androidx.fragment.app.Fragment;

import ru.job4j.exam.store.ExamBaseHelper;

public class ExamDescriptionActivity extends BaseActivity {

    @Override
    public Fragment loadFrg() {
        return ExamDescriptionFragment.of(
                getIntent().getIntExtra(ExamBaseHelper.EXAM_ID, -1));
    }
}