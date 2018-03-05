package iot.country2;

import android.app.Application;
import com.tsengvn.typekit.Typekit;
/**
 * Created by hwi on 2017-07-27.
 */

public class applicationbase extends Application {
    @Override public void onCreate() {
        super.onCreate();
        // 폰트 정의
        Typekit.getInstance().addNormal(Typekit.createFromAsset(this, "fonts/HoonWhitecatR.ttf"));
    }
}