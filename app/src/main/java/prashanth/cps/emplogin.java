package prashanth.cps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class emplogin extends AppCompatActivity {
private EditText loginid;
private Button login,login1;
DatabaseReference mDatabase;
private String result1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_emplogin);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("empid");
        loginid=(EditText) findViewById(R.id.edt1);
        login=(Button) findViewById(R.id.btn1);
        login1=(Button) findViewById(R.id.btn2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String loginid1=loginid.getText().toString();

                if(!loginid1.isEmpty() ) {

                    final boolean[] isSuccess = {false};
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String id = dataSnapshot1.getKey();
                                // Toast.makeText(emplogin.this, ""+id, Toast.LENGTH_SHORT).show();

                                if (loginid1.equals(dataSnapshot.child(id).getValue().toString())) {
                                    isSuccess[0] = true;
                                    Toast.makeText(emplogin.this, "Success ", Toast.LENGTH_SHORT).show();


                                }
                            }


                            if (isSuccess[0]) {
                                Intent intent = new Intent(getApplicationContext(), EMPCONNECT.class);

                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(emplogin.this, "Failed ", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    Toast.makeText(emplogin.this, "Enter a valid ID ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = new IntentIntegrator(emplogin.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();




                   /* final boolean[] isSuccess = {false};
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String id = dataSnapshot1.getKey();
                                // Toast.makeText(emplogin.this, ""+id, Toast.LENGTH_SHORT).show();

                                if (result1.equals(dataSnapshot.child(id).getValue().toString())) {
                                    isSuccess[0] = true;
                                    Toast.makeText(emplogin.this, "Success ", Toast.LENGTH_SHORT).show();


                                }
                            }


                            if (isSuccess[0]) {
                                Intent intent = new Intent(getApplicationContext(), connect.class);

                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(emplogin.this, "Failed ", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
*/
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
              //  Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                result1=result.getContents();
                Toast.makeText(this, ""+result1, Toast.LENGTH_LONG).show();
                final boolean[] isSuccess = {false};
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String id = dataSnapshot1.getKey();
                            // Toast.makeText(emplogin.this, ""+id, Toast.LENGTH_SHORT).show();

                            if (result1.equals(dataSnapshot.child(id).getValue().toString())) {
                                isSuccess[0] = true;
                                Toast.makeText(emplogin.this, "Success ", Toast.LENGTH_SHORT).show();


                            }
                        }


                        if (isSuccess[0]) {
                            Intent intent = new Intent(emplogin.this, EMPCONNECT.class);

                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(emplogin.this, "Failed ", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



}
