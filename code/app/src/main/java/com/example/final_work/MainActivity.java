package com.example.final_work;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private TextView TV_title;
    private Button BTN_order, BTN_game, BTN_call, BTN_map, BTN_history;
    private ImageView IMV_dish;



    //覆寫   處理電話授權結果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == 1)
        {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)  //拒絕授權
            {
                Toast.makeText(this, "沒有取得授權!", Toast.LENGTH_SHORT).show();
                finish();
            }
            else  //接受授權
            {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


    //activity啟動時一些必要的初始化工作
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TV_title = findViewById(R.id.TV_title);

        BTN_order = findViewById(R.id.BTN_order);
        BTN_game = findViewById(R.id.BTN_game);
        BTN_call = findViewById(R.id.BTN_call);
        BTN_map = findViewById(R.id.BTN_map);
        BTN_history = findViewById(R.id.BTN_history);

        //設置頁面跳轉的監聽器
        BTN_order.setOnClickListener(page_change);
        BTN_game.setOnClickListener(page_change);
        BTN_call.setOnClickListener(page_change);
        BTN_map.setOnClickListener(page_change);
        BTN_history.setOnClickListener(page_change);
    }


    private Button.OnClickListener page_change = new Button.OnClickListener()
    {
        //頁面跳轉
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.BTN_order)    //開始點餐
            {
                Intent intent = new Intent(MainActivity.this, order.class);
                startActivity(intent);
            }
            else if (v.getId() == R.id.BTN_game)  //有獎問答
            {
                Intent intent = new Intent(MainActivity.this, game.class);
                startActivity(intent);
            }
            else if (v.getId() == R.id.BTN_call)   //聯絡我們
            {
                Uri uri = Uri.parse("tel:0800-092-000");
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                //在開始撥號之前，需要先檢查權限


                //使用ContextCompat.checkSelfPermission()方法
                //或是使用ActivityCompat.checkSelfPermission()方法
                //(ActivityCompat繼承自ContextCompat)

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    //無權限
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
                else
                {
                    //有權限
                    startActivity(intent);
                }
            }
            else if (v.getId() == R.id.BTN_map)     //找到我們
            {
                Intent intent = new Intent(MainActivity.this, map.class);
                startActivity(intent);
            }
            else if (v.getId() == R.id.BTN_history)     //訂單紀錄
            {
                Intent intent = new Intent(MainActivity.this, order_hist.class);
                startActivity(intent);
            }
        }
    };


}

