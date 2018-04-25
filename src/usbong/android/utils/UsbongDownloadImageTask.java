package usbong.android.utils;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

//Reference: https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android;
//last accessed: 20180216;
//answer by: Android Developer; 
//edited by: abidibo

public class UsbongDownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//    ImageView bmImage;
    String urldisplay;
    
    public UsbongDownloadImageTask() {
    }
/*
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }
*/
    @Override
    protected Bitmap doInBackground(String... urls) {
    	 urldisplay = urls[0];
         Bitmap myBitmap = null;
         try {
             InputStream in = new java.net.URL(urldisplay).openStream();
             myBitmap = BitmapFactory.decodeStream(in);
             
             //added by Mike, 20180218
             UsbongUtils.storeImageInSDCard(urldisplay, myBitmap);    	
             
         } catch (Exception e) {
             Log.e("Error", e.getMessage());
             e.printStackTrace();
         }
         return myBitmap;
    	
/*    	
        String urldisplay = urls[0];
        Bitmap mIcon_val = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
//            mIcon11 = BitmapFactory.decodeStream(in);
            mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream()); 

            
//            URL newurl = new URL(photo_url_str); 
//            mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream()); 
//            profile_photo.setImageBitmap(mIcon_val);
                        
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon_val;
*/        
    }

    @Override
    protected void onPostExecute(Bitmap result) {
//        bmImage.setImageBitmap(result);
//        UsbongUtils.storeImageInSDCard(urldisplay, result);    	
    }
}