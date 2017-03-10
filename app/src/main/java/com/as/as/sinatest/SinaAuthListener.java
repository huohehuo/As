package com.as.as.sinatest;

import android.os.Bundle;
import android.util.Log;

import com.as.as.App;
import com.as.as.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.AccessTokenKeeper;
import com.sina.weibo.sdk.exception.WeiboException;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/3/9.
 */

public class SinaAuthListener implements WeiboAuthListener{

    private Oauth2AccessToken mAccessToken;
    private SinaIO sinaIO;
    public SinaAuthListener(SinaIO sinaIO){
        this.sinaIO = sinaIO;
    }
    @Override
    public void onComplete(Bundle bundle) {
        // 从 Bundle 中解析 Token
        mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
        if (mAccessToken.isSessionValid()) {
            // 保存 Token 到 SharedPreferences
            AccessTokenKeeper.writeAccessToken(App.getContext(), mAccessToken);
            Log.e("sina-onComplete", mAccessToken.toString());
            App.showToast("授权成功");
            sinaIO.getData(updateTokenView(true));
        } else {
            // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
            String code = bundle.getString("code", "");
            App.showToast("签名错误："+code);
            Log.e("sina-onComplete-error",code);
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {
            Log.e("sina-onWeiBoException",e.toString());
        App.showToast("授权错误onWeiboException");
    }

    @Override
    public void onCancel() {
            App.showToast("取消授权");
    }
    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private String updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = App.getContext().getString(R.string.weibosdk_demo_token_to_string_format_1);
       // mTokenText.setText(String.format(format, mAccessToken.getToken(), date));

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = App.getContext().getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
        return message;
    }
}
