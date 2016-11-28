package com.example.memento;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDatabaseHelper extends SQLiteOpenHelper {
     final String CREATE_TABLE_SQL="create table memento_tb(_id integer primary"+" key autoincrement,subject,body,date)";//����memento���SQL���

     public MyDatabaseHelper(Context context,String name,CursorFactory factory,int version){
    	 super(context,name,factory,version);//���췽��
     }
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE_SQL);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
       System.out.println("--------------------------------"+oldVersion+"---------------------------->"+newVersion);

	}
	

	

}
