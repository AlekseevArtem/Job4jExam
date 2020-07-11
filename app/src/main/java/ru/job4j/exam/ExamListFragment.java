package ru.job4j.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.job4j.exam.store.ExamBaseHelper;

public class ExamListFragment extends Fragment {
    private RecyclerView recycler;
    private ExamBaseHelper store;

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
        this.store = ExamBaseHelper.getInstance(this.getContext());
        updateUI();
        return view;
    }

    private void updateUI() {
        this.recycler.setAdapter(new ExamAdapter(store.getExams()));
    }

    public class ExamHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        private TextView text;
        private TextView result;
        private TextView date;
        private Exam exam;

        public ExamHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            text = view.findViewById(R.id.q_text);
            result = view.findViewById(R.id.result);
            date = view.findViewById(R.id.date);
        }

        public void bind(Exam exam) {
            this.exam = exam;
            view.setOnClickListener(this);
            text.setText(this.exam.getName());
            if (exam.getResult() != null) {
                result.setText(this.exam.getResult() + "%");
            }
            date.setText(this.exam.getTime());
        }

        @Override
        public void onClick(View v) {
            v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_image_click));
            Intent intent = new Intent(getActivity(), ExamDescriptionActivity.class);
            intent.putExtra(ExamBaseHelper.EXAM_ID, this.exam.getId());
            startActivity(intent);
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
            ExamHolder eh;
            View itemLayoutView;
            switch (i) {
                case 0:
                    itemLayoutView = LayoutInflater.from(parent.getContext()).
                            inflate(R.layout.info_exam_first_position, parent, false);
                    eh = new ExamHolder(itemLayoutView);
                    break;
                case 1:
                    itemLayoutView = LayoutInflater.from(parent.getContext()).
                            inflate(R.layout.info_exam, parent, false);
                    eh = new ExamHolder(itemLayoutView);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + i);
            }
            return eh;
        }

        @Override
        public void onBindViewHolder(@NonNull ExamHolder holder, int i) {
            if (i != 0) {
                holder.bind(this.exams.get(i - 1));
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        @Override
        public int getItemCount() {
            return this.exams.size() + 1;
        }
    }
}