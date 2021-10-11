package com.example.final_work;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class cart extends AppCompatActivity
{
    private TextView TV_title, TV_note, TV_coupon, TV_combo_list, TV_confirm, TV_total_price;

    private Button BTN_continue, BTN_check_out, BTN_clear_cart;

    private Spinner SPIN_coupon;

    String dish_name = "";  //顯示全部餐點的字串
    int total_amount = 0;   //總餐點數量
    int temp = 0;
    int total_price = 0;   //總價格

    String selected_coupon = "";    //使用者選取的優惠券
    String[] coupons = {"(無)", "9折優惠券", "7折優惠券", "1折優惠券"};  //優惠券種類

    private MyDB db = null;    //宣告MyDB資料庫物件


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        TV_title = findViewById(R.id.TV_title);
        TV_note = findViewById(R.id.TV_note);
        TV_coupon = findViewById(R.id.TV_coupon);
        TV_combo_list = findViewById(R.id.TV_combo_list);
        TV_confirm = findViewById(R.id.TV_confirm);
        TV_total_price = findViewById(R.id.TV_total_price);

        BTN_continue = findViewById(R.id.BTN_continue);
        BTN_check_out = findViewById(R.id.BTN_check_out);
        BTN_clear_cart = findViewById(R.id.BTN_clear_cart);

        SPIN_coupon = findViewById(R.id.SPIN_coupon);


        //透過SharedPreferences讀取寫入檔案的重要變數(餐點總數量.餐點總價格.已點的餐點名稱)
        total_amount = getSharedPreferences("database", MODE_PRIVATE).getInt("total_amount", 0);
        temp = getSharedPreferences("database", MODE_PRIVATE).getInt("total_price", 0);
        total_price = temp;  //temp是為了能夠方便存取存放在檔案裡面的數值
        dish_name = getSharedPreferences("database", MODE_PRIVATE).getString("dish_name", "");


        //先設置剛進入頁面要顯示的文字
        if (total_amount == 0)  //使用者沒買任何套餐
        {
            TV_combo_list.setText("您沒有訂購任何套餐");
        }
        else
        {
            TV_combo_list.setText("您一共訂購了" +  Integer.toString(total_amount) + "份套餐");
        }

        TV_total_price.setText("總金額 : " + total_price + "元");

        //spinner
            //建立 ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, coupons);
            //設定spinner顯示的格式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //設定spinner的資料來源
        SPIN_coupon.setAdapter(adapter);
            //設定監聽器  判定所選的物件為何
        SPIN_coupon.setOnItemSelectedListener(judge);


        //建立按鈕監聽器
        BTN_continue.setOnClickListener(back);
        BTN_check_out.setOnClickListener(check_out);
        BTN_clear_cart.setOnClickListener(clear);

        //開啟資料庫
        try{
            db = new MyDB(this);
            db.open();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"無法開啟資料庫" + e,Toast.LENGTH_LONG).show();
        }
    }



    private Spinner.OnItemSelectedListener judge = new AdapterView.OnItemSelectedListener()
    {
        @Override     //判斷選擇到哪個物件
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            selected_coupon = parent.getSelectedItem().toString();
            total_price = temp;   //記得要先重置，不然使用者每換一次優惠券，總價格就會一直減少

            int index = parent.getSelectedItemPosition();
            switch (index)
            {
                case 0:
                {
                    TV_confirm.setText("您沒有使用任何優惠券");
                    TV_total_price.setText("總金額 : " + total_price + "元");
                    break;
                }
                case 1:
                {
                    TV_confirm.setText("您已經使用9折優惠券!");
                    total_price *= 0.9;
                    TV_total_price.setText("總金額 : " + total_price + "元");
                    break;
                }
                case 2:
                {
                    TV_confirm.setText("您已經使用7折優惠券!");
                    total_price *= 0.7;
                    TV_total_price.setText("總金額 : " + total_price + "元");
                    break;
                }
                case 3:
                {
                    TV_confirm.setText("您已經使用1折優惠券!");
                    total_price *= 0.1;
                    TV_total_price.setText("總金額 : " + total_price + "元");
                    break;
                }
            }

        }
        @Override       //沒選任何東西
        public void onNothingSelected(AdapterView<?> parent)
        {
            SPIN_coupon.setSelection(0);   //預設為不使用優惠券
        }
    };


    private Button.OnClickListener back = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.BTN_continue)
            {
                Intent intent = new Intent(cart.this, order.class);
                startActivity(intent);
            }
        }
    };

    private Button.OnClickListener clear = new Button.OnClickListener()  //清空購物車
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.BTN_clear_cart)   //清空購物車
            {
                total_amount = 0;
                temp = 0;
                total_price = temp;
                SPIN_coupon.setSelection(0);

                SharedPreferences saved_data = getSharedPreferences("database", MODE_PRIVATE);
                //儲存資料到檔案
                saved_data.edit().putInt("total_amount", total_amount)
                        .putInt("total_price", total_price)
                        .putString("dish_name", dish_name)
                        .commit();
            }

            TV_combo_list.setText("您沒有訂購任何套餐");
            TV_total_price.setText("總金額 : " + total_price + "元");
        }
    };

    private Button.OnClickListener check_out = new Button.OnClickListener()
    {
        @Override
        //使用者按下結帳按鈕
        public void onClick(View v)
        {
            if (v.getId() == R.id.BTN_check_out)
            {
                if (total_price <= 0)   //使用者沒買任何東西卻按下確認結帳按鈕
                {
                    Toast toast = Toast.makeText(cart.this, "這位客人， 您沒有買任何東西啊...", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);    //快顯置中顯示
                    toast.show();
                }
                else
                {
                    //資料庫處理
                    try
                    {
                        db.append(dish_name, total_amount, total_price);     //將使用者點的餐點資訊新增到訂單資料庫
                    }
                    catch (Exception e)
                    {
                        Toast toast = Toast.makeText(cart.this, "" + e, Toast.LENGTH_LONG);   //顯示錯誤訊息
                        toast.show();
                    }
                    //顯示訊息
                    Toast toast = Toast.makeText(cart.this, "總金額為" + Integer.toString(total_price) +
                            "元， 請等待外送員的到來!!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    //結帳完成後需要把使用者剛才的訂單重置
                    total_amount = 0;
                    temp = 0;
                    total_price = temp;
                    dish_name = "";

                    SharedPreferences saved_data = getSharedPreferences("database", MODE_PRIVATE);
                    //儲存資料到檔案
                    saved_data.edit().putInt("total_amount", total_amount)
                            .putInt("total_price", total_price)
                            .putString("dish_name", dish_name)
                            .commit();

                    //跳轉至訂單頁面
                    Intent intent = new Intent(cart.this, order_hist.class);
                    startActivity(intent);
                }
            }
        }
    };
}
