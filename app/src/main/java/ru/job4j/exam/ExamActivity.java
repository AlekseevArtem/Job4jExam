package ru.job4j.exam;

import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import ru.job4j.exam.store.QuestionStore;

public class ExamActivity extends BaseActivity implements ConfirmHintDialogFragment.ConfirmHintDialogListener {
    @Override
    public void onPositiveDialogClick(DialogFragment dialog) {
        Intent intent = new Intent(this, HintActivity.class);
        intent.putExtra(QuestionStore.HINT_FOR, QuestionStore.getInstance().getPosition());
        startActivity(intent);
    }

    @Override
    public void onNegativeDialogClick(DialogFragment dialog) {
        Toast.makeText(this, "Молодец!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Fragment loadFrg() {
        return new ExamFragment();
    }
}
