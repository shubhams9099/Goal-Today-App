package com.example.demo_todo;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefrenceConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPrefrenceConfig(Context context){
        this.context=context;
        sharedPreferences= context.getSharedPreferences(context.getResources().getString(R.string.name_prefrence),Context.MODE_PRIVATE);
    }
    public void writeName(String name){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(context.getString(R.string.name_prefrence),name);
        editor.commit();
    }
    public void writeTime(String time){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(context.getString(R.string.time_prefrence),time);
        editor.commit();
    }
    protected String readName(){
        String name=sharedPreferences.getString(context.getResources().getString(R.string.name_prefrence),"user");
        return name;
    }
    protected String readTime(){
        String time=sharedPreferences.getString(context.getResources().getString(R.string.time_prefrence),"1200");
        return time;
    }
}
