package com.binplus.earnquizmoney.Fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import com.binplus.earnquizmoney.Adapters.StudentAdapter;
import com.binplus.earnquizmoney.Model.Student;
import com.binplus.earnquizmoney.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingFragment extends Fragment {
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<Student> studentList = new ArrayList<>();
    private EditText etName, etMarks;
    private TextView tvTime;
    private AppCompatButton btnAdd;
    private AppCompatButton pickTimeBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        etName = view.findViewById(R.id.et_name);
        etMarks = view.findViewById(R.id.et_marks);
        tvTime = view.findViewById(R.id.tv_time);
        btnAdd = view.findViewById(R.id.btn_add);
        pickTimeBtn = view.findViewById(R.id.idBtnPickTime);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StudentAdapter(studentList);
        recyclerView.setAdapter(adapter);

        pickTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                tvTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String marksStr = etMarks.getText().toString().trim();
            String timeStr = tvTime.getText().toString().trim();

            if (!name.isEmpty() && !marksStr.isEmpty() && !timeStr.isEmpty()) {
                int marks = Integer.parseInt(marksStr);
                long time = parseTimeString(timeStr);
                addStudent(name, marks, time);
                etName.setText("");
                etMarks.setText("");
                tvTime.setText(R.string.time);
            }
        });

        return view;
    }

    private long parseTimeString(String timeStr) {
        String[] parts = timeStr.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60L + minutes;
    }

    private void addStudent(String name, int marks, long time) {
        Student student = new Student(name, marks, time);
        studentList.add(student);
        updateRanks();
        adapter.notifyDataSetChanged();
    }

    private void updateRanks() {
        Collections.sort(studentList,(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                if (o1.getMarks() != o2.getMarks()) {
                    return Integer.compare(o2.getMarks(), o1.getMarks());
                } else {
                    return Long.compare(o1.getTime(), o2.getTime());
                }
            }
        }));
        int currentRank = 1;
        for (int i = 0; i < studentList.size(); i++) {
            if (i > 0) {
                Student prevStudent = studentList.get(i - 1);
                Student currStudent = studentList.get(i);
                if (currStudent.getMarks() < prevStudent.getMarks() ||
                        (currStudent.getMarks() == prevStudent.getMarks() && currStudent.getTime() > prevStudent.getTime())) {
                    currentRank = i + 1;
                }
            }
            studentList.get(i).setRank(currentRank);
        }
    }
}