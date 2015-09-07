package com.cqnu.chenchw;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class Edit extends FinalActivity{

	@ViewInject(id=R.id.NoteTitle) EditText eTextTitle; 
	@ViewInject(id=R.id.NoteContent) EditText eTextContent; 
	@ViewInject(id=R.id.NoteDate) TextView eTextDate; 
	@ViewInject(id=R.id.Weather) TextView eTextWeather; 
	@ViewInject(id=R.id.Back,click="btnBackclick") Button btnBack; 
	@ViewInject(id=R.id.Delete,click="btnDeleteclick") Button btnDelete; 

	ArrayList<String> edits=new ArrayList<String>();
	ArrayList<String> weathers=new ArrayList<String>();
	List<Notes> mListViewData = new ArrayList<Notes>();
	FinalDb db;
	//重庆当前时间的天气
	private final static String url = "http://www.weather.com.cn/data/cityinfo/101040100.html";
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		super.setTitle("添加日记");
		db = FinalDb.create(this);
		setContentView(R.layout.editor);
		edits=this.getIntent().getStringArrayListExtra("data");
		getWeather();
		if(!(edits==null))
		{
			eTextTitle.setText(edits.get(1).toString());
			eTextContent.setText(edits.get(2));
			eTextDate.setText(edits.get(3));
			eTextWeather.setText(edits.get(4));
		}
		else{
			eTextTitle.setText("");
			eTextContent.setText("");
			eTextDate.setText("");
			eTextWeather.setText("");
		}
	}


	//新增、修改
	@SuppressLint("SimpleDateFormat")
	public void btnBackclick(View v){
		String title=eTextTitle.getText().toString().trim();
		String mainContent=eTextContent.getText().toString().trim();

		if(!(edits==null))
		{
			if(title.equals("") && mainContent.equals("")){
				setAlertDialog("警告","标题、内容不能为空！");
			}
			else{
				Notes notes=db.findById(edits.get(0), Notes.class);
				notes.setTitle(title);
				notes.setContentDetail(mainContent);

				db.update(notes);
				setDialogActivity("温馨提示！","信息已保存！");
			}
		}
		else {//新建
			//获取当前时间
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
			Date curDate = new Date(System.currentTimeMillis());
			String date = formatter.format(curDate); 
			//获取网络天气
			
			String Weather=eTextWeather.getText().toString().trim();
			if(title.equals("") && mainContent.equals("") ){
				setAlertDialog("警告","标题、内容不能为空！");
			}
			else
			{
				Notes notes=new Notes();
				notes.setTitle(title);
				notes.setContentDetail(mainContent);
				notes.setDate(date);
				notes.setWeather(Weather);
				db.save(notes);

				setDialogActivity("温馨提示！","信息已保存！");
			}
		}
	}

	//删除信息
	public void btnDeleteclick(View v){
		if(eTextTitle.getText().toString()==""&&eTextContent.getText().toString().equals(null)){
			setAlertDialog("温馨提示！","没有可删除的信息！");
		}
		else{
			setDialogActivityDelete("警告！","确认删除信息吗？");
		}
	}


	private void showToast(String strMsg) {
		Toast.makeText(this, strMsg, 0).show();
	}
	//弹出警告对话框
	public void setAlertDialog(String title,String message){
		AlertDialog.Builder builder=new Builder(Edit.this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int which){
				dialog.dismiss();
			}
		});
		builder.show();
	}
	//自定义弹出对话框并跳转至其他活动程序
	public void setDialogActivity(String title,String message){
		AlertDialog.Builder builder=new Builder(Edit.this);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int which){
				dialog.dismiss();
				Intent intent=new Intent(Edit.this,NoteBookActivity.class);
				startActivity(intent);
				Edit.this.finish();
			}

		});
		builder.show();
	}

	//判断是否删除信息
	public void setDialogActivityDelete(String title,String message){
		AlertDialog.Builder builder=new Builder(Edit.this);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int which){
				//	Notes notes=db.findById(edits.get(0), Notes.class);
				db.deleteById(Notes.class,edits.get(0));
				showToast("删除成功");
				dialog.dismiss();
				Intent intent=new Intent(Edit.this,NoteBookActivity.class);
				startActivity(intent);
				Edit.this.finish();
			}

		});
		builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.show();

	}



	public void getWeather(){
		FinalHttp fh = new FinalHttp();
		AjaxParams params=new AjaxParams();
		fh.post(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				getWeather(t);
				
			}
//			public void onLoading(long count,long current){
//
//			}
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				showToast("获取失败");
			}
		});
		
	}


	public void getWeather(String t){
		String [] temp1=null,temp2=null;
		temp1=t.split(",");
		temp2=temp1[4].split(":");
		//showToast(temp2[1]);
		eTextWeather.setText(temp2[1]);
	}


}




