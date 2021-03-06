package com.android.daka;


import com.android.daka.fragments.DragGridFragment;
import com.android.daka.fragments.DragListFragment;
import com.android.daka.fragments.MainFragment;
import com.android.daka.fragments.MyFragments;
import com.android.daka.utils.ActivityUtils;
import com.android.daka.utils.SystemUtils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.os.Build;

public class DakaActivity extends Activity {
	static final String TAG = Config.TAG_APP+"DakaActivity";
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate() 111 ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daka);
		SystemUtils.initContext(this);
		//MainFragment,DragGridFragment
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new MainFragment()).commit();
		}
		ActivityUtils mActivityUtils = new ActivityUtils(this);
//		mActivityUtils.getRunningAppProcesses();
		//mActivityUtils.isInLauncher("com.android.contacts.ContactsApplication");
//		mActivityUtils.isInLauncher("com.android.systemui"
//				,"com.android.systemui.recent.RecentsActivity");
		//person.put("", value)
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.daka, menu);
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onPause()----");
				//"trace:"+Log.getStackTraceString(new Throwable()));
		super.onPause();
	}
	void test(){
		int a=0;
		int c = 1/a;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onDestroy()----");
		super.onDestroy();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onStop()----");
		super.onStop();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onResume()----");
		//test();
		super.onResume();
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onStart()----");
		super.onStart();
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onRestart()----");
		super.onRestart();
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
