package prashanth.cps;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;


import static java.util.Base64.getEncoder;

public class empcreate extends AppCompatActivity {

    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private static KeySpec myKeySpec;
    private static SecretKeyFactory mySecretKeyFactory;
    private static Cipher cipher;
    public static byte[] keyAsBytes;
    private static String myEncryptionKey;
    private static String myEncryptionScheme;
    public static SecretKey key;
    public static String grade;
    DatabaseReference mDatabase;

    private EditText txt1,txt2;
    private TextView txt3;
    private Button btn1;
    private String empEncrypt;
    public ImageView img;

   /* private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference refernce=firebaseDatabase.getReference();
    private DatabaseReference tx1=refernce.child("tx1"); */


    //FirebaseDatabase firebaseDatabase;
    //DatabaseReference databaseReference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_empcreate);

        txt1= (EditText) findViewById(R.id.edttxt1);
        txt2= (EditText) findViewById(R.id.edttxt2);
        txt3= (TextView) findViewById(R.id.edttxt3);
        btn1= (Button)findViewById(R.id.button);
        img=(ImageView)findViewById(R.id.img);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("empid");




        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int z=4;
                grade=txt1.getText().toString();
                empEncrypt=txt2.getText().toString();

                if(!grade.isEmpty() && !empEncrypt.isEmpty()) {
                    if (grade.equals("1") || grade.equals("2") || grade.equals("3")) {
                    empEncrypt = grade.concat(empEncrypt);
                    String empEncrypted = getID(empEncrypt).replace("\n", "");
                    txt3.setText("EMP ID  :" + empEncrypted);
                    Log.d("TEST", "emp ID=  " + empEncrypted);
                    Toast.makeText(empcreate.this, empEncrypted, Toast.LENGTH_LONG).show();

                    int a = 500, b = 500;
                    MultiFormatWriter mul = new MultiFormatWriter();
                    try {
                        BitMatrix bit = mul.encode(empEncrypted, BarcodeFormat.QR_CODE, a, b);
                        BarcodeEncoder bar = new BarcodeEncoder();
                        Bitmap bitmap = bar.createBitmap(bit);
                        img.setImageBitmap(bitmap);
                        try {
                            FileOutputStream outStream = null;
                            File sdCard = Environment.getExternalStorageDirectory();
                            File dir = new File(sdCard.getAbsolutePath() + "/CPS ADMIN");
                            dir.mkdirs();
                            String fileName = String.format("%d.jpg", System.currentTimeMillis());
                            File outFile = new File(dir, fileName);
                            outStream = new FileOutputStream(outFile);

                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                            outStream.flush();
                            outStream.close();
                            Toast.makeText(empcreate.this, "ID Saved In Device", Toast.LENGTH_LONG).show();


                        }catch (IOException e){
                            Toast.makeText(empcreate.this, "ERROR: "+e, Toast.LENGTH_LONG).show();
                        }
                    } catch (WriterException e) {
                    }

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(empEncrypted, empEncrypted);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(empcreate.this, "ID COPYED", Toast.LENGTH_SHORT).show();



                    mDatabase.push().setValue(empEncrypted).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(empcreate.this, "Added Successfully!!!", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(empcreate.this, "Failed: Something wrong !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                        Toast.makeText(empcreate.this, "Enter Grade 1 / 2 / 3 ", Toast.LENGTH_SHORT).show();
                    }

                    } else {
                        Toast.makeText(empcreate.this, "Enter Grade and NAME ", Toast.LENGTH_SHORT).show();
                    }

            }
        });




    }
    public static String getID(String unencryptedString) {
        String encryptedString = null;
        try {
            myEncryptionKey = "ThisIsSecretEncryptionKey";
            myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
            keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
            myKeySpec = new DESedeKeySpec(keyAsBytes);
            mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
            cipher = Cipher.getInstance(myEncryptionScheme);
            key = mySecretKeyFactory.generateSecret(myKeySpec);

            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);


          //  BASE64Encoder base64encoder = new BASE64Encoder();
   //-->  //   encryptedString = base64encoder.encode(encryptedText);

            byte[] encodevalue= android.util.Base64.encode(encryptedText, android.util.Base64.DEFAULT);

            encryptedString= bytes2String(encodevalue);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }
    private static String bytes2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }
}
