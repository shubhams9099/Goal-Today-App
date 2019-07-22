    package com.example.demo_todo;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TaskListFragment taskListFragment;
    private ArchieveFragment archieveFragment;
    private PrefrencesFragment prefrencesFragment;
    DatabaseHelper databaseHelper;
    static final int dialog_id=0;
    int hour_u=0;
    int min_u=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        databaseHelper=new DatabaseHelper(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        taskListFragment=new  TaskListFragment();
        archieveFragment=new ArchieveFragment();
        prefrencesFragment=new PrefrencesFragment();
        setFragment(taskListFragment);
        NotificationHandler();

    }
    private void NotificationHandler(){
        Intent notifyIntent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (this, 100, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 60*60 *1, pendingIntent);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about_developer) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.about_info);
            dialog.show();
            return true;
        }
        else if(id==R.id.privacy_policy){
            Dialog dialog = new Dialog(this);
            dialog.setTitle("Privacy Policy");
            dialog.setContentView(R.layout.privacy_policy);
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id)
        {
            case R.id.nav_tasks:
                setFragment(taskListFragment);
                break;
            case R.id.nav_archieve:
                setFragment(archieveFragment);
                break;
            case R.id.nav_manage:
                setFragment(prefrencesFragment);
                break;
            case R.id.nav_send:
                Intent mailIntent=new Intent(Intent.ACTION_SENDTO);
                mailIntent.setType("text/html");
                mailIntent.setData(Uri.parse("mailto:shahs9099@gmail.com"));
                mailIntent.putExtra(Intent.EXTRA_EMAIL,"shahs9099@gmail.com");
                mailIntent.putExtra(Intent.EXTRA_SUBJECT,"Feedback of the app");
                mailIntent.putExtra(Intent.EXTRA_TEXT,"Hey I just found that ..");
                try{
                    startActivity(Intent.createChooser(mailIntent,"Send Feedback using :"));
                }
                catch (Exception e){
                    Toast.makeText(this, "Please select appropriate app", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_share:
                Intent myintent=new Intent(Intent.ACTION_SEND);
                myintent.setType("text/plain");
                String subject="Goal Today";
                String body="Check this app at: https://drive.google.com/open?id=1Q6120KzF6qdfKR2MPykHf43Ux8YKbZdC";
                myintent.putExtra(Intent.EXTRA_SUBJECT,subject);
                myintent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(myintent,"Share Using"));
                break;
                default:
                    setFragment(taskListFragment);
                    break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,fragment);
        fragmentTransaction.commit();
    }

    public void deleteTask(View view) {
        taskListFragment.deleteTask(view);
    }

    public void arc_deleteTask(View view) {
        archieveFragment.arc_deleteTask(view);
    }

    public void arc_readdTask(View view) {
        archieveFragment.reAddTask(view);
    }

    public void showTimePickerDialog(View view) {
        showDialog(dialog_id);
    }
    @Override
    public Dialog onCreateDialog(int id){
        if(id==dialog_id)
            return new TimePickerDialog(this,kTimePickerListener,hour_u,min_u,true);
        return null;
    }
    protected TimePickerDialog.OnTimeSetListener kTimePickerListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_u=hourOfDay;
            min_u=minute;
            Toast.makeText(MainActivity.this,"You have to finish tasks before" +hour_u+":"+min_u, Toast.LENGTH_SHORT).show();
        }
    };

}
