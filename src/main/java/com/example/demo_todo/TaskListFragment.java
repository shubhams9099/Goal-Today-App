package com.example.demo_todo;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class TaskListFragment extends Fragment {
    ListView listView;
    ArrayAdapter<String> madapter;
    View myview;

    private DatabaseHelper db;

    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (myview != null) {
            /*if ((ViewGroup) myview.getParent() != null)
                ((ViewGroup) myview.getParent()).removeView(myview);*/
            updateUI();
            return myview;
        }
        db = new DatabaseHelper(getActivity());
        myview = inflater.inflate(R.layout.fragment_task_list, container, false);
        demo();
        return myview;

    }

    public void deleteTask(View view) {
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        int n = db.deleteData(task);
        if (n > 0)
            Toast.makeText(getContext(), "Task added to archieve", Toast.LENGTH_SHORT).show();
        updateUI();
        }

    public void demo() {
        FloatingActionButton fab = myview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTask();
            }
        });
        updateUI();
    }

    public void addNewTask() {
        final EditText taskEditText = new EditText(getActivity());
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Add a new task")
                .setMessage("Add title to task")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        addTask(task);
                        updateUI();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void addTask(String title) {
        boolean status = db.insertData(title);
        if (status)
            Toast.makeText(getActivity(), "Task added", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "Task added", Toast.LENGTH_SHORT).show();
    }

    protected void updateUI() {
        listView = myview.findViewById(R.id.items_todo);
        ArrayList<String> tasklist = new ArrayList<>();
        Cursor cursor = db.getData();
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex("TITLE");
            tasklist.add(cursor.getString(idx));
        }
        if (madapter == null) {
            madapter = new ArrayAdapter<String>(getContext(), R.layout.items_todo, R.id.task_title, tasklist);
            listView.setAdapter(madapter);
        } else {
            madapter.clear();
            madapter.addAll(tasklist);
            madapter.notifyDataSetChanged();
        }
        cursor.close();
    }

}
