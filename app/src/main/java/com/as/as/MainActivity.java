package com.as.as;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.as.as.qqtest.DataIO;
import com.as.as.qqtest.QLoginListener;
import com.as.as.qqtest.QShareListener;
import com.as.as.sinatest.SinaAuthListener;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.Tencent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView tv;
    private Tencent mTencent = App.getTencent();
    private AuthInfo mAuthInfo = App.getSina();
    //获取回调信息
    private DataIO dataIO = new DataIO() {
        @Override
        public void getLoginData(String str) {
            tv.setText(str);
        }
    };
    private SsoHandler mSsoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }



    @OnClick({R.id.button, R.id.button2, R.id.button3, R.id.button4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button://登录
                mTencent.login(this, "get_user_info", new QLoginListener(dataIO));
                break;
            case R.id.button2://分享
                if (mTencent.getOpenId()==null){
                    App.showToast("openid为空");
                }
                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
                mTencent.shareToQQ(MainActivity.this, params, new QShareListener(dataIO));
                break;
            case R.id.button3://退出登录
                App.showToast("退出登录");
                tv.setText("");
                mTencent.logout(MainActivity.this);
                break;
            case R.id.button4:
                mSsoHandler = new SsoHandler(MainActivity.this, mAuthInfo);
                mSsoHandler. authorize(new SinaAuthListener());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mTencent){
            mTencent.onActivityResultData(requestCode, resultCode, data, new QLoginListener(dataIO));
        }

        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
