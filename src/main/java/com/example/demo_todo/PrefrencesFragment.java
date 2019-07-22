package com.example.demo_todo;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class PrefrencesFragment extends DialogFragment {
    SharedPrefrenceConfig sharedPrefrenceConfig;
    EditText editText;
    String name="";
    String time="";
    View myview;
    Button save_btn;
    public PrefrencesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myview= inflater.inflate(R.layout.fragment_prefrences, container, false);
        sharedPrefrenceConfig =new SharedPrefrenceConfig(getActivity());
        editText= myview.findViewById(R.id.user_name);
        editText.setText(sharedPrefrenceConfig.readName());
        save_btn=myview.findViewById(R.id.save_changes);
        resolveName();
        return myview;
    }
    protected void resolveName(){
        save_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uname=editText.getText().toString();
                        sharedPrefrenceConfig.writeName(uname);
                        editText.setText(uname);
                        }
                }
        );
    }

}
