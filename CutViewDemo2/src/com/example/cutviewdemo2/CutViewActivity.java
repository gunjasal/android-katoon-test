package com.example.cutviewdemo2;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class CutViewActivity extends Activity {

	
	WebCutView cutView ;
	LinearLayout navBar;
	SeekBar seekBar;
	TextView pageText;
	
	int cutLen;
	int curPos;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {	
        super.onCreate(savedInstanceState);

////////////////////////////////
//        cutView as whole screen
//       
//        getWindow().requestFeature(Window.FEATURE_PROGRESS);
//        cutView = new WebCutView(this);
//        cutView.setWebViewClient(new WebViewClient());
//        setContentView(cutView);
//        cutView.getSettings().setJavaScriptEnabled(true); 
//        
//        final Activity activity = this;
//        cutView.setWebChromeClient(new WebChromeClient() {
//          public void onProgressChanged(WebView view, int progress) {
//            // Activities and WebViews measure progress with different scales.
//            // The progress meter will automatically disappear when we reach 100%
//            activity.setProgress(progress * 1000);
//          }
//        });

        
        ////////////////////////////////////
        // cutView as partial screen
        
        setContentView(R.layout.activity_cut_view);
        
        cutView = (WebCutView) findViewById(R.id.webcutview);
    	seekBar = (SeekBar) findViewById(R.id.seekbar);
    	navBar = (LinearLayout) findViewById(R.id.nav_bar);
    	pageText = (TextView) findViewById(R.id.pageno);
    	
    	// INIT BEGIN
    	cutView.getSettings().setJavaScriptEnabled(true);

//    	if (savedInstanceState == null) {
    		System.out.println("gunjasal welcome");
    		cutView.navCutBySkbr(cutView.getCutPos());
//    	}
//    	else System.out.println("gunjasal welcome back");

    	navBar.getLayoutParams().height= 0; // TO HIDE
    	navBar.setAlpha(0);
    	navBar.setLayoutParams(navBar.getLayoutParams());
    	
    	cutView.setNavBar(navBar);
    	cutLen = cutView.getCutLength();
    	pageText.setText((cutView.getCutPos()+1)+"/"+cutLen);
    	// INIT END
        
    	seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
    	    @Override
    	    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
    	    {	
    	    	// EXACT LOGIC SHOULD BE RETHOUGHT  
    	    	// 1-2-3-4-5 real page
    	    	// 0-1-2-3-4 prog index
    	    	curPos = cutView.getCutPos();
    	    	double prog = ((cutLen-1)*(double)progress)/100;
    	    	double progFloor = Math.floor(prog);
    	    	double diff = prog-progFloor;
    	    	
    	    	// DECLARE ANOTHER STATIC DIFF AND COMPARE WITH LOCAL DIFF 
    	    	// IF CHANGED CALL navCutBySkbr	
    	    	
    	    	//System.out.println("gunjasal seekbar="+String.valueOf(progress)+ " prog= "+prog+ " progFloor= "+progFloor+ " diff= "+diff);
    	    	
    	    	// THUMB AT LEFT-END
    	    	if (prog == 0 ) {
    	    		cutView.navCutBySkbr((int)prog);
    	    	}
    	    	//THUMB AT RIGHT-END
    	    	else if ((int)prog == (cutLen-1)) {
    	    		cutView.navCutBySkbr((int)prog);
    	    	}
    	    	// THUMB IN MIDDLE SECTION
    	    	
    	    	// ASCENDING WORKS
    	    	// TODO : DESCENDING TO BE IMPLEMENTED
    	    	else if( diff != 0) {
	    	    	int intProgFloor = (int) Math.floor(prog);
	    	    	
	    	    	// ASCENDING : curPos < intProgFloor 
	    	    	if( intProgFloor != curPos ) {
	    	    		cutView.navCutBySkbr(intProgFloor);
	    	    		System.out.println("gunjasal cutView.navCutBySkbr(intProgFloor)"+ intProgFloor +" curPos="+curPos);
	    	    	}
	    	    	// DESCENDING 
	    	    	// TODO 
    	    	}
    	    	pageText.setText((cutView.getCutPos()+1)+"/"+cutLen);
    	    }
    	    
    	    @Override
    	    public void onStartTrackingTouch(SeekBar seekBar) {
    	        // not implemented 
    	    }

    	    @Override
    	    public void onStopTrackingTouch(SeekBar seekBar) {
    	        // not implemented 
    	    }
		});
    }
}
