package org.sopt.captainna.component;

import android.app.Application;
import android.content.Context;

/**
 * Created by KoJunHee on 2017-06-27.
 */

public class CaptainNaApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }


    public static Context getAppmateApplication() {
        return mContext;
    }
}
