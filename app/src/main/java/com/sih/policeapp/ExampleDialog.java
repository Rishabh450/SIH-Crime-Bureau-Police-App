package com.sih.policeapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Calendar;
import java.util.Objects;

public class ExampleDialog extends AppCompatDialogFragment implements
        View.OnClickListener {

    private EditText a,b;
    private Button c;
    private ExampleDialogListner listner;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate((R.layout.dialog),null);

        builder.setView(view)
                .setTitle("Login")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ac = txtDate.getText().toString();
                        String bc = txtTime.getText().toString();

                        listner.applyText(ac,bc);

                    }
                });
        btnDatePicker=view.findViewById(R.id.btn_date);
        btnTimePicker=view.findViewById(R.id.btn_time);
        txtDate=view.findViewById(R.id.in_date);
        txtTime=view.findViewById(R.id.in_time);
        txtDate.setFocusable(false);
        txtTime.setFocusable(false);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        return  builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listner = (ExampleDialogListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Error");
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String a = String.valueOf(dayOfMonth);
                            String b = String.valueOf(monthOfYear+1);

                            if(a.length()==1) a = "0" + a;
                            if(b.length()==1) b = "0" + b;

                            String res = a + "-" + b + "-" + year;
                            txtDate.setText(res);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            String hr ="",min="",AMpm = "",flag="no";
                            if(hourOfDay>12) {
                                flag = "yes";
                            }

                            if(flag.equals("yes"))
                            {
                                hr = String.valueOf(hourOfDay-12);
                                min = String.valueOf(minute);

                                if(hr.length()==1) hr = "0" + hr;
                                if(min.length()==1) min = "0" + min;
                                AMpm = "PM";


                            }
                            else{
                                hr = String.valueOf(hourOfDay);
                                min = String.valueOf(minute);

                                if(hr.length()==1) hr = "0" + hr;
                                if(min.length()==1) min = "0" + min;
                                AMpm = "AM";
                            }
                            String res = hr + ":" + min + " " +AMpm;
                            txtTime.setText(res);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    public interface ExampleDialogListner{
        void applyText(String a,String b);
    }

}

