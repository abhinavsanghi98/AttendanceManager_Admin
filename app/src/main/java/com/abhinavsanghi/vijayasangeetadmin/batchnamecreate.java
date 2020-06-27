package com.abhinavsanghi.vijayasangeetadmin;

import android.app.ProgressDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class batchnamecreate extends AppCompatActivity {


    String batchnamestr;
    String batchnameprice;
    private EditText batchname;
    private EditText batchprice;
    private Button adbatchnamebtn;
    private DatabaseReference dbbatchname;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batchnamecreate);

        mDialog=new ProgressDialog(this);

        batchname=findViewById(R.id.batchname_et);
        batchprice=findViewById(R.id.batchname_price);
        adbatchnamebtn=findViewById(R.id.adbatchname_btn);


        dbbatchname= FirebaseDatabase.getInstance().getReference("BatchName");

        adbatchnamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                batchnamestr=batchname.getText().toString().toUpperCase().trim();
                batchnameprice=batchprice.getText().toString();

                if (batchnamestr.isEmpty()){
                    batchname.setError("Enter Valid Name");
                    return;
                }
                else if (batchnamestr.length()>10){
                    batchname.setError("Name should not greater than 10 char");
                    return;
                }
                else if(batchnameprice.isEmpty()){
                    batchprice.setError("enter price");
                }
                else{
                    mDialog.setTitle("Adding..\n"+batchnamestr);
                    mDialog.setMessage("Please Wait..");
                    mDialog.show();
                    mDialog.setCanceledOnTouchOutside(false);
                    dbbatchname.child(batchnamestr).child("batch").setValue(batchnamestr).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dbbatchname.child(batchnamestr).child("price").setValue(batchnameprice);
                            dbbatchname.child(batchnamestr).child("amount").setValue(0);
                            if (task.isSuccessful()){
                                mDialog.dismiss();
                                finish();
                                Toast.makeText(getApplicationContext(),"Added Succesfully",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Failed..",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
