package com.example.final_work;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class order_hist extends AppCompatActivity
{
    private Button btnAppend,btnEdit,btnDelete,btnClear;
    private EditText edtPrice,edtName,edtAmount;
    private ListView ListView01;

    private MyDB db = null;  //宣告資料庫物件db
    Cursor cursor;     //資料庫會用到的游標物件
    long myid;     //對應到資料庫的id欄位(訂單編號)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_hist);

        ListView01 = findViewById(R.id.ListView01);
        btnAppend = findViewById(R.id.btnAppend);
        btnEdit = findViewById(R.id.btnEdit);
        btnClear = findViewById(R.id.btnClear);
        btnDelete = findViewById(R.id.btnDelete);
        edtName = findViewById(R.id.edtName);
        edtAmount = findViewById(R.id.edtAmount);
        edtPrice = findViewById(R.id.edtPrice);

        btnAppend.setOnClickListener(myListener);
        btnEdit.setOnClickListener(myListener);
        btnDelete.setOnClickListener(myListener);
        btnClear.setOnClickListener(myListener);
        ListView01.setOnItemClickListener(listview01Listener);

        //例外處理:若有出現例外時，程式不會直接當掉，而是會做出對應的處理
        try{
            db = new MyDB(this);      //創建資料庫物件
            db.open();                    //開啟資料庫
            //一開始預設顯示所有資料
            cursor = db.getAll();      //查詢所有資料
            UpdateAdapter(cursor);     //載入資料表至ListView中並顯示

        }
        catch (Exception e)   //出現例外，捕捉到例外物件
        {
            Toast.makeText(getApplicationContext(),"無法開啟資料庫" + e,Toast.LENGTH_LONG).show(); //跳出訊息告訴使用者哪裡出了問題
        }


    }

    private ListView.OnItemClickListener listview01Listener=     //當選取到listview裡面的東西時，做出對應的動作
            new ListView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    ShowData(id);    //(自創建函數)顯示出所選到的訂單
                    cursor.moveToPosition(position);
                }
            };

    private void ShowData(long id)    //顯示出所選到的訂單
    {
        Cursor c = db.get(id);    //查詢指定id的資料，並將資料指派給游標
        myid=id;
        //餐點名稱.總數量.總價格分別對應到資料表的第2.3.4欄位
        edtName.setText(c.getString(1));
        edtAmount.setText(""+c.getInt(2));
        edtPrice.setText(""+c.getInt(3));
    }

    protected void onDestroy()
    {
        super.onDestroy();
        db.close();   // 關閉資料庫
    }
    private Button.OnClickListener myListener = new Button.OnClickListener()

    {

        @Override
        public void onClick(View v)
        {
            try
            {
                switch (v.getId())
                {
                    case R.id.btnAppend:     //新增資料至資料庫
                    {
                        int price = Integer.parseInt(edtPrice.getText().toString());
                        int amount = Integer.parseInt(edtAmount.getText().toString());
                        String name = edtName.getText().toString();
                        if(db.append(name, amount, price) > 0)
                        {
                            //若成功新增，就更新並透過ListView顯示
                            cursor = db.getAll();
                            UpdateAdapter(cursor);
                            ClearEdit();
                        }
                        break;
                    }
                    case R.id.btnEdit:     //修改資料庫裡的資料
                    {
                        int price = Integer.parseInt(edtPrice.getText().toString());
                        int amount = Integer.parseInt(edtAmount.getText().toString());
                        String name = edtName.getText().toString();
                        if(db.update(myid,name,amount,price))
                        {
                            //若成功修改，就更新並透過ListView顯示
                            cursor=db.getAll();
                            UpdateAdapter(cursor);
                        }
                        break;
                    }
                    case R.id.btnDelete:  //刪除資料庫裡的資料
                    {
                        if(cursor!=null&&cursor.getCount()>=0)
                        {
                            //設定對話方塊，讓使用者確認
                            AlertDialog.Builder builder = new AlertDialog.Builder(order_hist.this);
                            builder.setTitle("確定刪除");
                            builder.setMessage("確定要刪除\n訂單編號" + myid + "這筆資料?");
                            builder.setNegativeButton("確定", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int i)
                                {
                                    if(db.delete(myid))
                                    {
                                        //若成功刪除，就更新並透過ListView顯示
                                        cursor=db.getAll();
                                        UpdateAdapter(cursor);
                                        ClearEdit();
                                    }
                                }
                            });
                            builder.setPositiveButton("取消",new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                }

                            });
                            builder.show();
                        }
                        break;
                    }
                    case R.id.btnClear:   //清除
                    {
                        ClearEdit();
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"資料不正確!" + e,Toast.LENGTH_SHORT).show();
            }

        }
    };
    public void ClearEdit()   //清空edittext文字方塊裡面的內容
    {
        edtName.setText("");
        edtAmount.setText("");
        edtPrice.setText("");

    }

    //Adapter (n.) 適配器; 多功能插頭; 改編者
    //在Android的世界裡，Adapter是Database和UI（View）之間一個重要的橋樑，用於連接後端資料和前端介面
    //我們常用的View，包括ListView,GridView, Spinner等，常常伴隨著Adapter的使用
    public void UpdateAdapter(Cursor cursor)
    {
        if(cursor != null && cursor.getCount() >= 0)
        {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.activity_layout_ex,  //自己創建的layout
                    cursor,
                    new String[]{"_id","name","amount","price"},  //四個欄位的鍵(key)
                    new int[]{R.id.txtId,R.id.txtName,R.id.txtAmount,R.id.txtPrice},0);  //四個欄位的值(value)要放進layout裡的元件
            ListView01.setAdapter(adapter);   //將adapter增加到listview中
        }
    }

}
