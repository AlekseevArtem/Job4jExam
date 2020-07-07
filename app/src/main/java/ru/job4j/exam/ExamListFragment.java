package ru.job4j.exam;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
                    cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.NAME)),
                    cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DATE)),
                    cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.RESULT))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        this.recycler.setAdapter(new ExamAdapter(exams));
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
            if ((i % 2) == 0) {
                holder.view.setBackgroundColor(Color.parseColor("#d8d8d8"));
            }
            ((TextView) holder.view.findViewById(R.id.q_text)).setText(exam.getName());
            ((TextView) holder.view.findViewById(R.id.date)).setText(exam.getTime());
            if (exam.getResult() != null) {
                ((TextView) holder.view.findViewById(R.id.result)).setText(exam.getResult() + "%");
            }
            holder.view.findViewById(R.id.q_text)
                    .setOnClickListener(
                            btn -> {
                                Intent intent = new Intent(getActivity(), ExamDescriptionActivity.class);
                                intent.putExtra(ExamBaseHelper.EXAM_ID, exam.getId());
                                startActivity(intent);
                            }
                    );
        }

        @Override
        public int getItemCount() {
            return this.exams.size();
        }
    }
}