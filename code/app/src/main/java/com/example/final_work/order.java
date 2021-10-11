package com.example.final_work;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class order extends AppCompatActivity
{

    //介面元件
    private TextView TV_price, TV_mainDish, TV_drink, TV_amount;
    private ImageView IMV_food;
    private Button BTN_minus, BTN_plus, BTN_purchase, BTN_checkout, BTN_back;

    private RadioGroup RG_mainDish, RG_drink;

    private RadioButton dish1, dish2, dish3, drink1, drink2, drink3;


    //宣告變數
    String dish_name = "";  //顯示全部餐點的字串
    int total_amount = 0;  //訂單總數量
    int total_price = 0; //訂單總價錢

    String dish = "", drink = "";  //使用者選取的主菜  飲料 (記得要先初始化  指派空字串給變數)
    int amount = 1;  //欲加入的餐點數量

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //連結
        TV_price = findViewById(R.id.TV_price);
        TV_mainDish = findViewById(R.id.TV_mainDish);
        TV_drink = findViewById(R.id.TV_drink);
        TV_amount = findViewById(R.id.TV_amount);

        IMV_food = findViewById(R.id.IMV_food);

        BTN_minus = findViewById(R.id.BTN_minus);
        BTN_plus = findViewById(R.id.BTN_plus);
        BTN_purchase = findViewById(R.id.BTN_add_to_cart);
        BTN_checkout = findViewById(R.id.BTN_check_cart);
        BTN_back = findViewById(R.id.BTN_back);

        RG_mainDish = findViewById(R.id.RG_mainDish);
        RG_drink = findViewById(R.id.RG_drink);

        dish1 = findViewById(R.id.RB_dish1);
        dish2 = findViewById(R.id.RB_dish2);
        dish3 = findViewById(R.id.RB_dish3);
        drink1 = findViewById(R.id.RB_drink1);
        drink2 = findViewById(R.id.RB_drink2);
        drink3 = findViewById(R.id.RB_drink3);

        //若是使用者從購物車頁面跳轉回點餐頁面繼續點餐，需要記下使用者之前的點餐紀錄
        //因此需要讀取設定檔內的資料 (使用SharedPreferences的方法讀取先前已寫入的設定值，若沒讀取到則預設為0)
        total_amount = getSharedPreferences("database", MODE_PRIVATE).getInt("total_amount", 0);
        total_price = getSharedPreferences("database", MODE_PRIVATE).getInt("total_price", 0);
        dish_name = getSharedPreferences("database", MODE_PRIVATE).getString("dish_name", "");

        //設定共用Listener事件
            //Button
                //餐點數量調整的按鈕
        BTN_minus.setOnClickListener(choose_listener);
        BTN_plus.setOnClickListener(choose_listener);
                //加入購物車與結帳的按鈕
        BTN_purchase.setOnClickListener(buy_listener);
        BTN_checkout.setOnClickListener(buy_listener);
                //轉換頁面
        BTN_back.setOnClickListener(back);

            //RadioGroup
                //主食
        RG_mainDish.setOnCheckedChangeListener(mainDish_listener);
                //飲料
        RG_drink.setOnCheckedChangeListener(drink_listener);

    }

    //建立Listener
        //匿名類別的寫法
    private Button.OnClickListener choose_listener = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.BTN_plus)
            {
                amount++;
            }
            else if (v.getId() == R.id.BTN_minus)
            {
                amount--;
                if (amount <= 0)   //不管怎樣預設就是加入一份餐點,不能再低
                {
                    amount = 1;
                }
            }
            TV_amount.setText(Integer.toString(amount));
        }
    };

    private Button.OnClickListener buy_listener = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            //加入快顯訊息
            Toast alert = Toast.makeText(order.this, "", Toast.LENGTH_SHORT);
            alert.setGravity(0, 0, 0);


            //使用者按下加入購物車按鈕
            if (v.getId() == R.id.BTN_add_to_cart)
            {
                //首先判斷使用者有沒有確實選擇好套餐
                //因為後面程式我有把字串指派為radio button所選取的商品，所以若字串為空即代表使用者沒有選擇
                if (dish.matches("") | drink.matches(""))
                {
                    alert.setText("請確實選擇主餐及飲料!!");
                    alert.show();
                }
                else
                {
                //使用者確實選好了套餐
                //提醒使用者已經將餐點加入購物車
                //提示對話方塊
                AlertDialog.Builder confirm = new AlertDialog.Builder(order.this);
                confirm.setTitle("確認視窗")
                        .setMessage("確定要將" + dish + "及" + drink + "套餐，共" + amount + "份，加入購物車嗎？")
                        .setPositiveButton("取消", new DialogInterface.OnClickListener()
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
                                total_price += (150 * amount);   //計算價格
                                total_amount += amount;    //計算總數量
                                dish_name += dish + "及" + drink + "套餐共" + Integer.toString(amount) + "份\n";   //為了讓資料庫顯示餐點，需要把全部餐點記下來
                                amount = 1;         //重置餐點數量
                                TV_amount.setText(Integer.toString(amount));

                                //清空使用者上次選擇的品項
                                clear_radio(RG_mainDish);
                                dish = ""; //因為前面是用這個字串判別使用者有沒有選擇品項，所以要重新設回空字串

                                clear_radio(RG_drink);
                                drink = "";
                            }
                        }).show();


                TV_amount.setText(Integer.toString(amount));
                }
            }

            //使用者按下檢視購物車按鈕
            if (v.getId() == R.id.BTN_check_cart)
            {
                //因為有頁面來回跳轉，若用bundle來回傳遞會很麻煩，所以使用SharedPreferences類別直接將數值寫入檔案
                //透過此類別，讓APP在下一次執行時可讀取到這些上一次儲存下來的資料 (資料的儲存格式是XML檔)

                //產生一個檔名為database.xml的設定儲存檔，並只供本專案(app)可讀取
                SharedPreferences saved_data = getSharedPreferences("database", MODE_PRIVATE); //參數為:檔名  權限
                //儲存資料到檔案
                saved_data.edit().putInt("total_amount", total_amount)
                                .putInt("total_price", total_price)
                                .putString("dish_name", dish_name)
                                .commit();

                Intent intent = new Intent(order.this, cart.class);
                startActivity(intent);

            }
        }
    };

    private Button.OnClickListener back = new Button.OnClickListener()           //使用者按下返回主畫面按鈕
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.BTN_back)
            {
                Intent intent = new Intent(order.this, MainActivity.class);
                startActivity(intent);
            }
        }
    };

    //判斷使用者選取哪種主餐
    private RadioGroup.OnCheckedChangeListener mainDish_listener = new RadioGroup.OnCheckedChangeListener()
    {
        //檢查主餐的哪個元素被選取
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            if (dish1.isChecked())
            {
                dish = dish1.getText().toString();
            }
            if (dish2.isChecked())
            {
                dish = dish2.getText().toString();
            }
            if (dish3.isChecked())
            {
                dish = dish3.getText().toString();
            }
        }
    };

    //判斷使用者選取哪種飲料
    private RadioGroup.OnCheckedChangeListener drink_listener = new RadioGroup.OnCheckedChangeListener()
    {
        //檢查飲料的哪個元素被選取
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            if (drink1.isChecked())
            {
                drink = drink1.getText().toString();
            }
            if (drink2.isChecked())
            {
                drink = drink2.getText().toString();
            }
            if (drink3.isChecked())
            {
                drink = drink3.getText().toString();
            }
        }
    };

    private void clear_radio(RadioGroup radioGroup)    //清空使用者已經選中的radio button
    {
        radioGroup.clearCheck();
    }

}












