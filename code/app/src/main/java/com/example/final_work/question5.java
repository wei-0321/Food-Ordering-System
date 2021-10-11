package com.example.final_work;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.KeyStore;

public class question5 extends AppCompatActivity
{
    private ImageView IMV_status;
    private TextView TV_title, TVV_question;
    private Button BTN_A, BTN_B, BTN_C, BTN_D;   //四個按鈕分別為A.B.C.D四種答案
    private Toast toast;

    int total_correct;     //使用者的總答對題數
    int correct_answer = 1;  //此題答案為A:三層
    int user_answer;  //使用者的答案

    boolean answered = false;  //判斷使用者是否作答


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question5);

        IMV_status = findViewById(R.id.IMV_status);

        TV_title = findViewById(R.id.TV_title);
        TVV_question = findViewById(R.id.TV_question);

        BTN_A = findViewById(R.id.BTN_A);
        BTN_B = findViewById(R.id.BTN_B);
        BTN_C = findViewById(R.id.BTN_C);
        BTN_D = findViewById(R.id.BTN_D);

        BTN_A.setOnClickListener(process);
        BTN_B.setOnClickListener(process);
        BTN_C.setOnClickListener(process);
        BTN_D.setOnClickListener(process);

        total_correct = getSharedPreferences("database", MODE_PRIVATE).getInt("total_correct", 0);
    }


    Button.OnClickListener process = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.BTN_A)
            {
                user_answer = 1;
            }
            else if (v.getId() == R.id.BTN_B)
            {
                user_answer = 2;
            }
            else if (v.getId() == R.id.BTN_C)
            {
                user_answer = 3;
            }
            else if (v.getId() == R.id.BTN_D)
            {
                user_answer = 4;
            }

            if (check_answer(user_answer) == true)   //使用者答對
            {
                total_correct += 1;
                toast = Toast.makeText(question5.this, "答對!  " +
                        "目前答對題目 / 作答題目  :  " + Integer.toString(total_correct) + " / 5", Toast.LENGTH_SHORT);

            }
            else          //答錯
            {
                toast = Toast.makeText(question5.this, "答錯!  " +
                        "目前答對題目 / 作答題目  :  " + Integer.toString(total_correct) + " / 5", Toast.LENGTH_SHORT);
            }

            SharedPreferences saved_data = getSharedPreferences("database", MODE_PRIVATE); //參數為:檔名  權限
            //儲存資料到檔案
            saved_data.edit().putInt("total_correct", total_correct).commit();

            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            answered = true;
            jump();
        }
    };


    //副程式:
    //判斷使用者是否答對的函數
    public boolean check_answer(int user_answer)
    {
        if (user_answer == correct_answer)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //跳至下一頁面
    public void jump()
    {
        if (answered == true)
        {
            Intent intent = new Intent(question5.this, game_settle.class);
            startActivity(intent);
        }
    }
}