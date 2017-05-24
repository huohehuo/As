package com.as.as;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
//给图片加水印的方法
public class ImgWaterActivity extends AppCompatActivity {

    private ImageView img;

    private int width, height; // 图片的高度和宽带
    private Bitmap imgTemp; // 临时标记图
    private Bitmap imgMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        img = (ImageView) this.findViewById(R.id.img);
        Glide.with(this).load("https://www.baidu.com/img/bd_logo1.png").asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Log.e("get....","获取图片成功");
                imgMarker=resource;
                width = resource.getWidth();
                height = resource.getHeight();
                img.setImageDrawable(createDrawable("www.baidu.com"));
            }
        });

    }

    // 绘制带有透明背景的文字水印
    private Drawable createDrawable(String letter) {
        imgTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(imgTemp);//初始化画布绘制的图像到imgTemp上
        Paint paint = new Paint(); // 建立画笔
        paint.setDither(true);
        paint.setColor(Color.GRAY);
        paint.setFilterBitmap(true);
        Rect src = new Rect(0, 0, width, height);//创建一个指定的新矩形
        Rect dst = new Rect(0, 0, width, height);//创建一个指定的新矩形
        Rect bg = new Rect(width, 0, width, -9);//创建一个指定的新矩形
        canvas.drawBitmap(imgMarker, src, dst, paint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint
        //用于绘制位于图片下方的文字
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(40.0f);//文字大小
        textPaint.setTypeface(Typeface.DEFAULT); // 采用默认的宽度
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setShadowLayer(1f, 0f, 3f, Color.LTGRAY);//设置文字阴影
        textPaint.getTextBounds(letter,0,0,dst);
        canvas.drawText(letter, 5, height-18, textPaint);
        //用于绘制位于图片下方的半透明背景图
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        bgPaint.setTypeface(Typeface.DEFAULT); // 采用默认的宽度
        bgPaint.setColor(Color.BLACK);
        bgPaint.setAlpha(40);
        bgPaint.setAntiAlias(true);
        canvas.drawRect(0,height-55,width,height,bgPaint);

        canvas.save(Canvas.ALL_SAVE_FLAG);//保存所有的元素
        canvas.restore();
        return (Drawable) new BitmapDrawable(getResources(), imgTemp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

}