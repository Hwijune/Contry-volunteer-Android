package iot.country2;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import static iot.country2.R.id.tab1;

/**
 * Created by hwi on 2017-07-27.
 */

public class ListActivity extends AppCompatActivity {
    private int currentApiVersion; //네비바 없애기
    static int sheet = 0;
    static int row = 1;
    static boolean listflag = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.tab_set);

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();


        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TabHost.TabSpec ts1 = tabHost1.newTabSpec(("Tab Spec 1"));
        ts1.setContent(tab1);
        ts1.setIndicator("전체");
        tabHost1.addTab(ts1);

        TabHost.TabSpec ts2 = tabHost1.newTabSpec(("Tab Spec 2"));
        ts2.setContent(R.id.tab2);
        ts2.setIndicator("충주\n단양");
        tabHost1.addTab(ts2);

        TabHost.TabSpec ts3 = tabHost1.newTabSpec(("Tab Spec 3"));
        ts3.setContent(R.id.tab3);
        ts3.setIndicator("증평\n괴산\n음성");
        tabHost1.addTab(ts3);

        TabHost.TabSpec ts4 = tabHost1.newTabSpec(("Tab Spec 4"));
        ts4.setContent(R.id.tab4);
        ts4.setIndicator("청주\n보은");
        tabHost1.addTab(ts4);

        TabHost.TabSpec ts5 = tabHost1.newTabSpec(("Tab Spec 5"));
        ts5.setContent(R.id.tab5);
        ts5.setIndicator("옥천\n영동");
        tabHost1.addTab(ts5);

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
        addname();
    }

    public void todetail(View view) {
        listflag = true;
        String aaa = view.getResources().getResourceName(view.getId());
        String[] bbb = aaa.split("a");
        sheet = Integer.parseInt(bbb[1]);
        row = Integer.parseInt(bbb[2]);
        Intent intent = new Intent(ListActivity.this, detailActivity.class);
        startActivity(intent);
    }

    public void addname() {
        //체험목록 추가

        Workbook workbook = null;
        Sheet sheet = null;
        try {

            for (int sheets = 0; sheets < 10; sheets++) {
                InputStream inputStream = getBaseContext().getResources().getAssets().open("체험마을 데이터 총합.xls");
                workbook = Workbook.getWorkbook(inputStream);

                sheet = workbook.getSheet(sheets);

                for (row = 1; row < sheet.getRows(); row++) {
                    if (row != 5 && sheets != 5) {
                        //5시트5행이없는데 잡혀서 막음
                        String aname = sheet.getCell(0, row).getContents();
                        String bname = sheet.getCell(4, row).getContents();

                        String resName1 = "a" + sheets + "a" + row;//id이름
                        String packName = this.getPackageName(); // 패키지명
                        int resID1 = getResources().getIdentifier(resName1, "id", packName);
                        Button addbname = (Button) findViewById(resID1);
                        addbname.setText(aname + " \n " + bname);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }
    }
}
