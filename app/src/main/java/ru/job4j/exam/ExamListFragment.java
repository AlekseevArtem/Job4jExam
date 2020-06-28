package ru.job4j.exam;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import ru.job4j.exam.store.ExamBaseHelper;
import ru.job4j.exam.store.ExamDbSchema;

public class ExamListFragment extends Fragment {
    private RecyclerView recycler;
    private SQLiteDatabase store;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exams_list_fragment, container, false);
        this.recycler = view.findViewById(R.id.exams_list_fragment);
        this.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.store = new ExamBaseHelper(this.getContext()).getWritableDatabase();
        updateUI();
        return view;
    }

    private void updateUI() {
        List<Exam> exams = new ArrayList<>();
        Cursor cursor = this.store.query(
                ExamDbSchema.ExamTable.NAME,
                null, null, null,
                null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            exams.add(new Exam(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    new Date(System.currentTimeMillis()),
                    100
            ));
            cursor.moveToNext();
        }
        cursor.close();
        this.recycler.setAdapter(new ExamAdapter(exams));
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.exams_list_activity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_exam:
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.content, new ExamAddFragment()).addToBackStack(null).commit();
                return true;
            case R.id.delete_exam:
                DialogFragment dialog = new ConfirmDeleteDialogFragment();
                dialog.show(Objects.requireNonNull(getFragmentManager()), "dialog_delete_tag");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteAll() {
        store.delete(ExamDbSchema.ExamTable.NAME, null, null);
        updateUI();
    }

    public class ExamHolder extends RecyclerView.ViewHolder {
        private View view;

        public ExamHolder(@NonNull View itemView) {
            super(itemView);
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
        public ExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.info_exam, parent, false);
            return new ExamHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExamHolder holder, int i) {
            Exam exam = this.exams.get(i);
            TextView text = holder.view.findViewById(R.id.q_text);
            if ((i % 2) == 0) {
                holder.view.setBackgroundColor(Color.parseColor("#d8d8d8"));
            }
            text.setText(exam.getName());
            holder.view.findViewById(R.id.edit)
                    .setOnClickListener(
                            btn -> {
                                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                                Fragment fragment = new ExamUpdateFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt("id", exam.getId());
                                bundle.putString("name", exam.getName());
                                fragment.setArguments(bundle);
                                fm.beginTransaction()
                                        .replace(R.id.content, fragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                    );

            holder.view.findViewById(R.id.delete)
                    .setOnClickListener(
                            btn -> {
                                store.delete(ExamDbSchema.ExamTable.NAME,
                                        "id = ?",
                                        new String[]{String.valueOf(exam.getId())});
                                exams.remove(exam);
                                notifyItemRemoved(i);
                            }
                    );
        }

        @Override
        public int getItemCount() {
            return this.exams.size();
        }
    }
}