package cwb.fingerprintpasswordpaydemo;

import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nestia.biometriclib.BiometricPromptManager;

import cwb.fingerprintpasswordpaydemo.popwindow.SelectPopupWindow;

public class MainActivity extends AppCompatActivity implements SelectPopupWindow.OnPopWindowClickListener{

    private TextView mTextView;
    private Button mButton;
    private BiometricPromptManager mManager;
    private SelectPopupWindow menuWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.text_view);
        mButton = findViewById(R.id.button);

        try{
            mManager = BiometricPromptManager.from(this);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SDK version is "+ Build.VERSION.SDK_INT);
            stringBuilder.append("\n");
            stringBuilder.append("是否支持指纹 : "+mManager.isHardwareDetected());
            stringBuilder.append("\n");
            stringBuilder.append("是否已经录入指纹 : "+mManager.hasEnrolledFingerprints());
            stringBuilder.append("\n");
            stringBuilder.append("是否设置数字键盘密码 : "+mManager.isKeyguardSecure());
            stringBuilder.append("\n");

            mTextView.setText(stringBuilder.toString());

            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mManager.isBiometricPromptEnable()) {
                        mManager.authenticate(new BiometricPromptManager.OnBiometricIdentifyCallback() {
                            @Override
                            public void onUsePassword() {
                                Toast.makeText(MainActivity.this, "onUsePassword", Toast.LENGTH_SHORT).show();
                                menuWindow = new SelectPopupWindow(MainActivity.this, MainActivity.this);
                                Rect rect = new Rect();
                                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                                int winHeight = getWindow().getDecorView().getHeight();
                                menuWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
                            }

                            @Override
                            public void onSucceeded() {
                                Toast.makeText(MainActivity.this, "onSucceeded", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed() {

                                Toast.makeText(MainActivity.this, "onFailed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(int code, String reason) {

                                Toast.makeText(MainActivity.this, "onError", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel() {

                                Toast.makeText(MainActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(MainActivity.this,"不支持指纹或者指纹没开启",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if(complete)
            Toast.makeText(this, "您输入的密码是"+psw, Toast.LENGTH_SHORT).show();
    }

}
