package com.example.final_work;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity
{
    private TextView TV_account, TV_password;
    private EditText ET_account, ET_password;
    private Button BTN_login, BTN_cancel, BTN_hint;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TV_account = findViewById(R.id.TV_account);
        TV_password = findViewById(R.id.TV_password);

        ET_account = findViewById(R.id.ET_account);
        ET_password = findViewById(R.id.ET_password);

        BTN_login = findViewById(R.id.BTN_login);
        BTN_cancel = findViewById(R.id.BTN_cancel);
        BTN_hint = findViewById(R.id.BTN_hint);

        BTN_login.setOnClickListener(login_process);
        BTN_cancel.setOnClickListener(login_process);
        BTN_hint.setOnClickListener(login_process);

    }


    private Button.OnClickListener login_process = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.BTN_login)  //按下登入按鈕
            {
                //檢查帳號密碼欄位是否有空值
                if (ET_account.getText().toString().matches("")  | ET_password.getText().toString().matches("") )
                {
                    Toast toast = Toast.makeText(login.this, "帳號以及密碼欄位不能為空!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);    //快顯置中顯示
                    toast.show();
                }
                //使用者帳號和密碼都有輸入
                else
                {
                    if (ET_account.getText().toString().matches("cjcu") & ET_password.getText().toString().matches("android"))
                    {
                        //登入成功
                        Toast toast = Toast.makeText(login.this, "登入成功!!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);    //快顯置中顯示
                        toast.show();

                        //跳轉至主頁面
                        Intent intent = new Intent(login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        //登入失敗
                        Toast toast = Toast.makeText(login.this, "登入失敗，請確認帳號密碼輸入無誤", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);    //快顯置中顯示
                        toast.show();
                        //清空使用者先前輸入的帳號密碼
                        ET_account.setText("");
                        ET_password.setText("");
                    }
                }
            }

            else if (v.getId() == R.id.BTN_cancel) //按下取消按鈕
            {
                //提示對話方塊
                AlertDialog.Builder confirm = new AlertDialog.Builder(login.this);
                confirm.setTitle("確認視窗")
                        .setMessage("確定要取消登入嗎?")
                        .setPositiveButton("回到登入頁面", new DialogInterface.OnClickListener()
                        {
                            //按下取消選項
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        })
                        .setNegativeButton("確定", new DialogInterface.OnClickListener()
                        {
                            //按下確定選項
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                finish();
                            }
                        }).show();

            }

            else if (v.getId() == R.id.BTN_hint) //按下密碼提示按鈕
            {
                //快顯訊息
                Toast toast = Toast.makeText(login.this, "帳號 : cjcu  密碼 : android", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);    //快顯置中顯示
                toast.show();
            }
        }
    };

}

