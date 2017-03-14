package com.as.as.wxapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.as.as.App;
import com.as.as.Config;
import com.as.as.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sina.weibo.sdk.api.TextObject;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeChatActivity extends AppCompatActivity {

    @BindView(R.id.wx_check)
    CheckBox wxCheck;
    private int WX_type_Friend = SendMessageToWX.Req.WXSceneSession;
    private int WX_type_Pengyou = SendMessageToWX.Req.WXSceneTimeline;

    private int Send_Text = 1;
    private int Send_Img = 2;
    private int Send_Web = 3;
    private int Send_Music = 4;
    private int Send_Video = 5;

    private IWXAPI iwxapi = App.getWeChat();

    private boolean isFriend=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat);
        ButterKnife.bind(this);
        //iwxapi.registerApp(Config.WX_APP_ID);//注意注册
        wxCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isFriend=false;
                    Log.e("check change true","");
                }else{
                    isFriend=true;
                    Log.e("check change false","");
                }
            }
        });

    }

    @OnClick({R.id.wx_btn1, R.id.wx_btn2, R.id.wx_btn3, R.id.wx_btn4, R.id.wx_btn5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wx_btn1:
                if (isFriend){
                    sendToWX("发送文字", WX_type_Friend, Send_Text);
                }else{
                    sendToWX("发送文字", WX_type_Pengyou, Send_Text);
                }
                break;
            case R.id.wx_btn2:
                if (isFriend){
                    sendToWX("发送图片", WX_type_Friend, Send_Img);
                }else{
                    sendToWX("发送图片", WX_type_Pengyou, Send_Img);
                }
                break;
            case R.id.wx_btn3:
                if (isFriend){
                    sendToWX("发送网页", WX_type_Friend, Send_Web);
                }else{
                    sendToWX("发送网页", WX_type_Pengyou, Send_Web);
                }
                break;
            case R.id.wx_btn4:
                if (isFriend){
                    sendToWX("发送音乐", WX_type_Friend, Send_Music);
                }else{
                    sendToWX("发送音乐", WX_type_Pengyou, Send_Music);
                }
                break;
            case R.id.wx_btn5:
                if (isFriend){
                    sendToWX("发送视频", WX_type_Friend, Send_Video);
                }else{
                    sendToWX("发送视频", WX_type_Pengyou, Send_Video);
                }
                break;
        }
    }

    private void sendToWX(final String text, final int mTargetScene, final int type) {
        Glide.with(this).load("https://www.baidu.com/img/bd_logo1.png").asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (type == Send_Text) {
                    WXTextObject textObj = new WXTextObject();
                    Log.e("sendToWX", text);
                    textObj.text = text;
                    WXMediaMessage msg = new WXMediaMessage();
                    msg.mediaObject = textObj;
                    msg.description = text;
                    SendMessageToWX.Req req2 = new SendMessageToWX.Req();
                    req2.transaction = buildTransaction("text");
                    req2.message = msg;
                    req2.scene = mTargetScene;
                    iwxapi.sendReq(req2);
                }
                if (type == Send_Img) {
                    WXImageObject imageObject = new WXImageObject(resource);
                    WXMediaMessage mediaMessage = new WXMediaMessage();
                    mediaMessage.title = text+"并不会显示标题";
                    mediaMessage.description = "并不会显示描述。。。。描述。。。。。。描述。。。。描述。。。。";
                    //mediaMessage.mediaObject = getTextObj(text);//两个对象建立方式一样，所以会被替换掉不会共存
                    mediaMessage.mediaObject = imageObject;
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("img");
                    req.message = mediaMessage;
                    req.scene = mTargetScene;
                    iwxapi.sendReq(req);
                }
                if (type == Send_Web) {
                    WXWebpageObject webpageObject = new WXWebpageObject();
                    webpageObject.webpageUrl = "http://music.163.com/#/song?id=342224";
                    WXMediaMessage mediaMessage = new WXMediaMessage();
                    mediaMessage.title = text;
                    mediaMessage.description = "描述。。。";
                    mediaMessage.mediaObject = webpageObject;
                    mediaMessage.setThumbImage(resource);
                    //Bitmap thumb = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                    //mediaMessage.thumbData = bmpToByteArray(resource, true);//压缩图片
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = mediaMessage;
                    req.scene = mTargetScene;
                    iwxapi.sendReq(req);
                }
                if (type == Send_Music) {
                    WXMusicObject musicObject = new WXMusicObject();
                    musicObject.musicUrl = "http://y.baidu.com/song/29246?fm=altg_new3";
                    WXMediaMessage mediaMessage = new WXMediaMessage();
                    mediaMessage.title = text;
                    mediaMessage.description = "描述。。。";
                    mediaMessage.mediaObject = musicObject;
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("music");
                    req.message = mediaMessage;
                    req.scene = mTargetScene;
                    iwxapi.sendReq(req);
                }
                if (type == Send_Video) {
                    WXVideoObject videoObject = new WXVideoObject();
                    videoObject.videoUrl = "http://www.iqiyi.com/v_19rrahdfo4.html?src=frbdaldjunest";
                    WXMediaMessage mediaMessage = new WXMediaMessage();
                    mediaMessage.title = text;
                    mediaMessage.description = "描述。。。";
                    mediaMessage.mediaObject = videoObject;
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("video");
                    req.message = mediaMessage;
                    req.scene = mTargetScene;
                    iwxapi.sendReq(req);
                }
            }
        });
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //轻微压缩图片
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    private static WXTextObject getTextObj(String text) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = text;
        return textObject;
    }
}
