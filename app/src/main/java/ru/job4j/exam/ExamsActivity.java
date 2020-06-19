package ru.job4j.exam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ExamsActivity extends AppCompatActivity implements ConfirmDeleteDialogFragment.ConfirmDeleteDialogListener {
    private RecyclerView recycler;
    private List<Exam> exams = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.exams_activity);
        this.recycler = findViewById(R.id.exams_activity);
        this.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exams_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                addOne();
                return true;
            case R.id.delete_item:
                DialogFragment dialog = new ConfirmDeleteDialogFragment();
                dialog.show(Objects.requireNonNull(getSupportFragmentManager()), "dialog_delete_tag");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        for (int index = 0; index != 100; index++) {
            exams.add(new Exam(index, String.format("Exam %s", index), new Date(System.currentTimeMillis()), index));
        }
        this.recycler.setAdapter(new ExamAdapter(exams));
    }

    private void deleteAll() {
        exams.clear();
        Objects.requireNonNull(recycler.getAdapter()).notifyDataSetChanged();
    }

    private void addOne() {
        int index = exams.size();
        exams.add(new Exam(index, String.format("Exam %s", index), new Date(System.currentTimeMillis()), index));
        Objects.requireNonNull(recycler.getAdapter()).notifyItemInserted(index);
    }

    @Override
    public void onPositiveDialogClick(DialogFragment dialog) {
        deleteAll();
    }

    @Override
    public void onNegativeDialogClick(DialogFragment dialog) {
    }

    public class ExamHolder extends RecyclerView.ViewHolder {
        private View view;

        public ExamHolder(@NonNull View view) {
            super(view);
            this.view = itemView;
        }
    }

    public class ExamAdapter extends RecyclerView.Adapter<ExamHolder> {
        private final List<Exam> exams;
        public ExamAdapter(List<Exam> exams) {
            this.exams = exams;
        }
        @NonNull
        @Override
        public ExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.info_exam, parent, false);
            return new ExamHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExamHolder holder, int i) {
            final Exam exam = this.exams.get(i);
            TextView text = holder.view.findViewById(R.id.info);
            text.setText(exam.getName());
            TextView result = holder.view.findViewById(R.id.result);
            result.setText(String.valueOf(exam.getResult()));
            TextView date = holder.view.findViewById(R.id.date);
            @SuppressLint("SimpleDateFormat") String data = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss E").format(exam.getTime());
            date.setText(data);
            text.setOnClickListener(view -> openToast(view, exam));
        }

        private void openToast(View view, Exam exam) {
            Toast.makeText(
                    getApplicationContext(), "You select " + exam,
                    Toast.LENGTH_SHORT
            ).show();
            Intent intent = new Intent(getApplicationContext(), ExamActivity.class);
            startActivity(intent);
        }

        @Override
        public int getItemCount() {
            return this.exams.size();
        }
    }
}