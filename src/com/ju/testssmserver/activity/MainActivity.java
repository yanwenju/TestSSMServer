package com.ju.testssmserver.activity;

import com.ju.testssmserver.R;
import com.ju.testssmserver.R.id;
import com.ju.testssmserver.R.layout;
import com.ju.testssmserver.R.menu;
import com.ju.testssmserver.model.UserInfoModel;
import com.ju.testssmserver.object.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView userIDTV;
	private TextView userNameTV;
	private TextView passwordTV;
	private Button getBtn;
	
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		userIDTV = (TextView) findViewById(R.id.userIDTV);
		userNameTV = (TextView) findViewById(R.id.userNameTV);
		passwordTV = (TextView) findViewById(R.id.passwordTV);
		getBtn = (Button)findViewById(R.id.getBtn);
		
		intent= this.getIntent();
	
		getBtn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				initActivity();
				
			}
		});
		
	}
	
	private void initActivity(){
		/**
		 * Handler
		 * @note 用于子线程和主线程之间的数据传递
		 * 解决了andr4.0以上主线程无法访问网络的问题。
		 */
		final Handler myhandler = new Handler() {
			public void handleMessage(Message msg) {
				//isNetError = msg.getData().getBoolean("isNetError");
				if(msg.what==0x123)
				{
					userIDTV.setText(String.valueOf(msg.getData().getInt("userID")));
					userNameTV.setText(msg.getData().getString("userName"));
					passwordTV.setText(msg.getData().getString("password"));
				}
			}
		};
	
		/**
		 * 新建子线程
		 * @note 用于建立网络连接，从服务器获取IMEI对应的用户信息
		 * 解决了andr4.0以上主线程无法访问网络的问题。
		 */
		new Thread(){
			public void run(){
				UserInfoModel userInfoModel = new UserInfoModel();		
				User user = new User();
				
				try {
					user = userInfoModel.getUserInfo(1);
					
					if(user == null)
					{
						myhandler.post(new Runnable(){
	                            public void run(){
	                            showDialog("数据库中未查找到你的相关信息！请及时注册！");
	                            }
						});
					}
					else
					{
						Bundle bundle = new Bundle();		
						Message message = new Message();
						
						bundle.putInt("userID", user.getUserID());
						bundle.putString("userName", user.getUserName());
						bundle.putString("password", user.getPassword());

						message.setData(bundle);
						message.what=0x123;			
						myhandler.sendMessage(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	
	/**
	 * 对话框显示函数
	 * 
	 * @param message是要显示的内容
	 */
	private void showDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity.this);
		builder.setMessage(message);

		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				MainActivity.this.setResult(RESULT_OK, intent);
				MainActivity.this.finish();  
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
