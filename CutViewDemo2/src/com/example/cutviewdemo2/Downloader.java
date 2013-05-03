package com.example.cutviewdemo2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

public class Downloader {
	private Context context;
	
	public Downloader(Context context) {
		this.context = context;
	}

	public void createExternalStoragePrivateFile() {
	    // Create a path where we will place our private file on external
	    // storage.
		if (context != null) {
			
		    File file = new File(context.getExternalFilesDir(null), "DemoFile.png");
		    
		    System.out.println("gunjasal file =" +file.getAbsolutePath());
	
		    try {
		        // Very simple code to copy a picture from the application's
		        // resource into the external file.  Note that this code does
		        // no error checking, and assumes the picture is small (does not
		        // try to copy it in chunks).  Note that if external storage is
		        // not currently mounted this will silently fail.
		        InputStream is = context.getResources().openRawResource(R.drawable.ic_launcher);
		        OutputStream os = new FileOutputStream(file);
		        byte[] data = new byte[is.available()];
		        is.read(data);
		        os.write(data);
		        is.close();
		        os.close();
		    } catch (IOException e) {
		        // Unable to create file, likely because external storage is
		        // not currently mounted.
		        Log.w("ExternalStorage", "Error writing " + file, e);
		    }
		}
	}

	public void deleteExternalStoragePrivateFile() {
	    // Get path for the file on external storage.  If external
	    // storage is not currently mounted this will fail.
	    File file = new File(context.getExternalFilesDir(null), "DemoFile.png");
	    if (file != null) {
	        file.delete();
	    }
	}

	public boolean hasExternalStoragePrivateFile() {
	    // Get path for the file on external storage.  If external
	    // storage is not currently mounted this will fail.
	    File file = new File(context.getExternalFilesDir(null), "DemoFile.png");
	    if (file != null) {
	        return file.exists();
	    }
	    return false;
	}
}
