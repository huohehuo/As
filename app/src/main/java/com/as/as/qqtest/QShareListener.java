package com.as.as.qqtest;

import android.util.Log;

import com.as.as.App;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by Administrator on 2017/3/9.
 */

public class QShareListener implements IUiListener{

    private DataIO dataIO;
    public QShareListener(DataIO dataIO){
        this.dataIO = dataIO;
    }
    @Override
    public void onComplete(Object o) {
        Log.e("QShare-onComplete",o.toString());
        App.showToast("分享成功"+o.toString());
        dataIO.getLoginData("分享回调信息"+o.toString());
    }
    @Override
    public void onError(UiError uiError) {
        Log.e("QShare-onError",uiError.toString());
        App.showToast("分享失败");
        dataIO.getLoginData("分享回调信息"+uiError.toString());

    }
    @Override
    public void onCancel() {
        Log.e("QShare-onCancel","取消分享");
        App.showToast("取消分享");
    }
}
