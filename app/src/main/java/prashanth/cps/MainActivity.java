package prashanth.cps;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.security.spec.KeySpec;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class MainActivity extends AppCompatActivity  {

    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,mbtn,mmbtn;
    EditText mtxt;
    int flag1=0,flag2=0,flag3=0,flag4=0,flag5=0,flag6=0,flag7=0,db1=0,db2=0,db3=0,db4=0,db5=0,db6=0;
    String empid;

    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private static KeySpec myKeySpec;
    private static SecretKeyFactory mySecretKeyFactory;
    private static Cipher cipher;
    public static byte[] keyAsBytes;
    private static String myEncryptionKey;
    private static String myEncryptionScheme;
    public static SecretKey key;
    DatabaseReference maindb,sh1db,sh2db,lddb,cmd1db,cmd2db,db;
    public static String main;
    private    String m,s1,s2,l,c1,c2;
    public boolean[] isSuccess = {false};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent newint = getIntent();
        address = newint.getStringExtra(connect.EXTRA_ADDRESS);
        setContentView(R.layout.activity_main);
        new ConnectBT().execute();

        mtxt=(EditText)findViewById(R.id.edttxtm);
        btn1=(Button)findViewById(R.id.btn1);
        btn2=(Button)findViewById(R.id.btn2);
        btn3=(Button)findViewById(R.id.btn3);
        btn4=(Button)findViewById(R.id.btn4);
        btn5=(Button)findViewById(R.id.btn5);
        btn6=(Button)findViewById(R.id.btn6);
        btn7=(Button)findViewById(R.id.btn7);
        mbtn=(Button)findViewById(R.id.mbtn);
        mmbtn=(Button)findViewById(R.id.mmbtn);

        btn1.setEnabled(false);
        btn3.setEnabled(false);
        btn4.setEnabled(false);
        btn5.setEnabled(false);
        btn6.setEnabled(false);
        btn7.setEnabled(false);

        maindb= FirebaseDatabase.getInstance().getReference().child("Main");
        sh1db= FirebaseDatabase.getInstance().getReference().child("shaft1");
        sh2db= FirebaseDatabase.getInstance().getReference().child("shaft2");
        lddb= FirebaseDatabase.getInstance().getReference().child("linehead");
        cmd1db= FirebaseDatabase.getInstance().getReference().child("cmd1");
        cmd2db= FirebaseDatabase.getInstance().getReference().child("cmd2");
        db= FirebaseDatabase.getInstance().getReference().child("db");

        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setEnabled(false);
                btn3.setEnabled(false);
                btn4.setEnabled(false);
                btn5.setEnabled(false);
                btn6.setEnabled(false);
                btn7.setEnabled(false);
                String auth=mtxt.getText().toString();
                if(auth!=null) {
                    String ID1 = findID(auth);
                    // Character one = new Character("1");
                    Character first = new Character(ID1.charAt(0));
                    Toast.makeText(MainActivity.this, " EMP " + first, Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, " NAME: " + ID1.substring(1), Toast.LENGTH_SHORT).show();
                    if (first.equals('1')) {

                        btn1.setEnabled(true);
                        btn3.setEnabled(true);
                        btn4.setEnabled(true);
                        btn5.setEnabled(true);
                        btn6.setEnabled(true);
                        btn7.setEnabled(true);


                    } else if (first.equals('2')) {
                        btn3.setEnabled(true);
                        btn4.setEnabled(true);
                        btn5.setEnabled(true);
                        btn6.setEnabled(true);
                        btn7.setEnabled(true);

                    } else if (first.equals('3')) {
                        btn5.setEnabled(true);
                        btn6.setEnabled(true);

                    } else {
                        Toast.makeText(MainActivity.this, "INAVLID EMP ", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(MainActivity.this, "Enter a ID ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setEnabled(false);
                btn3.setEnabled(false);
                btn4.setEnabled(false);
                btn5.setEnabled(false);
                btn6.setEnabled(false);
                btn7.setEnabled(false);
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });




        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {


                    if(flag1==0){
                        btSocket.getOutputStream().write("-o1".toString().getBytes());
                       // maindb.setValue("ON");
                        flag1++;
                    }else {
                        flag1--;
                        btSocket.getOutputStream().write("-n1".toString().getBytes());
                       // maindb.setValue("OFF");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

          }
        });



       btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (flag3 == 0) {
                        btSocket.getOutputStream().write("-n3".toString().getBytes());
                        flag3++;
                        //sh1db.setValue("ON");
                    } else {
                        flag3--;
                        btSocket.getOutputStream().write("-o3".toString().getBytes());
                        //sh1db.setValue("OFF");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if(flag4==0){
                        btSocket.getOutputStream().write("-n4".toString().getBytes());
                        flag4++;
                        //sh2db.setValue("ON");
                    }else {
                        flag4--;
                        btSocket.getOutputStream().write("-o4".toString().getBytes());
                        //sh2db.setValue("OFF");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if(flag5==0){
                        btSocket.getOutputStream().write("-n5".toString().getBytes());
                        flag5++;
                       // cmd1db.setValue("ON");
                    }else {
                        flag5--;
                        btSocket.getOutputStream().write("-o5".toString().getBytes());
                       // cmd1db.setValue("OFF");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if(flag6==0){
                        btSocket.getOutputStream().write("-n6".toString().getBytes());
                        flag6++;
                       // cmd2db.setValue("ON");
                    }else {
                        flag6--;
                        btSocket.getOutputStream().write("-aa".toString().getBytes());
                       // cmd2db.setValue("OFF");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               try {

                    if(flag7==0){
                        btSocket.getOutputStream().write("-n7".toString().getBytes());
                        flag7++;
                        //lddb.setValue("ON");
                    }else {
                        flag7--;
                        btSocket.getOutputStream().write("-o7".toString().getBytes());
                        //lddb.setValue("OFF");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }



    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!!!");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected.");
                isSuccess[0]= true;
                db.setValue("OFF");
                isBtConnected = true;
                if (btSocket!=null) {
                    try {
                        btSocket.getOutputStream().write("-CON".toString().getBytes());
                    }
                    catch (IOException e) {
                        msg("Unable to send data!");
                    }
                }
            }
            progress.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {

        } else if (id == R.id.exit) {
            if (btSocket!=null) {
                try {
                    btSocket.getOutputStream().write("-DIS".toString().getBytes());
                }
                catch (IOException e) {
                    msg("Unable to send data!");
                }
            }
            if (btSocket!=null) {
                try {
                    btSocket.close();
                } catch (IOException e) {
                    msg("Error");
                }
            }
            finish();
        }
        return true;
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }



    public static String findID(String encryptedString) {
        String decryptedText=null;
        try {
            myEncryptionKey = "ThisIsSecretEncryptionKey";
            myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
            keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
            myKeySpec = new DESedeKeySpec(keyAsBytes);
            mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
            cipher = Cipher.getInstance(myEncryptionScheme);
            key = mySecretKeyFactory.generateSecret(myKeySpec);

            cipher.init(Cipher.DECRYPT_MODE, key);


            //         BASE64Decoder base64decoder = new BASE64Decoder();
            //         byte[] encryptedText = base64decoder.decodeBuffer(encryptedString);

            byte[] encryptedText= android.util.Base64.decode(encryptedString, android.util.Base64.DEFAULT);


            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= bytes2String(plainText);




        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    private static String bytes2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                String ID1 = findID(result.getContents());
                // Character one = new Character("1");
                Character first = new Character(ID1.charAt(0));
                Toast.makeText(MainActivity.this, " EMP " + first, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, " NAME: " + ID1.substring(1), Toast.LENGTH_SHORT).show();
                if (first.equals('1')) {

                    btn1.setEnabled(true);
                    btn3.setEnabled(true);
                    btn4.setEnabled(true);
                    btn5.setEnabled(true);
                    btn6.setEnabled(true);
                    btn7.setEnabled(true);




                } else if (first.equals('2')) {
                    btn3.setEnabled(true);
                    btn4.setEnabled(true);
                    btn5.setEnabled(true);
                    btn6.setEnabled(true);
                    btn7.setEnabled(true);

                } else if (first.equals('3')) {
                    btn5.setEnabled(true);
                    btn6.setEnabled(true);

                } else {
                    Toast.makeText(MainActivity.this, "INAVLID EMP ", Toast.LENGTH_SHORT).show();

                }
            }
        }


        else {
            super.onActivityResult(requestCode, resultCode, data);
        }



    }


        @Override
        protected void onStart () {
        super.onStart();
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   String db = dataSnapshot.getValue(String.class);
                    if(isSuccess[0]) {

                        if (db.equals("OFF")) {
                        db1=0;
                        } else if (db.equals("ON")) {
                            db1=1;
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        maindb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                main = dataSnapshot.getValue(String.class);

                if(isSuccess[0]) {
                    if (db1 == 1) {

                        if (main.equals("OFF")) {
                            Toast.makeText(MainActivity.this, "DB Main -" + main, Toast.LENGTH_SHORT).show();
                            btn1.performClick();


                        } else if (main.equals("ON")) {
                            Toast.makeText(MainActivity.this, "DB Main -" + main, Toast.LENGTH_SHORT).show();
                            btn1.performClick();


                        }
                        db1=0;
                        db.setValue("OFF");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sh1db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sh1 = dataSnapshot.getValue(String.class);
                if(isSuccess[0]) {
                    if(db1==1) {
                        if (sh1.equals("ON")) {
                            Toast.makeText(MainActivity.this, "DB SHAFT I -" + sh1, Toast.LENGTH_SHORT).show();
                            btn3.performClick();


                        } else if (sh1.equals("OFF")) {
                            Toast.makeText(MainActivity.this, "DB SHAFT I -" + sh1, Toast.LENGTH_SHORT).show();
                            btn3.performClick();


                        }
                        db1=0;
                        db.setValue("OFF");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sh2db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sh2 = dataSnapshot.getValue(String.class);
                if(isSuccess[0]) {
                    if (db1 == 1) {
                        if (sh2.equals("ON")) {
                            Toast.makeText(MainActivity.this, "SHAFT II -" + sh2, Toast.LENGTH_SHORT).show();
                            btn4.performClick();


                        } else if (sh2.equals("OFF")) {
                            Toast.makeText(MainActivity.this, "SHAFT II -" + sh2, Toast.LENGTH_SHORT).show();
                            btn4.performClick();

                        }
                        db1=0;
                        db.setValue("OFF");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lddb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ld = dataSnapshot.getValue(String.class);
                if(isSuccess[0]) {
                    if (db1 == 1) {
                        if (ld.equals("ON")) {
                            Toast.makeText(MainActivity.this, "LINE HEAD -" + ld, Toast.LENGTH_SHORT).show();
                            btn7.performClick();


                        } else if (ld.equals("OFF")) {
                            Toast.makeText(MainActivity.this, "LINE HEAD -" + ld, Toast.LENGTH_SHORT).show();
                            btn7.performClick();

                        }
                        db1=0;
                        db.setValue("OFF");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        cmd1db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String cm1 = dataSnapshot.getValue(String.class);
                if(isSuccess[0]) {
                    if (db1 == 1) {
                        if (cm1.equals("ON")) {
                            Toast.makeText(MainActivity.this, "CONTROL I -" + cm1, Toast.LENGTH_SHORT).show();
                            btn5.performClick();


                        } else if (cm1.equals("OFF")) {
                            Toast.makeText(MainActivity.this, "CONTROL I -" + cm1, Toast.LENGTH_SHORT).show();
                            btn5.performClick();

                        }
                        db1=0;
                        db.setValue("OFF");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        cmd2db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String cm2 = dataSnapshot.getValue(String.class);

                if(isSuccess[0]) {
                    if (db1 == 1) {
                        if (cm2.equals("ON")) {
                            Toast.makeText(MainActivity.this, "CONTROL II -" + cm2, Toast.LENGTH_SHORT).show();
                            btn6.performClick();


                        } else if (cm2.equals("OFF")) {
                            Toast.makeText(MainActivity.this, "CONTROL II -" + cm2, Toast.LENGTH_SHORT).show();
                            btn6.performClick();

                        }
                        db1=0;
                        db.setValue("OFF");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
