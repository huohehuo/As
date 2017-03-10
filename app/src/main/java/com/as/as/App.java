package com.as.as;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.tauth.Tencent;

/**
 * Created by Administrator on 2017/3/9.
 */

public class App extends Application{
    private static Context context;
    private static Tencent mTencent;
    private static Toast mToast;
    private static AuthInfo mAuthInfo;

    //    private static App app = new App();
//    public static App getApp(){
//        return app;
//    }
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(Config.QQ_ID, this);

        mAuthInfo = new AuthInfo(this, Config.SINA_APP_KEY,Config.SINA_REDIRECT_URL,Config.SINA_SCOPE);


    }

    public static Context getContext(){
        return context;
    }
    public static Tencent getTencent(){
        return mTencent;
    }
    public static AuthInfo getSina(){
        return mAuthInfo;
    }
    //全局Toast,致使无等待显示下一个Toast
    public static void showToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
