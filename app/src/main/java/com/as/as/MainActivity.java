package com.as.as;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.as.as.qqtest.DataIO;
import com.as.as.qqtest.QLoginListener;
import com.as.as.qqtest.QShareListener;
import com.as.as.sinatest.SinaAuthListener;
import com.as.as.sinatest.SinaIO;
import com.as.as.sinatest.WBShare2Activity;
import com.as.as.wxapi.WeChatActivity;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.Tencent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IWeiboHandler.Response{

    @BindView(R.id.tv)
    TextView tv;
    private Tencent mTencent = App.getTencent();
    private AuthInfo mAuthInfo = App.getSina();
    /** 微博分享的接口实例 */
    private IWeiboShareAPI mWeiboShareAPI=App.getmWeiboShareAPI();

    public static final String KEY_SHARE_TYPE = "key_share_type";
    public static final int SHARE_CLIENT = 1;
    public static final int SHARE_ALL_IN_ONE = 2;
    private int mShareType =2;


    //获取回调信息
    private DataIO dataIO = new DataIO() {
        @Override
        public void getLoginData(String str) {
            tv.setText(str);
        }
    };
    private SinaIO sinaIO = new SinaIO() {
        @Override
        public void getData(String str) {
            tv.setText(str);
        }
    };

    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();
        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }
    }
    /**
     * @see {@link Activity#onNewIntent}
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }



    @OnClick({R.id.button, R.id.button2, R.id.button3, R.id.button4,R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button://QQ登录
                mTencent.login(this, "get_user_info", new QLoginListener(dataIO));
                break;
            case R.id.button2://QQ分享
//                if (mTencent.getOpenId()==null){
//                    App.showToast("openid为空");
//                }
                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
                mTencent.shareToQQ(MainActivity.this, params, new QShareListener(dataIO));
                break;
            case R.id.button3://QQ退出登录
                App.showToast("QQ退出登录");
                tv.setText("");
                mTencent.logout(MainActivity.this);
                break;
            case R.id.button4://新浪账户登录
                mSsoHandler = new SsoHandler(MainActivity.this, mAuthInfo);
                mSsoHandler. authorize(new SinaAuthListener(sinaIO));
                break;
            case R.id.btn5://新浪分享
                sendMultiMessage();
                break;
            case R.id.btn6://新浪用户登出
                AccessTokenKeeper.clear(App.getContext());
                // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
                // 第一次启动本应用，AccessToken 不可用
                mAccessToken = new Oauth2AccessToken();
                mAccessToken = AccessTokenKeeper.readAccessToken(this);
                if (mAccessToken.isSessionValid()) {
                    //如果还有效，do something
                    tv.setText(mAccessToken.getToken());
                }else{
                    tv.setText("");
                }
                break;
            case R.id.btn7://微博分享2（主要用于测试两个页面同时启用分享所返回的指定用于接收回调的Activity）
                startActivity(new Intent(MainActivity.this,WBShare2Activity.class));
                break;
            case R.id.btn8://微信登录
                startActivity(new Intent(MainActivity.this,WeChatActivity.class));
                break;
            case R.id.btn9://加水印的图片处理
                startActivity(new Intent(MainActivity.this,ImgWaterActivity.class));
                break;
        }
    }
    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMultiMessage() {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text ="我正在用第三方分享消息";
        weiboMessage.textObject =textObject;
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        // 3. 发送请求消息到微博，唤起微博分享界面(自动选择，客户端或者网页)
        if (mShareType == SHARE_ALL_IN_ONE) {
            //AuthInfo authInfo = new AuthInfo(this, Config.SINA_APP_KEY, Config.SINA_REDIRECT_URL, Config.SINA_SCOPE);
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            mWeiboShareAPI.sendRequest(this, request, mAuthInfo, token, new SinaAuthListener(sinaIO));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // Tencent.onActivityResult(requestCode,resultCode,data);
        //腾讯获取返回
        if (null != mTencent){
            mTencent.onActivityResultData(requestCode, resultCode, data, new QLoginListener(dataIO));
        }
        //新浪获取返回
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     */
    @Override
    public void onResponse(BaseResponse baseResp) {
        if(baseResp!= null){
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this,
                            getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: " + baseResp.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


//    /**
//     * 创建图片消息对象。
//     *
//     * @return 图片消息对象。
//     */
//    private ImageObject getImageObj() {
//        ImageObject imageObject = new ImageObject();
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageView.getDrawable();
//        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
//        imageObject.setImageObject(bitmap);
//        return imageObject;
//    }
}
