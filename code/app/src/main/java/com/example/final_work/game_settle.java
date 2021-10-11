package com.example.final_work;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class game_settle extends AppCompatActivity
{
    TextView TV_title, TV_correct_amount, TV_prize, TV_get_coupon;
    Button BTN_back;

    int total_correct;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settle);

        TV_title = findViewById(R.id.TV_title);
        TV_correct_amount = findViewById(R.id.TV_correct_amount);
        TV_prize = findViewById(R.id.TV_prize);
        TV_get_coupon = findViewById(R.id.TV_get_coupon);

        BTN_back = findViewById(R.id.BTN_back);

        BTN_back.setOnClickListener(back);

        //最後回傳的總答對題數
        total_correct = getSharedPreferences("database", MODE_PRIVATE).getInt("total_correct", 0);

        if (total_correct < 3)
        {
            message = "安慰獎   請下次再來QQ";
        }
        else if(total_correct == 3)
        {
            message = "九折優惠券!";
        }
        else if(total_correct == 4)
        {
            message = "七折優惠券!!";
        }
        else if(total_correct == 5)
        {
            message = "一折優惠券!!!";
        }

        TV_correct_amount.setText("你總共答對了 : " + Integer.toString(total_correct) + "題");
        TV_get_coupon.setText(message);

        //記得要把答對題數重置並更新檔案內容
        total_correct = 0;
        SharedPreferences saved_data = getSharedPreferences("database", MODE_PRIVATE);
        saved_data.edit().putInt("total_correct", total_correct).commit();
    }


    Button.OnClickListener back = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(game_settle.this, MainActivity.class);
            startActivity(intent);
        }
    };

}
