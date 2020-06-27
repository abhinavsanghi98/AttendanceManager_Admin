package com.abhinavsanghi.vijayasangeetadmin;

import android.app.ProgressDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class removestudent extends AppCompatActivity{

    String item_batchname;
    EditText Sid;
    String sid;
    Button delstdbtn;
    DatabaseReference databaseStudent;
    DatabaseReference batchdetails;
    DatabaseReference dbbatchname;
    DatabaseReference atten;
    //DatabaseReference stusubd;
    ProgressDialog mDialog;
    String dbp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removestudent);


        Sid =  findViewById(R.id.del_stdenrollno_et);
        delstdbtn=findViewById(R.id.std_delete_btn);

        mDialog=new ProgressDialog(this);

        databaseStudent = FirebaseDatabase.getInstance().getReference("Student");
        dbbatchname=FirebaseDatabase.getInstance().getReference("BatchName");
        //will be used later while updating things currently unused
        batchdetails=FirebaseDatabase.getInstance().getReference("Batchdetails");
        //stusubd=FirebaseDatabase.getInstance().getReference("StudentSubDeatails");
        atten=FirebaseDatabase.getInstance().getReference("AttendanceRecord");


        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        //Spinner for batchname
        final List<String> lstbacthn=new ArrayList<String>();
        lstbacthn.add("Select Batch");

        ArrayAdapter<String> facultyarrayadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lstbacthn);
        facultyarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(facultyarrayadapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_batchname=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dbbatchname.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp :dataSnapshot.getChildren()){
                    String name;
                    name=dsp.getKey();
                    lstbacthn.add(name);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        delstdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removestd();
            }
        });
    }


    public void removestd(){

        sid = Sid.getText().toString().trim();

        if(sid.isEmpty()){
            Sid.setError("Enter Enrollment Number");
            return;
        }
        else if(item_batchname=="Select Batch")
        {
            Toast.makeText(getApplicationContext(),"Please Select Batch",Toast.LENGTH_SHORT).show();
            return;
        }

        mDialog.setTitle("Deleting Account");
        mDialog.setMessage("Please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        verifyusercredential();


    }
    public  void verifyusercredential(){
        databaseStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    dbp = dataSnapshot.child(sid).child("sid").getValue(String.class);
                    verify(dbp);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void verify(String dbp){
        if(! sid.equals(dbp)){
            Toast.makeText(getApplicationContext(),"No such student exists", Toast.LENGTH_LONG).show();
            mDialog.dismiss();

        }
        else{
            databaseStudent.child(sid).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        batchdetails.child(item_batchname).child(sid).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    atten.child(item_batchname).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                   @Override
                                                                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                       for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                                                                                           String avail;
                                                                                                           avail = dsp.getKey();
                                                                                                           atten.child(item_batchname).child(avail).child(sid).setValue(null);
                                                                                                       }
                                                                                                       mDialog.dismiss();
                                                                                                       finish();
                                                                                                       Toast.makeText(getApplicationContext(),"Deleted Successfully..",Toast.LENGTH_SHORT).show();
                                                                                                   }





                                                                                                   @Override
                                                                                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                   }



                                                                                               }



                                    );

                                }
                            }

                        });

                    }
                    else {
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Oops...Failed.",Toast.LENGTH_SHORT).show();

                    }
                }

            });
        }

    }
}


