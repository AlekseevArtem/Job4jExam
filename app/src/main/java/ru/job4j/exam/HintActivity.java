package ru.job4j.exam;

import androidx.fragment.app.Fragment;

import ru.job4j.exam.store.ExamBaseHelper;

public class HintActivity extends BaseActivity{

    @Override
    public Fragment loadFrg() {
        return HintFragment.of(
                getIntent().getIntExtra(ExamBaseHelper.EXAM_ID, -1),
                getIntent().getIntExtra(ExamBaseHelper.HINT_FOR, -1)
        );
    }
}