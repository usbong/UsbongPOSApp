package usbong.android.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import usbong.android.pos_app.R;
import usbong.android.pos_app.UsbongMainActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UsbongHTTPConnect extends AsyncTask<Void, Void, String> {

	private static String TAG = "usbong.HTTPConnect.ProductsListDownload";
/*
	private String filePath = "";
	private String columnName = "";
	private String action = "";
*/	
	private URL url;
	private HttpURLConnection conn;
	private UsbongMainActivity a;
	
	public UsbongHTTPConnect(UsbongMainActivity a) {
		this.a = a;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override	
	protected String doInBackground(Void... data) {
/*
		//TODO: check if data is null
		filePath = data[0];
		columnName = data[1];
		action = data[2];
*/		
		String response = "";
		
		try {
			url = new URL(UsbongConstants.USBONG_STORE_PRODUCTS_LIST_DOWNLOAD);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoInput(true);
			conn.setDoOutput(true);
/*			
			JSONObject j = new JSONObject();
			j.put(UsbongConstants.product_id, data[0]);
			j.put(UsbongConstants.merchant_id, data[1]);
			j.put(UsbongConstants.product_type_id, data[2]);
			j.put(UsbongConstants.name, data[3]);
			j.put(UsbongConstants.price, data[4]);
			j.put(UsbongConstants.previous_price, data[5]);
			j.put(UsbongConstants.language, data[6]);
			j.put(UsbongConstants.author, data[7]);
			j.put(UsbongConstants.supplier, data[8]);
			j.put(UsbongConstants.description, data[9]);
			j.put(UsbongConstants.image_location, data[10]);
			j.put(UsbongConstants.format, data[11]);
			j.put(UsbongConstants.quantity_in_stock, data[12]);
			j.put(UsbongConstants.translator, data[13]);
			j.put(UsbongConstants.pages, data[14]);
						
			byte[] postData = j.toString().getBytes();
			
			OutputStream out = conn.getOutputStream();

		    out.write(postData);
		    out.close();
*/
	        if(conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
	            throw new RuntimeException("Failed : HTTP error code : "
	                + conn.getResponseCode());
	        } else {
		        BufferedReader br = new BufferedReader(new InputStreamReader(
		                (conn.getInputStream())));
		        String line;
		        while ((line=br.readLine()) != null) {
	                response+=line;
	            }
	        }
	        conn.disconnect();        
		}catch(IOException e) {
/*			Log.w(TAG, "HTTP3:" + e );
			response = e.getMessage();
*/			
			response = e.getMessage();

			//added by Mike, 20180310			
			a.runOnUiThread(new Runnable() {
				LayoutInflater inflater = (LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    View toastLayout = inflater.inflate(R.layout.my_custom_toast, null);

				public void run() {
/*
					Toast.makeText(a, "No connection.",
							   Toast.LENGTH_LONG).show();
				    }
*/
					// Notify the user
				    TextView textView = (TextView) toastLayout.findViewById(R.id.text);
				    textView.setText("No connection.");             
				    Toast toast = new Toast(a);
				    toast.setDuration(1000);
				    toast.setGravity(Gravity.CENTER, 0, 0);
				    toast.setView(toastLayout);
				    toast.show();  
				}
			});
		}catch(Exception e) {
			Log.w(TAG, "HTTP4:" + e );
			response = e.getMessage();
		}
		return response;
	}
	
	@Override
	protected void onPostExecute(String result){
		Log.d(TAG, "Response: " + result); //.replace("Current character set: utf8", "")
		
		if (!result.trim().equals("")) {			
			a.syncDB(result);//.replace("Current character set: utf8", ""));					
		}		
	}
}