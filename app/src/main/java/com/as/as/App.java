package com.as.as;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

/**
 * Created by Administrator on 2017/3/9.
 */

public class App extends Application{
    private static Context context;
    private static Tencent mTencent;
    private static Toast mToast;
    private static AuthInfo mAuthInfo;
    /** 微博分享的接口实例 */
    private static IWeiboShareAPI mWeiboShareAPI;

    private static IWXAPI iwxapi;
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
        // 创建微博 SDK 接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Config.SINA_APP_KEY);
        //微信初始化，注册
        iwxapi = WXAPIFactory.createWXAPI(this,Config.WX_APP_ID,true);
        iwxapi.registerApp(Config.WX_APP_ID);

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
    public static IWeiboShareAPI getmWeiboShareAPI(){
        return mWeiboShareAPI;
    }
    public static IWXAPI getWeChat(){
        return iwxapi;
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
