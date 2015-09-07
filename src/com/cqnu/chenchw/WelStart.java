package com.cqnu.chenchw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelStart extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		new Handler().postDelayed(new Runnable(){
			public void run(){
				// TODO Auto-generated method stub
				Intent intent=new Intent(WelStart.this,NoteBookActivity.class);
				WelStart.this.startActivity(intent);
				WelStart.this.finish();
			}
		}, 2000);
	}
}

