package com.iridium.AppLock;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iridium.AppLock.DBUttiles.DBUttiles;

/**
 * @author rajapeela
 * 
 */
public class NewActivity extends Activity {

    private Button submitButton, cancelButton;
    String appName = "";
    private Cursor pawordCursor;
    private EditText passwordFeild;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.main);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.window_title);
        submitButton = (Button) findViewById(R.id.submitButton1);
        passwordFeild = (EditText) findViewById(R.id.passwordField1);
        //cancelButton = (Button) findViewById(R.id.cancleButton1);
        submitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	pawordCursor = DBUttiles.getDBUttile(getApplicationContext())
        				.selectPASSWORD();
            	if (pawordCursor
						.getString(1)
						.toString()
						.equals(passwordFeild.getText().toString()
								.trim())){
            		Cursor appListData = DBUttiles.getDBUttile(getApplicationContext())
                            .selectAllLockApps();
                    String packageName = getIntent().getPackage();
                    ActivityManager am = (ActivityManager) getApplicationContext()
                    .getSystemService(ACTIVITY_SERVICE);
                    //String packageName = am.getRunningTasks(1).get(0).baseActivity
                   // .getPackageName();
                    //Toast.makeText(getApplicationContext(), "Activity Triggrd true"+packageName,3000).show();
                    int i = 0;
                    while (appListData.isAfterLast() == false) {
                        
                        if(packageName.equals(appListData.getString(2))){
                            //Toast.makeText(getApplicationContext(), packageName+"::Inside Cond::"+appListData.getString(2),3000).show();
                            appName = appListData.getString(1);
                            break;
                        }
                                
                        appListData.moveToNext();
                        i++;
                    }

                    appListData.close();
                    DBUttiles.getDBUttile(getApplicationContext()).updateFlage(
                            appName, packageName, "false");                
                    //Toast.makeText(getApplicationContext(), "class Name ", 3000).show();                
                    PackageManager  pmi = getPackageManager();
                    Intent intent = null;
    	            intent = pmi.getLaunchIntentForPackage(packageName);                    
    	            if (intent != null){
    	                startActivity(intent);
    	            }	           
                    System.out.println("event called");          		
            	}else{
            		Toast.makeText(getApplicationContext(), "Enter Correct Password", 3000).show();
            	}
                
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}