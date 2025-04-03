package com.example.todolist1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {

    private EditText editTextTask;
    private Button buttonAdd;
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "TaskPrefs";
    private static final String KEY_TASKS = "tasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        buttonAdd = findViewById(R.id.buttonAdd);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        taskList = loadTasks();

        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerViewTasks.setAdapter(taskAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskText = editTextTask.getText().toString().trim();
                if (!taskText.isEmpty()) {
                    taskList.add(new Task(taskText, false));
                    taskAdapter.notifyDataSetChanged();
                    saveTasks();
                    editTextTask.setText("");
                }
            }
        });
    }

    @Override
    public void onTaskChecked(int position) {
        taskList.get(position).toggleCompleted();
        taskAdapter.notifyItemChanged(position);
        saveTasks();
    }

    @Override
    public void onTaskDelete(int position) {
        taskList.remove(position);
        taskAdapter.notifyItemRemoved(position);
        saveTasks();
    }

    private void saveTasks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray();
        for (Task task : taskList) {
            jsonArray.put(task.getText());
        }
        editor.putString(KEY_TASKS, jsonArray.toString());
        editor.apply();
    }

    private ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        String jsonString = sharedPreferences.getString(KEY_TASKS, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                tasks.add(new Task(jsonArray.getString(i), false));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tasks;
    }
}
