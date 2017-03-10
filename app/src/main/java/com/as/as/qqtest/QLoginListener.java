package com.as.as.qqtest;

import android.util.Log;

import com.as.as.App;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by Administrator on 2017/3/9.
 */

public class QLoginListener implements IUiListener{
    private DataIO dataIO;
    public QLoginListener(DataIO dataIO){
        this.dataIO = dataIO;
    } 
    @Override
    public void onComplete(Object o) {
        Log.e("QLogin-onComplete:", "QQ登录seccuss...");
        App.showToast("QQ登录成功"+o.toString());
        dataIO.getLoginData("QQ登录回调信息"+o.toString());
    }

    @Override
    public void onError(UiError uiError) {
        Log.e("QLogin-onError:", "QQ登录error...");
        dataIO.getLoginData("QQ登录回调信息"+uiError.toString());

        App.showToast("QQ登录失败");
    }

    @Override
    public void onCancel() {
        Log.e("QLogin-onCancel:", "取消QQ登录");
        App.showToast("QQ登录取消");

    }
}
