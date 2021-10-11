package com.example.final_work;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MyDB
{

    public SQLiteDatabase db = null;

    //建立database名稱、talble資料表名稱
    //含有自動增加的_id、餐點名稱，數量，價格 的資料表欄位
    private final static String DATABASE_NAME ="db1.db";
    private final static String TABLE_NAME ="table01";
    private final static String _ID ="_id";
    private final static String NAME ="name";
    private final static String AMOUNT ="amount";
    private final  static  String PRICE ="price";

    //建立資料表的欄位
    private final static String CREATE_TABLE =  " CREATE TABLE " + TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY,"
            + NAME +" TEXT,"+ AMOUNT +" INTEGER," + PRICE + " INTEGER)";

    //建構Context物件，設定mCtx為全域變數
    private Context mCtx = null;

    //傳入Contexth參數ctx 並指派給全域變數 mCtx(代表建立此物件的cart)
    public MyDB(Context ctx)
    {
        this.mCtx = ctx;
    }

    //開啟已經存在的資料庫
    public void open() throws SQLException
    {
        db = mCtx.openOrCreateDatabase(DATABASE_NAME ,0,null);
        try
        {
            db.execSQL(CREATE_TABLE);
        }
        catch (Exception e)
        {

        }
    }

    //關閉資料庫
    public void close()
    {
        db.close();
    }


    //查詢所有資料，只取_ID ,NAME ,AMOUNT ,PRICE欄位
    public Cursor getAll()
    {

        return db.query(TABLE_NAME ,new String[]{ _ID ,NAME ,AMOUNT ,PRICE},
                null,null,null,null,null,null);

    }

    //查詢指定ID的資料，只取_ID ,NAME ,AMOUNT ,PRICE欄位
    public Cursor get(long rowId) throws SQLException
    {
        Cursor mCursor = db.query(TABLE_NAME ,new String[]{_ID ,NAME , AMOUNT , PRICE},
                _ID +"="+rowId,null,null,null,null,null);

        if(mCursor!=null)
        {
            //將Cursor移動到第一筆
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //新增一筆資料，其中_ID是自動編號，因此只需要NAME ,AMOUNT ,PRICE欄位
    public long append(String name,int amount,int price)
    {
        ContentValues args = new ContentValues();
        args.put(NAME ,name);
        args.put(AMOUNT ,amount);
        args.put(PRICE ,price);
        return db.insert(TABLE_NAME ,null,args);
    }

    //刪除指定_ID的資料
    public boolean delete(long rowId)
    {
        return db.delete(TABLE_NAME ,_ID + "=" + rowId,null)>0;
    }

    //更新指定_ID的資料
    public boolean update(long rowId,String name,int amount,int price)
    {
        ContentValues args=new ContentValues();
        args.put(NAME ,name);
        args.put(AMOUNT ,amount);
        args.put(PRICE ,price);
        return  db.update(TABLE_NAME ,args,_ID + "=" + rowId,null) > 0;
    }
}