package com.cqnu.chenchw.Size;

import com.cqnu.chenchw.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ChangeSize extends Activity{
	//遍历设置字体  
	public static void changeViewSize(ViewGroup viewGroup,int screenWidth,int screenHeight) {//传入Activity顶层Layout,屏幕宽,屏幕高  
	        int adjustFontSize = adjustFontSize(screenWidth,screenHeight); 
	        for(int i = 0; i<viewGroup.getChildCount(); i++ ){ 
	            View v = viewGroup.getChildAt(i); 
	            if(v instanceof ViewGroup){ 
	                changeViewSize((ViewGroup)v,screenWidth,screenHeight); 
	            }else if(v instanceof Button){//按钮加大这个一定要放在TextView上面，因为Button也继承了TextView  
	                ( (Button)v ).setTextSize(adjustFontSize+2); 
	            }else if(v instanceof TextView){ 
	                if(v.getId()== R.id.NoteTitle){//顶部标题  
	                    ( (TextView)v ).setTextSize(adjustFontSize+4); 
	                }else{ 
	                    ( (TextView)v ).setTextSize(adjustFontSize); 
	                } 
	            } 
	        } 
	    }

	private static int adjustFontSize(int screenWidth, int screenHeight) {
		// TODO Auto-generated method stub
		return 0;
	} 


}
