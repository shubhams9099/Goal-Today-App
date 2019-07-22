package com.example.demo_todo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ArchieveFragment extends Fragment {
    View archieveView;
    DatabaseHelper db;
    ListView listView;
    ArrayAdapter<String> madapter;
    TaskListFragment taskListFragment;
    public ArchieveFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (archieveView != null) {
            updateList();

            return archieveView;
        }
        db = new DatabaseHelper(getActivity());
        archieveView = inflater.inflate(R.layout.fragment_archieve, container, false);
        updateList();
        taskListFragment=new TaskListFragment();
        return archieveView;
    }
    protected void updateList() {
        listView =archieveView.findViewById(R.id.tasks_archieved) ;
        ArrayList<String> tasklist = new ArrayList<>();
        Cursor cursor = db.getArchievedData();
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex("TITLE");
            tasklist.add(cursor.getString(idx));
        }
        if (madapter == null) {
            madapter = new ArrayAdapter<String>(getContext(), R.layout.items_archieve, R.id.task_title, tasklist);
            listView.setAdapter(madapter);
        } else {
            madapter.clear();
            madapter.addAll(tasklist);
            madapter.notifyDataSetChanged();
        }
        cursor.close();
    }
    protected void arc_deleteTask(View view)
    {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        int n = db.arc_deleteTask(task);
        if (n > 0)
            Toast.makeText(getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
        updateList();
    }
    protected void reAddTask(View view)
    {
        View parent=(View)view.getParent();
        TextView taskTitle=parent.findViewById(R.id.task_title);
        String task=taskTitle.getText().toString();
        boolean status=db.insertData(task);
        if(status)
            Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
        int n = db.arc_deleteTask(task);
        updateList();

    }
}
