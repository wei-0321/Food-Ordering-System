package com.example.final_work;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class game extends AppCompatActivity
{   //變數宣告(包含介面元件)
    private TextView TV_title, TV_introduce, TV_description1, TV_description2;
    private ImageView IMV_status;
    private Button BTN_start;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //與介面連結
        BTN_start = findViewById(R.id.BTN_start);

        //建立按鈕的監聽器
        BTN_start.setOnClickListener(start_game);
    }

    Button.OnClickListener start_game = new Button.OnClickListener()
    {
        //使用者點選開始 =>  進入遊戲畫面
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(game.this, question1.class);
            startActivity(intent);
        }
    };


}
