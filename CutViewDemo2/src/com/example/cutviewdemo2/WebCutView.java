package com.example.cutviewdemo2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

    class WebCutView extends WebView {
    	Context context;
    	GestureDetector gd;
    	
    	LinearLayout navBar;
//    	SeekBar navBar;
    	
    	private int cutViewWidth;

    	private static int cutPos = 0;
    	
    	private String cut[] = {"file:///android_asset/sliding_effects.html"
    			, "file:///android_asset/jquery_effects.html"
    			, "file:///android_asset/sliding_effects.html"
    	};
    	
    	
    	// CONSTRUCTORS BEGIN
    	public WebCutView(Context context) {
    		super(context);
        	this.context = context;
        	
        	if(!isInEditMode()) {
           		gd = new GestureDetector(context, sogl);
        	}
    	}
    	
    	public WebCutView(Context context, AttributeSet attr) {
    		super(context, attr);
        	this.context = context;
        	
        	if(!isInEditMode()) {
           		gd = new GestureDetector(context, sogl);
        	}
    	}
    	// CONSTRUCTORS END

    	// SET SEEKBAR
    	public void setNavBar(LinearLayout ll) {
    		navBar = ll;
    	}
    	
    	// GET CUT POSITION
    	public int getCutPos() {
    		return cutPos;
    	}
    	
    	public String getCutData(int idx) {
    		return cut[idx];
    	}
    	
    	public int getCutLength() {
    		return cut.length;
    	}
    	
    	// NAVIGATE BY TOUCH
    	private void navCutByTouch(int nav) {
    					
			Log.d("gunjasal", "navCutByTouch cutPos="+cutPos+" nav="+nav);
			
			cutPos += nav;
			
			// SHOW/HIDE NAVIGATOR
			if (nav==0) {
				
				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) navBar.getLayoutParams();
				
				Log.d("gunjasal : ", lp.toString());
				
		        if(navBar.getAlpha()==0) {
					showToast("gunjasal show navigation");
					
					lp.height = 80;
					navBar.setAlpha(1);
				}
				else {
					showToast("gunjasal hide navigation");
					
					lp.height = 0;
					navBar.setAlpha(0);
				}
				
				try {
					navBar.setLayoutParams(lp);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			// PREV/NEXT PAGE
			else {
				if(cutPos==-1) {
					showToast("gunjasal first page");
					cutPos = 0;
				}
				else if(cutPos==cut.length) {
					showToast("gunjasal last page");
					cutPos = cut.length-1;
				}
				else {
					SeekBar sb = (SeekBar) navBar.findViewById(R.id.seekbar);
					TextView tv = (TextView) navBar.findViewById(R.id.pageno);
					
					int prog = (int) (((float)cutPos/(float)(cut.length-1))*100);
					Log.d("gunjasal", "setprogress="+(float)cutPos/(float)(cut.length-1));
					
					sb.setProgress(prog);
					tv.setText(Integer.toString(cutPos+1)+"/"+cut.length);
					
					this.loadUrl(cut[cutPos]);
				}
			}
		}
    	
    	// NAVIGATE BY SEEKBAR
    	public void navCutBySkbr(int sbPos) {
    		Log.d("gunjasal", "navCutBySkbr sbPos="+sbPos);
			
    		cutPos = sbPos;
    		
    		if(cutPos >= 0 && cutPos <= cut.length-1) this.loadUrl(cut[cutPos]);
    		else showToast("gunjasal Invalid cut#");
    	}
    	
    	// SCROLL DOWN ON DOUBLE TAP
    	@SuppressLint("NewApi")
    	private boolean scrollByDblTap() {
    		boolean b =  false;
    		
//    		TODO : WHT NOT WORKING?
    		b = this.pageDown(false);
    		
    		// TODO : TEMP SOLUTION 
//    		int viewHeight = (int) Math.floor(this.getHeight());
    		int screenHeight = this.getHeight();
    		int contentHeight = this.getContentHeight();
    		int scroll2Y = 0;
    		
    		if(Math.abs(screenHeight-contentHeight) > 150) {
    			scroll2Y = (int)this.getScrollY()+150;
    		}
    		else {
    			scroll2Y = (int)this.getScrollY()+Math.abs(screenHeight-contentHeight);
    		}
    		
    		Log.d("gunjasal", "contentHeight ="+contentHeight+" getScrollY="+scroll2Y);
    		
    		if(screenHeight >= contentHeight+scroll2Y) {
    			this.scrollTo((int)this.getX(), (int)this.getScrollY()+150); // CONTIUOUS SCROLL
    		}
    		
    		Log.d("gunjasal", "scrollbydbltap="+Boolean.toString(b));
    		
    		return b;
    	}

		// TOUCH EVENT
    	@Override
	    public boolean onTouchEvent(MotionEvent event) {
	    	
	        cutViewWidth = this.getWidth();
	    	
//	    	System.out.println("gunjasal X="+cutViewWidth);	    	
//	    	System.out.println("gunjasal onTouchEvent");
	    	
	    	boolean gdTe = gd.onTouchEvent(event);
	    	boolean superTe = super.onTouchEvent(event);
	    	
//	    	gdTe true = onTap
//	    	superTe true = always
//	    	System.out.println("gdTe = "+gdTe +" superTe= "+superTe);
	    	return (gdTe || superTe); 

	    }
		// TOUCH EVENT END
    	
		// GESTURE DETECTOR ON WEBVIEW
    	GestureDetector.SimpleOnGestureListener sogl = new GestureDetector.SimpleOnGestureListener() {
	    	public boolean onDown(MotionEvent event) {
//	    		System.out.println("gunjasal onDown");
	    		super.onDown(event);
	    		return true;
	    	}
	    	
	    	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
	    		
//	    		System.out.println("gunjasal fling1 : X=" + event1.getX() +" Y= "+event1.getY());
//	    		System.out.println("gunjasal fling2 : X=" + event2.getX() +" Y= "+event2.getY());
	    		
	    		if (event1.getRawX() > event2.getRawX() && StrictMath.abs(event1.getRawY()-event2.getRawY())<100) {
	    			System.out.println("gunjasal -1");
	    			navCutByTouch(1);
	    		} else if(event1.getRawX() < event2.getRawX() && StrictMath.abs(event1.getRawY()-event2.getRawY())<100){
	    			System.out.println("gunjasal +1");
	    			navCutByTouch(-1);
	    		}
	    		
	    		return true;
	    	}
	    	
	    	public boolean onSingleTapConfirmed(MotionEvent event) {
	    		float posX = event.getX();
	    		float posY = event.getY();
	    		
	    		//System.out.println("gunjasal single tap X="+event.getX()+" Y="+event.getY());
//	    		System.out.println("gunjasal single tap X="+event.getX()+" / "+cutViewWidth);
	    		
	    		if(posX<=(cutViewWidth/3)) {
	    			navCutByTouch(-1);
	    		}
	    		else if (posX>=(cutViewWidth/3*2)) {
	    			navCutByTouch(1);
	    		}
	    		else {
	    			navCutByTouch(0);
	    		}
	    		
	    		return true;
	    	}
	    	
	    	public boolean onDoubleTap(MotionEvent event) {
	    		// SCROLL DOWN A BIT
	    		return scrollByDblTap();
	    	}
		};
		// GESTURE DETECTOR ON WEBVIEW END
		
		void showToast(final String text) {
			Toast t = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			t.show();
		}
    }
