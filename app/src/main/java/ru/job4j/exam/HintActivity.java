package ru.job4j.exam;

import androidx.fragment.app.Fragment;

import ru.job4j.exam.store.QuestionStore;

public class HintActivity extends BaseActivity{

    @Override
    public Fragment loadFrg() {
        return HintFragment.of(
                getIntent().getIntExtra(QuestionStore.HINT_FOR, 0)
        );
    }
}