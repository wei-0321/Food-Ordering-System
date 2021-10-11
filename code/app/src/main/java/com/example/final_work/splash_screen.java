package com.example.final_work;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

public class splash_screen extends AppCompatActivity
{
    long delay=8000;
    private ProgressBar pb1;
    private TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        pb1=findViewById(R.id.pb1);
        tv3=findViewById(R.id.tv3);

        pb1.setMax(100);
        pb1.setScaleY(3f);
        progressAnimation();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //跳轉至登入頁面
                Intent intent = new Intent(splash_screen.this, login.class);

                startActivity(intent);
                finish();
            }
        }, 8000);    //設定延遲時間
    }
    public void progressAnimation()
    {
        progressBar_animation anim = new progressBar_animation(this,pb1,tv3,0f,100f);
        anim.setDuration(8000);
        pb1.setAnimation(anim);
    }

    //內部類別
    private class progressBar_animation extends Animation
    {
        private Context context;
        private ProgressBar progressbar;
        private TextView textView;
        private float from;
        private float to;

        public progressBar_animation(Context context,ProgressBar progressbar,TextView textView,float from,float to){
            this.context=context;
            this.progressbar=progressbar;
            this.textView=textView;
            this.from=from;
            this.to=to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t)
        {
            super.applyTransformation(interpolatedTime, t);
            float value=from+(to-from)*interpolatedTime;
            progressbar.setProgress((int)value);
            textView.setText((int)value+" %");

            if(value==to){
                context.startActivity(new Intent(context,login.class));
            }
        }
    }
}
