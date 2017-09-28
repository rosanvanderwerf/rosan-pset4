package com.example.rosan.tasklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText inputTask;
    Context context;
    DBHelper helper;
    ArrayList<Task> taskList;
    ListView taskView;
    TextView taskTitle;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For adding new Task
        inputTask = (EditText) findViewById(R.id.inputTask);

        // Create new DataBase Helper
        context = this;
        helper = new DBHelper(context);

        taskList = helper.read();
        taskView = (ListView) findViewById(R.id.taskView);

        // Delete on LongClick
        taskView.setOnItemLongClickListener(new deleteTask());
        taskView.setOnItemClickListener(new checkTask());

        /* Empty */
        if(taskList.isEmpty()){
            Task todo = new Task("The things I really ought to do");
            Task add = new Task("Add tasks!");
            Task click = new Task("Long click to delete task");
            helper.create(todo);
            helper.create(add);
            helper.create(click);
        }

        loadTaskList();
    }

    private class deleteTask implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View v, int i, long id) {
            TaskAdapter taskAdapter = new TaskAdapter(MainActivity.this, taskList);
            Task task = taskAdapter.getItem(i);
            helper.delete(task);
            taskList = helper.read();
            loadTaskList();
            return true;
        }
    }

    private class checkTask implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
            TaskAdapter taskAdapter = new TaskAdapter(MainActivity.this, taskList);
            Task task = taskAdapter.getItem(i);
            v.setBackgroundColor(Color.parseColor("#93bed2"));
            assert task != null;
            task.setCompleted("Completed");
            helper.update(task);
            loadTaskList();
        }
    }

    public void addTask(View v){
        String input = inputTask.getText().toString();
        if(input.trim().length() > 0){
            Toast.makeText(this, "task is added", Toast.LENGTH_SHORT).show();
            task = new Task(input);
            helper.create(task);
            inputTask.setText("");
            taskList = helper.read();
            loadTaskList();
        } else {
            Toast.makeText(this, "please no", Toast.LENGTH_SHORT).show();
        }
    }

    private class TaskAdapter extends ArrayAdapter<Task>{
        TaskAdapter(Context context, ArrayList<Task> taskList){
            super(context, 0, taskList);
        }

        @NonNull
        @Override
        public View getView(int i, View v, @NonNull ViewGroup parent) {
            Task task = getItem(i);
            if (v == null){
                v = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
            }
            taskTitle = v.findViewById(R.id.taskTitle);
            assert task != null;
            taskTitle.setText(task.name);
            if (task.getCompleted().equals("Completed")){
                v.setBackgroundColor(Color.parseColor("#93bed2"));
            }
            return v;
        }
    }

    private void loadTaskList() {
        TaskAdapter taskAdapter = new TaskAdapter(this, taskList);
        assert taskView != null;
        taskView.setAdapter(taskAdapter);
    }
}