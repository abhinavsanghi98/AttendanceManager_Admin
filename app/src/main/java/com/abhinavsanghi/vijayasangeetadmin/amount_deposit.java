package com.abhinavsanghi.vijayasangeetadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class amount_deposit extends AppCompatActivity {
    String s;
    String s1;
    String s2;
    String s3;
    String tid;
    String updateddeposit;
    private EditText depositing;
    Button submit;
    TextView name;
    TextView prevdeposit;
    ProgressDialog mdialog;
    DatabaseReference student;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_deposit);
        Intent intent=getIntent();
        s=intent.getStringExtra("sid");
        s1=intent.getStringExtra("subid");
        tid=intent.getStringExtra("tid");
        mdialog=new ProgressDialog(this);

        student= FirebaseDatabase.getInstance().getReference("Student").child(s);
        name=findViewById(R.id.fac_profile_name_et);
        // depositing=findViewById(R.id.deposit_amount);
        prevdeposit=findViewById(R.id.fac_profile_desig_et);
        depositing=findViewById(R.id.deposit_amount);

        submit=findViewById(R.id.upload_pic_btn);
        student.keepSynced(true);

        student.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("sname").getValue(String.class));
                prevdeposit.setText("Previous Deposit:  "+dataSnapshot.child("due").getValue(String.class));
                s3=dataSnapshot.child("due").getValue(String.class);
                //s2=depositing.getText().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        student.keepSynced(true);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s2=depositing.getText().toString();

                if(s2.isEmpty()){

                    depositing.setError("Enter amount");
                    return;
                }
                else {
                    int a=Integer.valueOf(s3);
                    int b=Integer.valueOf(s2);
                    a=a+b;
                    updateddeposit=String.valueOf(a);
                    student.child("due").setValue(updateddeposit).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                finish();
                                Intent intent2=new Intent(getApplicationContext(),view_student_inadmin.class);
                                intent2.putExtra("subid",s1);
                                intent2.putExtra("tid",tid);
                                startActivity(intent2);

                            }
                            else{

                            }
                        }
                    });

                }

            }
        });



    }
}
