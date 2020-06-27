package com.abhinavsanghi.vijayasangeetadmin;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

public class view_student_inadmin_mid extends AppCompatActivity {

    String tid;
    String itemfacsublist;
    Spinner spinnersublist;
    Button submitmid;
    List<String> totalsublist=new ArrayList<String>();

    DatabaseReference batchlist;
    ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batchwiseview);
        Intent intent=getIntent();
        tid=intent.getStringExtra("tid");
        batchlist= FirebaseDatabase.getInstance().getReference("BatchName");
        mDialog=new ProgressDialog(this);
        mDialog.setTitle("Loading...");
        mDialog.setMessage("Please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        spinnersublist=findViewById(R.id.spinner_viewatten_adminsub);
        submitmid=findViewById(R.id.admin_viewatten_proceed_btn);


        totalsublist.add("Select Subject");
        ArrayAdapter<String> facultysubarrayadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,totalsublist);
        facultysubarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnersublist.setAdapter(facultysubarrayadapter);

        spinnersublist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemfacsublist=parent.getItemAtPosition(position).toString();
                //dateselect(itemfacsublist);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        batchlist.addListenerForSingleValueEvent(new ValueEventListener() {
                                                     @Override
                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                         for (DataSnapshot dsp: dataSnapshot.getChildren()){
                                                             String name;
                                                             name=dsp.getKey();
                                                             totalsublist.add(name);
                                                         }

                                                         mDialog.dismiss();

                                                     }

                                                     @Override
                                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                                     }
                                                 }


        );
        submitmid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceeding();
            }
        });




    }
    public void proceeding() {
        if (itemfacsublist == "Select Subject") {
            Toast.makeText(getApplicationContext(), "Please Select Subject", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent intent2 = new Intent(getApplicationContext(), view_student_inadmin.class);
            intent2.putExtra("subid", itemfacsublist);
            intent2.putExtra("tid",tid);
            startActivity(intent2);
        }
    }
        @Override
        public void onBackPressed() {
            super.onBackPressed();
            Intent intent=new Intent(getApplicationContext(),admin_mainpage.class);
            intent.putExtra("tid",tid);
            startActivity(intent);
        }
    }