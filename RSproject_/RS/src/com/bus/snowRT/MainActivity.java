package com.bus.snowRT;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.bus.snowRT.fragment.ManualMode;
import com.bus.snowRT.fragment.Setting;
import com.bus.snowRT.fragment.MenuFragment;
import com.heima.news.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
    private SlidingMenu sm;
//    private Fragment fragment1;
    private ManualMode mManualMode;
    MenuItem actionConnect; 
	 MenuItem actiondisConnect;

	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setBehindContentView(R.layout.menu);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
   
		sm = getSlidingMenu();
		
		sm.setMode(SlidingMenu.LEFT);
		
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		
		sm.setShadowDrawable(R.drawable.shadow);
	
		sm.setShadowWidth(R.dimen.shadow_width);
		
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		MenuFragment menuFragment = new MenuFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, menuFragment, "Menu").commit();
		setDefaultFragment();
    }
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		actionConnect = menu.findItem(R.id.connect1); 
		actiondisConnect = menu.findItem(R.id.disconnect1); 
		actionConnect.setVisible(true);  
		actiondisConnect.setVisible(false);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		return MenuChoice(item);
	}
	
	private boolean MenuChoice(MenuItem item){
		
		switch(item.getItemId()){
		case R.id.connect1:
			mManualMode.Connect();
			if (mManualMode.Flag == 1) {
			    actionConnect.setVisible(false);  
			    actiondisConnect.setVisible(true);
			}
			else{
				actionConnect.setVisible(true);  
				actiondisConnect.setVisible(false);
			}
            break;
		case R.id.disconnect1:
			mManualMode.disConnect();
			if (mManualMode.Flag == 2){
				actiondisConnect.setVisible(false);
				actionConnect.setVisible(true);
			}
			
        break;
		}
		return false;
	}
	
	private void setDefaultFragment() {  
	        FragmentManager fm = getSupportFragmentManager();  
	        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();  
	        mManualMode = new ManualMode();  
	        transaction.replace(R.id.content_frame, mManualMode);  
	        transaction.commit();  
	  }  

	public void switchContent(Fragment f,int position) {
		
		FragmentManager fm = getSupportFragmentManager();  
	        // 开启Fragment事务  
	    android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction(); 

 	    transaction.replace(R.id.content_frame, f).commit();
	
		sm.toggle();
	}
 
@SuppressWarnings("deprecation")
public boolean onKeyDown(int keyCode, KeyEvent event) {
   if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
       // 确认对话框
       final AlertDialog isExit = new AlertDialog.Builder(this).create();
       isExit.setIcon(android.R.drawable.btn_star_big_off);
       isExit.setTitle(R.string.System_Prompt);
       isExit.setMessage("Are you sure you want to exit?");
       DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
               switch (which) {
               case AlertDialog.BUTTON1:// "确认"按钮退出程序
                    NotificationManager notificationManager = (NotificationManager) MainActivity.this 
                       .getSystemService(NOTIFICATION_SERVICE); 
                       notificationManager.cancel(0); 
                       System.exit(0); 
                   break;
               case AlertDialog.BUTTON2:// "取消"第二个按钮取消对话框
                   isExit.cancel();
                   break;
               default:
                   break;
               }
           }
       };
       // 注册监听
       isExit.setButton("YES", listener);
       isExit.setButton2("NO", listener);
       // 显示对话框
       isExit.show();
       return false;
   }
   return false;
 }
}

