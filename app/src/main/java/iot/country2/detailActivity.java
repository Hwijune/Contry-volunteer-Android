package iot.country2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by hwi on 2017-07-26.
 */

public class detailActivity extends AppCompatActivity {
    private int currentApiVersion; //네비바 없애기
    Button resb;
    int sheets=0;
    int row =1;


    TextView name, address, info, info2, info3, leader, leaderphone;
    ImageButton mainimage, scroolimg1, scroolimg2, scroolimg3, scroolimg4, scroolimg5, scroolimg6, scroolimg7;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        //액션바 투명
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        // 플래그받아서 액티비티보기
        if(ListActivity.listflag)
        {
            sheets = ListActivity.sheet;
            row = ListActivity.row;
            ListActivity.listflag = false;
        }
        else
        {
            sheets = MapsActivity.sendsheets;
            row = MapsActivity.sendrow;
        }

        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        info = (TextView) findViewById(R.id.info);
        info2 = (TextView) findViewById(R.id.info2);
        info3 = (TextView) findViewById(R.id.info3);
        leader = (TextView) findViewById(R.id.leader);
        leaderphone = (TextView) findViewById(R.id.leaderphone);
        excel();
        reservationActivity.pn = leaderphone.getText().toString();

        mainimage = (ImageButton) findViewById(R.id.mainimage);
        scroolimg1 = (ImageButton) findViewById(R.id.scrollimg1);
        scroolimg2 = (ImageButton) findViewById(R.id.scrollimg2);
        scroolimg3 = (ImageButton) findViewById(R.id.scrollimg3);
        scroolimg4 = (ImageButton) findViewById(R.id.scrollimg4);
        scroolimg5 = (ImageButton) findViewById(R.id.scrollimg5);
        scroolimg6 = (ImageButton) findViewById(R.id.scrollimg6);
        scroolimg7 = (ImageButton) findViewById(R.id.scrollimg7);
        changeImage();

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

    public void excel() {
        Workbook workbook = null;
        Sheet sheet = null;
        try {
            InputStream inputStream = getBaseContext().getResources().getAssets().open("체험마을 데이터 총합.xls");
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(sheets);
            String aname = sheet.getCell(0, row).getContents();
            String aaddress = sheet.getCell(2,row).getContents();
            String ainfo = sheet.getCell(25, row).getContents();
            String ainfo2 = sheet.getCell(4,row).getContents();
            String ainfo3 = sheet.getCell(5,row).getContents();
            String aleader = sheet.getCell(9,row).getContents();
            String aleaderphone = sheet.getCell(10,row).getContents();

            name.setText(aname);
            address.setText(aaddress);
            info.setText(ainfo);
            info2.setText(ainfo2);
            info3.setText(ainfo3);
            leader.setText(aleader);
            leaderphone.setText(aleaderphone);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }
    }

    public void changeImage() {
        String packName = this.getPackageName(); // 패키지명

        String resName1 = "@drawable/img" + sheets + "_" + row + "_" + 1;
        int resID1 = getResources().getIdentifier(resName1, "drawable", packName);
        scroolimg1.setImageResource(resID1);
        mainimage.setImageResource(resID1);

        String resName2 = "@drawable/img" + sheets + "_" + row + "_" + 2;
        int resID2 = getResources().getIdentifier(resName2, "drawable", packName);
        scroolimg2.setImageResource(resID2);

        String resName3 = "@drawable/img" + sheets + "_" + row + "_" + 3;
        int resID3 = getResources().getIdentifier(resName3, "drawable", packName);
        scroolimg3.setImageResource(resID3);

        String resName4 = "@drawable/img" + sheets + "_" + row + "_" + 4;
        int resID4 = getResources().getIdentifier(resName4, "drawable", packName);
        scroolimg4.setImageResource(resID4);

        String resName5 = "@drawable/img" + sheets + "_" + row + "_" + 5;
        int resID5 = getResources().getIdentifier(resName5, "drawable", packName);
        scroolimg5.setImageResource(resID5);

        String resName6 = "@drawable/img" + sheets + "_" + row + "_" + 6;
        int resID6 = getResources().getIdentifier(resName6, "drawable", packName);
        scroolimg6.setImageResource(resID6);

        String resName7 = "@drawable/img" + sheets + "_" + row + "_" + 7;
        int resID7 = getResources().getIdentifier(resName7, "drawable", packName);
        scroolimg7.setImageResource(resID7);
    }

    public void call(View view){
        if(leaderphone.getText() == ""){
            Toast.makeText(this,"번호가 없습니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+leaderphone.getText().toString()));
            startActivity(intent);
        }
    }
    public void reslay(View view){
        if(leaderphone.getText() == ""){
            Toast.makeText(this,"번호가 없습니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Intent intent = new Intent(detailActivity.this, reservationActivity.class);
            startActivity(intent);
        }
    }
    public void homepage(View view){
        Workbook workbook = null;
        Sheet sheet = null;
        try {
            InputStream inputStream = getBaseContext().getResources().getAssets().open("체험마을 데이터 총합.xls");
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(sheets);
            String homepage = sheet.getCell(12, row).getContents();
            if(homepage == ""){
                Toast.makeText(this,"홈페이지가 없습니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(homepage));
                startActivity(intent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }
    }

    public void imageClick1(View v)
    {
        String packName = this.getPackageName(); // 패키지명
        String resName1 = "@drawable/img" + sheets + "_" + row + "_" + 1;
        int resID1 = getResources().getIdentifier(resName1, "drawable", packName);
        mainimage.setImageResource(resID1);
    }

    public void imageClick2(View v)
    {
        String packName = this.getPackageName(); // 패키지명
        String resName1 = "@drawable/img" + sheets + "_" + row + "_" + 2;
        int resID1 = getResources().getIdentifier(resName1, "drawable", packName);
        mainimage.setImageResource(resID1);
    }

    public void imageClick3(View v)
    {
        String packName = this.getPackageName(); // 패키지명
        String resName1 = "@drawable/img" + sheets + "_" + row + "_" + 3;
        int resID1 = getResources().getIdentifier(resName1, "drawable", packName);
        mainimage.setImageResource(resID1);
    }

    public void imageClick4(View v)
    {
        String packName = this.getPackageName(); // 패키지명
        String resName1 = "@drawable/img" + sheets + "_" + row + "_" + 4;
        int resID1 = getResources().getIdentifier(resName1, "drawable", packName);
        mainimage.setImageResource(resID1);
    }

    public void imageClick5(View v)
    {
        String packName = this.getPackageName(); // 패키지명
        String resName1 = "@drawable/img" + sheets + "_" + row + "_" + 5;
        int resID1 = getResources().getIdentifier(resName1, "drawable", packName);
        mainimage.setImageResource(resID1);
    }

    public void imageClick6(View v)
    {
        String packName = this.getPackageName(); // 패키지명
        String resName1 = "@drawable/img" + sheets + "_" + row + "_" + 6;
        int resID1 = getResources().getIdentifier(resName1, "drawable", packName);
        mainimage.setImageResource(resID1);
    }

    public void imageClick7(View v)
    {
        String packName = this.getPackageName(); // 패키지명
        String resName1 = "@drawable/img" + sheets + "_" + row + "_" + 7;
        int resID1 = getResources().getIdentifier(resName1, "drawable", packName);
        mainimage.setImageResource(resID1);
    }
}