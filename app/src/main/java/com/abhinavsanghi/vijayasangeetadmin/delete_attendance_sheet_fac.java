package com.abhinavsanghi.vijayasangeetadmin;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

public class delete_attendance_sheet_fac extends AppCompatActivity {

    String subid,itemdate;
    String itemfacsublist;
    Spinner subjectlist;
    Spinner datelist;
    Button delatten;
    List<String> lstfacdate=new ArrayList<String>();
    List<String> lstfacsub=new ArrayList<String>();
    DatabaseReference attenrecord;
    DatabaseReference facultysubjects;
    ProgressDialog mDialog;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_attendance_sheet);
        Intent intent=getIntent();
        subid=intent.getStringExtra("tid");
        attenrecord= FirebaseDatabase.getInstance().getReference("AttendanceRecord");
        facultysubjects=FirebaseDatabase.getInstance().getReference("FacultySubjectDetails");
        mDialog=new ProgressDialog(this);
        mDialog.setTitle("Loading...");
        mDialog.setMessage("Please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        subjectlist=findViewById(R.id.spinner_delatten_sub);
        datelist=findViewById(R.id.spinner_delatten_date);
        delatten=findViewById(R.id.button_delatten);

        lstfacsub.add("Select Subject");
        ArrayAdapter<String> facultysubarrayadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lstfacsub);
        facultysubarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectlist.setAdapter(facultysubarrayadapter);

        subjectlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemfacsublist=parent.getItemAtPosition(position).toString();
                dateselect(itemfacsublist);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        facultysubjects.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.child(subid).getChildren()){
                    String name;
                    name=dsp.getKey();
                    lstfacsub.add(name);
                }

                mDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        delatten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceeding();
            }
        });





    }
    public void dateselect(final String faclst){
        mDialog.show();
        lstfacdate.clear();
        lstfacdate.add("Select date");
        attenrecord.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dspp :dataSnapshot.child(faclst).getChildren()){
                    String datetime;
                    datetime=dspp.getKey();
                    lstfacdate.add(datetime);

                }
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ArrayAdapter<String> facultydaterrayadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lstfacdate);
        facultydaterrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        datelist.setAdapter(facultydaterrayadapter);

        datelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemdate=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void proceeding(){
        if (itemfacsublist=="Select Subject"){
            Toast.makeText(getApplicationContext(),"Please Select Subject",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(itemdate=="Select date"){
            Toast.makeText(getApplicationContext(),"Please Select Subject",Toast.LENGTH_SHORT).show();
            return;

        }
        mDialog.setTitle("Deleting Attendance");
        mDialog.setMessage("Please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        deletefinal();
    }
    public void deletefinal(){
        attenrecord.child(itemfacsublist).child(itemdate).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mDialog.dismiss();
                    finish();
                    Toast.makeText(getApplicationContext(),"Deleted Successfully..",Toast.LENGTH_SHORT).show();
                }


                else{
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Oops...Failed.",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



}
