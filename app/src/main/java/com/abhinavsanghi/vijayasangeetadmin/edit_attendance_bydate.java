package com.abhinavsanghi.vijayasangeetadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class edit_attendance_bydate extends AppCompatActivity {
    String subject, date,tid;

    TextView subjectname;
    TextView datetv;
    ListView listViewbydate;
    ArrayList attendance = new ArrayList<>();
    DatabaseReference attendancerecord;
    ArrayList<String> selectedItems;
    ArrayList<String> nonselectedItems;
    boolean internetsts;
    Button deletebutton;
    private ArrayAdapter adapter;

    ProgressDialog mDialog;

    String mrollno;

    int count = 0;
    int count1=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_viewbydate_edit);

        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Loading...");
        mDialog.setMessage("Please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        attendance.clear();//clearning arraylist

        listViewbydate = (ListView) findViewById(R.id.list_view_bd);
        subjectname = findViewById(R.id.subject);
        datetv = findViewById(R.id.date_tv);

        Intent intent = getIntent();
        subject = intent.getStringExtra("subid");
        date = intent.getStringExtra("date");
        tid=intent.getStringExtra("tid");
        subjectname.setText(subject);
        datetv.setText("Date/Time:" + date);

        //Toast.makeText(getApplicationContext(),subject+date,Toast.LENGTH_SHORT).show();

        attendancerecord = FirebaseDatabase.getInstance().getReference("AttendanceRecord");
        attendancerecord.keepSynced(true);
        deletebutton = findViewById(R.id.delatten_btn);
        selectedItems = new ArrayList<String>();

        //attendance.add("Enrollment No" + "            " + "Attendance Value");
        attendancerecord.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dspp : dataSnapshot.child(subject).child(date).getChildren()) {
                    String rollnumber, avalue,temp9;
                    rollnumber = dspp.getKey();
                    temp9=dspp.child("atvalue").getValue(String.class).substring(0,1);
                    if(temp9.equals("0")) {
                        avalue = dspp.child("atvalue").getValue(String.class);

                        attendance.add(rollnumber + "                             " + avalue);

                        count = count + 1;
                    }
                }
                mDialog.dismiss();
                onStart1(attendance);
//                attendance.add("Total Submission=  "+count);
//                listshow(attendance);//this is a function created by me
//                mDialog.dismiss();
//                count = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                internetsts = isNetworkAvailable();

                if (internetsts != true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(edit_attendance_bydate.this);
                    builder.setMessage("Internet is not available");
                    builder.setTitle("Alert !");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                } else {
                    assurance();
                }

            }
        });


    }

    public void onStart1(ArrayList<String> attendance) {
        nonselectedItems = attendance;
        final ListView ch1 = findViewById(R.id.list_view_bd);
        ch1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        ArrayAdapter<String> sidarrayadapter = new ArrayAdapter<String>(this, R.layout.checkable_list_layout, R.id.checkboxt, attendance);
        ch1.setAdapter(sidarrayadapter);

        ch1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItems.contains(selectedItem))
                    selectedItems.remove(selectedItem);
                else
                    selectedItems.add(selectedItem);

            }

        });

        ch1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                updateattendence(selectedItem);
                //Toast.makeText(getApplicationContext(),selectedItem,Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    public void listshow(ArrayList attendancelist) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, attendancelist);
        listViewbydate.setAdapter(adapter);

        listViewbydate.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String dataupatevar = parent.getItemAtPosition(position).toString();
                updateattendence(dataupatevar);
                return false;
            }
        });
    }

    public void updateattendence(String dataupdate) {

        mrollno = dataupdate.substring(0, 11);
        mrollno = mrollno.trim();
        String atvalue = dataupdate.substring(14);
        atvalue = atvalue.trim();
        String atvaltrim = atvalue.substring(0, 1);
        final String atvaluemax = atvalue.substring(2, 3);


        final AlertDialog.Builder myDialog = new AlertDialog.Builder(edit_attendance_bydate.this);
        LayoutInflater inflater = LayoutInflater.from(edit_attendance_bydate.this);
        View mview = inflater.inflate(R.layout.upate_attandance, null);
        final AlertDialog dialog = myDialog.create();
        dialog.setView(mview);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final TextView enrollmentno = mview.findViewById(R.id.rollno_update_tv);
        final TextView datetime = mview.findViewById(R.id.date_update_tv);
        final TextView subjecttv = mview.findViewById(R.id.subject_update_tv);
        final EditText atstatus = mview.findViewById(R.id.attendance_vale_update);
        Button updatebtn = mview.findViewById(R.id.update_btn);
        Button cancelbtn = mview.findViewById(R.id.cancel_btn);
        Button deleteit = mview.findViewById(R.id.delbutton);

        enrollmentno.setText(mrollno.trim());
        datetime.setText(date);
        subjecttv.setText(subject);
        atstatus.setText(atvaltrim);

        cancelbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String getatvalue = atstatus.getText().toString();
                String updates;
                updates = (getatvalue + "/" + atvaluemax);
                if (getatvalue.isEmpty()) {
                    atstatus.setError("Can't leave Empty");
                    return;
                } else if ((Integer.valueOf(getatvalue)) > (Integer.valueOf(atvaluemax))) {
                    Toast.makeText(getApplicationContext(), "Value can't be more than " + atvaluemax, Toast.LENGTH_SHORT).show();
                } else {

                    mDialog.show();
                    dialog.dismiss();
                    updatte(updates);
                }

            }
        });
        deleteit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.show();
                        dialog.dismiss();
                        updatte1();

                    }
                });


    }

    public void updatte(String atval) {

        attendancerecord.child(subject).child(date).child(mrollno).child("atvalue").setValue(atval).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mDialog.dismiss();
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void updatte1() {

        attendancerecord.child(subject).child(date).child(mrollno).child("atvalue").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mDialog.dismiss();
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void assurance() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation!");
        builder.setMessage("Do you want to Submit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startuploadattendence1();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();  // Show the Alert Dialog box

    }

    public void startuploadattendence1() {

        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Submitting Attendance");
        mDialog.setMessage("Please wait..");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        String temp3;

        for (String item : selectedItems) {
            nonselectedItems.remove(item);
            temp3=item.substring(0,11);
            temp3=temp3.trim();
            //Attendance attendance=new Attendance((attenvalue+"/"+attenvalue));
            attendancerecord.child(subject).child(date).child(temp3).setValue(null);
            count1=count1+1;

        }
        for(String item :nonselectedItems){
            //Attendance attendance=new Attendance(("0/"+attenvalue));
            //a.child(subid).child(timestamp).child(item).setValue(attendance);
            count1=count1+1;
        }


        mDialog.dismiss();
        if(count==count1){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Submitted Successfully!");
            //updateprice();
            //builder.setMessage("Present="+present+"  Absent="+count1+" Total Student="+count2);
            builder.setCancelable(false);

            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    //startActivity(new Intent(getApplicationContext(),facultydashboard.class));
                    Intent intent3=new Intent(getApplicationContext(),facultydashboard.class);
                    intent3.putExtra("tid",tid);
                    startActivity(intent3);
                    dialog.cancel();
                    finish();


                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            count1=0;
            count=0;

        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Submission Failed");
            builder.setMessage("Something went wrong !");
            builder.setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }



}
