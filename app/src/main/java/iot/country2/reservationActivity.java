package iot.country2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by hwi on 2017-07-26.
 */

public class reservationActivity extends AppCompatActivity {
    private int currentApiVersion; //네비바 없애기
    DatePicker dp;
    EditText rnum, rname;
    public static String pn;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation);
        dp = (DatePicker) findViewById(R.id.datePicker);
        rnum = (EditText)findViewById(R.id.renum);
        rname = (EditText)findViewById(R.id.rename);

        //네비게이션바 없애기
        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
    }
    public void todetail(View view){
        finish();
    }

    public void sendmessage(View view) {
        if(rname.length()<=0 || rnum.length()<=0){
            Toast.makeText(this,"이름과 예약자 수를 입력해 주세요",Toast.LENGTH_SHORT).show();
        }
        else {
            int year = dp.getYear();
            int mon = dp.getMonth();
            int day = dp.getDayOfMonth();
            String num = rnum.getText().toString();
            String name = rname.getText().toString();
            String text = year+"년"+mon+"월"+day+"일 "+name+"외 "+num+"명 예약입니다.";

            if (Build.VERSION.SDK_INT > 18) {
                String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getApplicationContext());
                Intent sendIntent = new Intent(Intent.ACTION_SEND, Uri.parse("smsto:" + pn));
                sendIntent.setType("text/plain");
                sendIntent.putExtra("address", pn);
                sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                if (defaultSmsPackageName != null) {
                    sendIntent.setPackage(defaultSmsPackageName);
                }
                startActivity(sendIntent);
            } else {
                // 18이하는 기존코드
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + pn));
                intent.putExtra("sms_body", text);
                startActivity(intent);
            }
        }
    }
}