package ru.job4j.exam;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

public class ExamsActivity extends AppCompatActivity
        implements ConfirmDeleteDialogFragment.ConfirmDeleteDialogListener {

    private final FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.exams_activity);
        Fragment fragment = manager.findFragmentById(R.id.content);
        if (fragment == null) {
            fragment = new ExamListFragment();
            manager.beginTransaction()
                    .add(R.id.content, fragment)
                    .commit();
        }
    }

    @Override
    public void onPositiveDialogClick(DialogFragment dialog) {
        ExamListFragment fragment = (ExamListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        Objects.requireNonNull(fragment).deleteAll();
    }

    @Override
    public void onNegativeDialogClick(DialogFragment dialog) {

    }
}