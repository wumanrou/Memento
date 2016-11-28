package com.example.memento;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Layout;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button chooseDate,add,query;
    private EditText date,subject,body;
    private  ListView result;
    private LinearLayout title;
    MyDatabaseHelper mydbHelper;
	@Override
	public  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		chooseDate = (Button) findViewById(R.id.chooseDate);
		add = (Button) findViewById(R.id.add);
		query = (Button) findViewById(R.id.query);
		date = (EditText) findViewById(R.id.date);
		subject = (EditText) findViewById(R.id.subject);
		body = (EditText) findViewById(R.id.body);
		result = (ListView) findViewById(R.id.result);
		title=(LinearLayout)findViewById(R.id.title);
		title.setVisibility(View.INVISIBLE);

		chooseDate.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Calendar c=Calendar.getInstance();//获取当前日期
				new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
					

					//日期选择器对话框,日期改变监听器
				//设置文本编辑框内容为设置的日期，month需要。从0开始，所以月份为month+1
					@Override
					public void onDateSet(DatePicker view, int year,int month,int day) {
						// TODO Auto-generated method stub
						date.setText(year+"-"+(month+1)+"-"+day);
					}
				}, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		MyOnClickListener myOnClickListener=new MyOnClickListener();
		add.setOnClickListener(myOnClickListener);
		query.setOnClickListener(myOnClickListener);
	}
	private class MyOnClickListener implements OnClickListener{

		public void onClick(View v){
			mydbHelper=new MyDatabaseHelper(MainActivity.this,"memento.db",null,1);
			//创建数据库辅助类
			SQLiteDatabase db=mydbHelper.getReadableDatabase();
			//获取SQLite数据库

			String subStr=subject.getText().toString();//获取主题编辑框的内容
			String bodyStr=body.getText().toString();//获取内容编辑框的内容
			String dateStr=date.getText().toString();//获取时间编辑框的内容
			
			
			switch(v.getId()){
			case R.id.add:
			
				title.setVisibility(View.INVISIBLE);//设置表头不可见
				addMemento(db,subStr,bodyStr,dateStr);//调用添加记录方法
				Toast.makeText(MainActivity.this, "添加备忘录成功", 1000).show();
				result.setAdapter(null);//下拉列表内容为空
				break;
			case R.id.query://单击的是查询按钮
				title.setVisibility(View.VISIBLE);
				Cursor cursor=queryMemento(db,subStr,bodyStr,dateStr);
				//调用查询方法
				SimpleCursorAdapter resultAdapter = new SimpleCursorAdapter(
						MainActivity.this, R.layout.result, cursor,
						new String[] { "_id", "subject", "body", "date" },
						new int[] { R.id.memento_num, R.id.memento_subject,
								R.id.memento_body, R.id.memento_date });

				result.setAdapter(resultAdapter);//设置下拉列表的内容
				break;
				default:
					break;
			}
		  }
	}
		public void addMemento(SQLiteDatabase db, String subject,
			String body, String date) {
		// TODO Auto-generated method stub
		db.execSQL("insert into memento_tb values(null,?,?,?)",new String[]{
				subject,body,date});//执行插入操作
		this.subject.setText("");
		//添加数据后，将所有的文本编辑框的内容设为空
		this.body.setText("");
		this.date.setText("");
	}
		public Cursor queryMemento(SQLiteDatabase db,String subject,String body,String date){
			Cursor cursor=db.rawQuery("select * from memento_tb where subject like ? and body like ? and date like ? ",
				new String[]{"%"	+subject+"%","%"+body+"%","%"+date+"%"});//执行查询操作，提供模糊查询功能
			return cursor;
	}
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
  
		protected void onDestory(){
			if(mydbHelper!=null){
				mydbHelper.close();
			}
		}

}
