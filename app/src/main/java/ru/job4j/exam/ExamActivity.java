package ru.job4j.exam;

import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.job4j.exam.store.ExamBaseHelper;

public class ExamActivity extends BaseActivity implements ConfirmHintDialogFragment.ConfirmHintDialogListener {
    @Override
    public void onPositiveDialogClick(DialogFragment dialog) {
        ExamFragment fragment = (ExamFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        Objects.requireNonNull(fragment).onPositiveDialogClick();
    }

    @Override
    public void onNegativeDialogClick(DialogFragment dialog) {
        Toast.makeText(this, "Молодец!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Fragment loadFrg() {
        return ExamFragment.of(
                getIntent().getIntExtra(ExamBaseHelper.EXAM_ID, 0)
        );
    }
}
