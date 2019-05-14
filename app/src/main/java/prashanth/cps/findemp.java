package prashanth.cps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class findemp extends AppCompatActivity {
    Button btn;
    EditText fnd;
    TextView txt1,txt2;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_findemp);

        btn=(Button)findViewById(R.id.btnfd);
        fnd=(EditText)findViewById(R.id.edtfnd);
        txt1=(TextView) findViewById(R.id.txt1);
        txt2=(TextView) findViewById(R.id.txt2);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID=fnd.getText().toString();
                if(!ID.isEmpty() ) {

                    String ID1 = findID(ID);
                    char first = ID1.charAt(0);
                    txt1.setText("EMP GRADE:" + first);
                    txt2.setText("EMP NAME:" + ID1.substring(1));

                }else {
                    Toast.makeText(findemp.this, "Enter EMP ID ", Toast.LENGTH_SHORT).show();
                }
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

}
