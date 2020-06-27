package com.abhinavsanghi.vijayasangeetadmin;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class view_student_inadmin extends AppCompatActivity {

    String subid1;
    String subid;
    String s2;
    int tpresent = 0;
    int tattencount = 0;
    //SearchView sv;
    String getdeposit;
    //String perclassrate;
    int perclassrateint;
    int a;

    Toolbar hometoolbar;

    DatabaseReference stuensbdetails;
    DatabaseReference attenrecord;
    DatabaseReference foramount;
    DatabaseReference student;
    ProgressDialog mDialog;
    String tid;

    //variable deceleration
    ArrayList<Details> data = new ArrayList<>();
    RecyclerView recyclerView;
    RAdapter adapter;
    //ArrayAdapter<Details> adapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_attenview_mid2);


        Intent intent = getIntent();
        subid = intent.getStringExtra("subid");
        tid=intent.getStringExtra("tid");
        subid1 = "Students";
        //sv=findViewById(R.id.searchView);

        hometoolbar=findViewById(R.id.view_attencollec_toolbar);
        setSupportActionBar(hometoolbar);
        getSupportActionBar().setTitle(subid);



        //implementation of recycler view

        recyclerView = findViewById(R.id.recycler_collective_attendance_view);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RAdapter(data, getApplicationContext());

        stuensbdetails = FirebaseDatabase.getInstance().getReference("Batchdetails");
        attenrecord = FirebaseDatabase.getInstance().getReference("AttendanceRecord");
        foramount=FirebaseDatabase.getInstance().getReference("BatchName").child(subid);
        student=FirebaseDatabase.getInstance().getReference("Student");
        foramount.keepSynced(true);
        foramount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String perclassrate=dataSnapshot.child("price").getValue(String.class);
                perclassrateint=Integer.valueOf(perclassrate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        stuensbdetails.keepSynced(true);
        attenrecord.keepSynced(true);

        recyclerView.setAdapter(adapter);


        //ending of recycler view


        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Please Wait");
        mDialog.setMessage("Data is loading...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();




        //start quaring with student step 1 getting name and sid from here
        stuensbdetails.child(subid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String id, name;
                    id = dsp.getKey();
                    name = dsp.child("sname").getValue(String.class);
                    //subid= dsp.child("batch").getValue(String.class);
                    gotoattendencedatabase(id, name,subid);
                    adapter.notifyDataSetChanged();


                }
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //adapter1=new ArrayAdapter<>(this,R.layout.fac_view_attendance_item_data_mid2,data);

//        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                adapter1.getFilter().filter(s);
//                return false;
//            }
//        });



    }

    public void gotoattendencedatabase(final String id, final String name,final String subid) {

        attenrecord.child(subid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String aval;
                    //String absent=subid;

//                    aval1=dsp.child(id).getKey();
//                    attenrecord.child(aval1).removeValue();
                    aval = dsp.child(id).child("atvalue").getValue(String.class);
                    //Toast.makeText(getApplicationContext(),name+"--"+aval,Toast.LENGTH_SHORT).show();
                    try {
                        tpresent += Integer.valueOf(aval.substring(0, 1));
                        tattencount += Integer.valueOf(aval.substring(2, 3));
                    }
                    catch (Exception e){
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(fac_attenview_mid2.this);
//                        builder.setTitle("Alert !");
//                        builder.setMessage("Something went wrong please contact Admin");
//                        builder.setCancelable(false);
//                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//
//                                dialog.cancel();
//                            }
//                        });
//                        AlertDialog alertDialog = builder.create();
//                        alertDialog.show();

                    }


                }
                //int abs = tattencount - tpresent;
                //String abs=ava
                float percent;
                percent = (((float) tpresent) / ((float) tattencount)) * 100;
                percent=Math.round(percent*1000)/1000;

                String attendence=(String.valueOf(tpresent)+"/"+String.valueOf(tattencount)) ;
                String absent=subid;
                String percentage=String.valueOf(percent);
                String due=String.valueOf(tattencount*perclassrateint);

                Details details=new Details(id,name,attendence,absent,percentage,due);
                data.add(details);
                adapter.notifyDataSetChanged();
                tpresent = 0;
                tattencount = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    class RAdapter extends RecyclerView.Adapter<RAdapter.RViewHolder>{
        ArrayList<Details> details;
        Context context;
        @NonNull
        @Override
        public RViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fac_view_attendance_item_data_mid2, viewGroup, false);
            RViewHolder holder = new RViewHolder(v);
            return  holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RViewHolder v, final int i) {
            View view = v.itemView;
            String percent = details.get(i).percent;
            //String finaldue=details.get(i).getDue();
            //a=Integer.valueOf(finaldue);
            //String getdeposit1;
            //findgetdeposit();
   student.keepSynced(true);
            student.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String getdeposit1=dataSnapshot.child(details.get(i).enrollment).child("due").getValue(String.class);
                    int b;
                    if(getdeposit1!=null){
                        b=Integer.valueOf(getdeposit1);}
                    else{
                        b=0;
                    }
                    a=Integer.valueOf(details.get(i).getDue());
                    a=a-b;
                    String finaldue=String.valueOf(a);
                    v.due.setText(finaldue);
                    //please(getdeposit1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            student.keepSynced(true);
//            b=Integer.valueOf(getdeposit);
//            a=a-b;
//            String temp=String.valueOf(a);
            if(Float.valueOf(percent)<=75){
                v.percenttv.setTextColor(Color.RED);
            }
            else if(Float.valueOf(percent)>75 && Float.valueOf(percent)<85 ){
                v.percenttv.setTextColor(Color.BLUE);
            }
            else if(Float.valueOf(percent)>=85){
                v.percenttv.setTextColor(Color.MAGENTA);
            }
            v.enrollmenttv.setText(details.get(i).getEnrollment());
            //s2=details.get(i).getEnrollment();
            v.nametv.setText(details.get(i).getName());
            //v.absenttv.setText(details.get(i).getAbsent());
            v.percenttv.setText(percent);
            v.attendancetv.setText(details.get(i).getAttendance());
            // v.due.setText(details.get(i).getDue());
            // v.due.setText(temp);
            v.receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),amount_deposit.class);
                    intent.putExtra("sid",details.get(i).enrollment);
                    intent.putExtra("subid",subid);
                    intent.putExtra("tid",tid);
                    startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return details.size();
        }

        RAdapter(ArrayList<Details> details, Context context){
            this.details = details;
            this.context  = context;
        }
        class RViewHolder extends RecyclerView.ViewHolder{
            TextView enrollmenttv;
            TextView nametv;
            TextView attendancetv;
            TextView absenttv;
            TextView percenttv;
            TextView due;
            Button receive;
            public RViewHolder(@NonNull View v) {
                super(v);
                enrollmenttv = v.findViewById(R.id.enno_input_tv);
                nametv = v.findViewById(R.id.name_input_tv);
                attendancetv = v.findViewById(R.id.present_input_tv);
                //absenttv = v.findViewById(R.id.absent_input_tv);
                percenttv = v.findViewById(R.id.percentage_input_tv);
                due=v.findViewById(R.id.due_input_tv);
                receive=v.findViewById(R.id.happy1);


            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
       // Intent intent4=new Intent(getApplicationContext(),view_student_inadmin_mid.class);
        Intent intent4=new Intent(getApplicationContext(),view_student_inadmin_mid.class);
        intent4.putExtra("tid",tid);
        startActivity(intent4);
        //finish();
    }


}
