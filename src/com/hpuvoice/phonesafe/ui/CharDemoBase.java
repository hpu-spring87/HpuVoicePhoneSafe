package com.hpuvoice.phonesafe.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hpuvoice.phonesafe.R;
public class CharDemoBase extends FragmentActivity{

	
    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
    
    @Override
    protected void onCreate(Bundle arg0) {
    	super.onCreate(arg0);
    }
}
