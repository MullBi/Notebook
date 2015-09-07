package com.cqnu.chenchw;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Context;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NoteBookActivity extends FinalActivity {
	@ViewInject(id=R.id.add,click="btnclick")Button add_note;
	@ViewInject(id=R.id.LvTitle,itemClick="itemClick",itemLongClick="itemLongClick") ListView lvTitle;

	List<Notes> mListViewData=new ArrayList<Notes>();
	ArrayList<String> edits=new ArrayList<String>();//传输编辑数据
	FinalDb db;
	//
	/*	private  ListView lvTitle;//显示当前日期、标题，单击某一行查看详细信息，长按可修改和删除该行信息
	//private Button add_note;//添加记事本
	private DBOpenHelper dbHelper;//数据库
	private Cursor cursor;//光标
	Map<String,Object> item;
	//	private List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		db=FinalDb.create(this);
		Fresh();
	}



	//单击标题删除

	public void itemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		new AlertDialog.Builder(NoteBookActivity.this).setTitle("提示")
		.setMessage("是否确定删除选中记录")
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Notes notes =mListViewData.get(position);
				//showToast(""+notes.getId());
				try{//删除
					db.deleteById(Notes.class, notes.getId());
					//db.delete(notes);	
					Fresh();
					showToast("删除成功");


				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).setNegativeButton("取消",null).show();

	}


	//长按相应菜单
	public void itemLongClick(AdapterView<?> parent, View view, final int position, long id){


		Notes read = mListViewData.get(position);
		edits.clear();
		edits.add(0,""+read.getId());
		edits.add(1,read.getTitle());
		edits.add(2,read.getContentDetail());
		edits.add(3,read.getDate());
		edits.add(4,read.getWeather());

		new AlertDialog.Builder(NoteBookActivity.this)
		.setTitle("选择功能")
		.setItems(R.array.choose, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String[] check=getResources().getStringArray(R.array.choose);


				if(check[which].equals("查看")){
					showToast("\n时间："+edits.get(3)+"\n天气："+edits.get(4)
							+"\n标题："+edits.get(1)+"\n内容："+edits.get(2));

				}
				else if(check[which].equals("修改")){
					Intent intent=new Intent(NoteBookActivity.this,Edit.class);
					intent.putStringArrayListExtra("data", edits);
					NoteBookActivity.this.startActivityForResult(intent, 30);
					NoteBookActivity.this.finish();
				}
				else if(check[which].equals("删除")){
					try{//删除
						db.deleteById(Notes.class, edits.get(0));
						//db.delete(notes);	
						Fresh();
						showToast("删除成功");
					}catch(Exception e){
						e.printStackTrace();
					}
				}


			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		}).create().show();
	}
	//新建记事本

	public void btnclick(View v) {
		Intent intent=new Intent(NoteBookActivity.this,Edit.class);
		startActivity(intent);
		NoteBookActivity.this.finish();
	}
	@SuppressLint("ShowToast")
	private void showToast(String strMsg) {
		// TODO Auto-generated method stub
		Toast.makeText(this, strMsg, 0).show();

	}
	private void Fresh() {
		// TODO Auto-generated method stub

		mListViewData.clear();
		mListViewData.addAll(db.findAll(Notes.class));
		mListAdapter.notifyDataSetChanged();
		lvTitle.setAdapter(mListAdapter);

	}
	private BaseAdapter mListAdapter=new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View item=View.inflate(NoteBookActivity.this, R.layout.item, null);
			TextView weather = (TextView) item.findViewById(R.id.Weather);
			TextView title = (TextView) item.findViewById(R.id.Title);
			TextView date = (TextView) item.findViewById(R.id.Date);

			Notes notes=mListViewData.get(position);
			weather.setText("天气："+notes.getWeather());
			title.setText("标题："+notes.getTitle());
			date.setText("日期："+notes.getDate());

			return item;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mListViewData.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListViewData.size();
		}
	};


























	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}