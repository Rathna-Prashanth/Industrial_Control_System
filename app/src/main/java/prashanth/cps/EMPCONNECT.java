package prashanth.cps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class EMPCONNECT extends AppCompatActivity {
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
    public Character first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_empconnect);

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
                    Toast.makeText(EMPCONNECT.this, " EMP " + first, Toast.LENGTH_SHORT).show();
                    Toast.makeText(EMPCONNECT.this, " NAME: " + ID1.substring(1), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EMPCONNECT.this, "INAVLID EMP ", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(EMPCONNECT.this, "Enter a ID ", Toast.LENGTH_SHORT).show();
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
                IntentIntegrator integrator = new IntentIntegrator(EMPCONNECT.this);
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




                    if(flag1==0){
                        db.setValue("ON");
                         maindb.setValue("ON");
                        Toast.makeText(EMPCONNECT.this, "MAIN ON ", Toast.LENGTH_SHORT).show();
                        flag1++;
                    }else {
                        flag1--;
                        db.setValue("ON");
                         maindb.setValue("OFF");
                        Toast.makeText(EMPCONNECT.this, "MAIN OFF ", Toast.LENGTH_SHORT).show();


                    }



            }
        });



        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

         //if(isSuccess[1]) {

             if (flag3 == 0) {

                 flag3++;
                 db.setValue("ON");
                 sh1db.setValue("ON");
                 Toast.makeText(EMPCONNECT.this, "SHAFT I ON ", Toast.LENGTH_SHORT).show();


             } else {
                 flag3--;
                 db.setValue("ON");
                 sh1db.setValue("OFF");
                 Toast.makeText(EMPCONNECT.this, "SHAFT I OFF ", Toast.LENGTH_SHORT).show();


             }


       //  }else {
       //      Toast.makeText(EMPCONNECT.this, "!!!!! MAIN OFF !!!!!!! ", Toast.LENGTH_SHORT).show();
        // }


            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if(isSuccess[1]) {

                    if(flag4==0){

                        flag4++;
                        db.setValue("ON");
                        sh2db.setValue("ON");
                        Toast.makeText(EMPCONNECT.this, "SHAFT II ON ", Toast.LENGTH_SHORT).show();


                    }else {
                        flag4--;
                        db.setValue("ON");
                        sh2db.setValue("OFF");
                        Toast.makeText(EMPCONNECT.this, "SHAFT II OFF ", Toast.LENGTH_SHORT).show();


                    }
               // }else {
               //     Toast.makeText(EMPCONNECT.this, "!!!!! MAIN OFF !!!!!!! ", Toast.LENGTH_SHORT).show();
                //}

            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               // if (isSuccess[1]) {
                    if (flag5 == 0) {
                        flag5++;
                        db.setValue("ON");
                        cmd1db.setValue("ON");
                        Toast.makeText(EMPCONNECT.this, "CMD I ON ", Toast.LENGTH_SHORT).show();


                    } else {
                        flag5--;
                        db.setValue("ON");
                        cmd1db.setValue("OFF");
                        Toast.makeText(EMPCONNECT.this, "CMD I OFF ", Toast.LENGTH_SHORT).show();


                    }


             //   } else {
             //       Toast.makeText(EMPCONNECT.this, "!!!!! MAIN OFF !!!!!!! ", Toast.LENGTH_SHORT).show();
             //   }
            }

        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


         //   if(isSuccess[1]){
                    if(flag6==0){

                        flag6++;
                        db.setValue("ON");
                        cmd2db.setValue("ON");
                        Toast.makeText(EMPCONNECT.this, "CMD II ON ", Toast.LENGTH_SHORT).show();


                    }else {
                        flag6--;
                        db.setValue("ON");
                        cmd2db.setValue("OFF");
                        Toast.makeText(EMPCONNECT.this, "CMD II OFF ", Toast.LENGTH_SHORT).show();


                    }

         //   }else {
         //       Toast.makeText(EMPCONNECT.this, "!!!!! MAIN OFF !!!!!!! ", Toast.LENGTH_SHORT).show();
         //   }

            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


      //  if(isSuccess[1]){
                    if(flag7==0){

                        flag7++;
                        db.setValue("ON");
                        lddb.setValue("ON");
                        Toast.makeText(EMPCONNECT.this, "LINE HEAD ON ", Toast.LENGTH_SHORT).show();


                    }else {
                        flag7--;
                        db.setValue("ON");
                        lddb.setValue("OFF");
                        Toast.makeText(EMPCONNECT.this, "LINE HEAD OFF ", Toast.LENGTH_SHORT).show();


                    }
      //  }else {
       //     Toast.makeText(EMPCONNECT.this, "!!!!! MAIN OFF !!!!!!! ", Toast.LENGTH_SHORT).show();
       // }
            }
        });
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
                 first = new Character(ID1.charAt(0));
                Toast.makeText(EMPCONNECT.this, " EMP " + first, Toast.LENGTH_SHORT).show();
                Toast.makeText(EMPCONNECT.this, " NAME: " + ID1.substring(1), Toast.LENGTH_SHORT).show();
                isSuccess[0]= true;
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
                    Toast.makeText(EMPCONNECT.this, "INAVLID EMP ", Toast.LENGTH_SHORT).show();

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

                        if (main.equals("OFF")) {

                            btn3.setEnabled(false);
                            btn4.setEnabled(false);
                            btn5.setEnabled(false);
                            btn6.setEnabled(false);
                            btn7.setEnabled(false);
                            Toast.makeText(EMPCONNECT.this, "!!!!!! MAIN OFF !!!!!!!! ", Toast.LENGTH_SHORT).show();

                        } else if (main.equals("ON")) {

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

                            }
                            Toast.makeText(EMPCONNECT.this, "!!!!!! MAIN ON !!!!!! ", Toast.LENGTH_SHORT).show();

                        }


                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}
