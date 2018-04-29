/*
 * Copyright 2016 Michael Syson
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package usbong.android.pos_app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;

import usbong.android.db.UsbongDbHelper;
import usbong.android.utils.UsbongConstants;
import usbong.android.utils.UsbongHTTPConnect;
import usbong.android.utils.UsbongUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/*
 * This is Usbong's Main Menu activity. 
 */
public class UsbongMainActivity extends AppCompatActivity/*Activity*/ 
{	
	//edited by Mike, 20170606
//	private int currCategory = UsbongConstants.PRODUCT_TYPE_ALL;//ITEMS_LIST_DEFAULT;

	private static UsbongMainActivity instance;
				
	public static String timeStamp;
	
//	private static Date startTime;	

//commented out by Mike, 20180427	
//	protected UsbongDecisionTreeEngineActivity myUsbongDecisionTreeEngineActivity;
	protected SettingsActivity mySettingsActivity;
	
	private ProgressDialog myProgressDialog;

	//edited by Mike, 20170525
	private static int currModeOfPayment=UsbongConstants.defaultModeOfPayment; 
    
	public ListView treesListView;
	
	private CustomDataAdapter mCustomAdapter;
	private ArrayList<String> listOfTreesArrayList;
	
	private boolean isInTreeLoader;
	
	//added by Mike, 20170523
	private UsbongDbHelper myDbHelper;
	private SQLiteDatabase mySQLiteDatabase;
	
	//edited by Mike, 20180427
	private int currProductTypeId = UsbongConstants.PRODUCT_TYPE_NON_MED; //default //UsbongConstants.PRODUCT_TYPE_BOOKS; //default
	private static boolean isInMerchantShop=false;
	private static boolean hasPerformedSearch=false;
	private String searchEditTextString="";
	
	private ArrayList<Button> categoryButtonsList;
	private ArrayList<Integer> categoryListInteger;
	
	private static int merchantId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);

        //added by Mike, 27 Sept. 2015
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        instance=this;
        
        //added by Mike, 25 Sept. 2015
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.usbong));//getResources().getString(R.string.app_name)); //edited by Mike, 20170214
//        getSupportActionBar().setDisplayUseLogoEnabled(true);        

//    	if (instance==null) { //comment this out, since the app seems to retain the instance even after we do a finish to GameActivity to close the app...
	        setContentView(R.layout.main);	        
//	    	startTime = new Date();	        

/*	        
            //added by Mike, 20161117
        	Bundle extras = getIntent().getExtras();
        	if (extras!=null) {
	        	String message = extras.getString("completed_tree");

	        	if (message.equals("true")) {
			        AppRater.showRateDialog(this); 
	        	}	        		
        	}
*/
	        //added by Mike, 20170530
			listOfTreesArrayList = new ArrayList<String>();
	        
			//added by Mike, 20170606
    		categoryListInteger = new ArrayList<Integer>();
			categoryButtonsList = new ArrayList<Button>();
			
	        reset();
        	
//	        initMainMenuScreen();
//	        init();
    		
    		//edited by Mike, 20170530
        	Bundle extras = getIntent().getExtras();
        	if (extras!=null) {
        		//added by Mike, 20170606
            	myDbHelper = new UsbongDbHelper(this);
                myDbHelper.initializeDataBase();
        		
    			String merchantName = getIntent().getExtras().getString("loadMerchantStore");
    			
    			//added by Mike, 20180416
    			if (merchantName!=null) {
        			loadMerchantStore(merchantName);			    				
    			}
    			else {
        			loadMerchantStore(UsbongConstants.MERCHANT_NAME);			    				
    			}
    		} 
        	else {
        		myProgressDialog = ProgressDialog.show(instance, "Loading...",
        				  "This takes only a short while.", true, false);				  
        		new MyBackgroundTask().execute();    
    		}
    }
    
    public static UsbongMainActivity getInstance() {
    	return instance;
    }
    
    /*
     * Initialize this activity
     */
    public void init()
    {    	    	
		//added by Mike, 20170310
    	UsbongUtils.deleteRecursive(new File(UsbongUtils.BASE_FILE_PATH_TEMP));
    	
    	try{    		
    		//commented out by Mike, 4 Oct. 2015
  			UsbongUtils.createUsbongFileStructure();
  			
  			//added by Mike, 20160417
  			UsbongUtils.initUsbongConfigFile();
    	}
    	catch(IOException ioe) {
    		ioe.printStackTrace();
    	}
		
		//added by Mike, 30 April 2015
		isInTreeLoader=false;		

		UsbongUtils.clearTempFolder();

		//added by Mike, 20170605
    	myDbHelper = new UsbongDbHelper(this);
        myDbHelper.initializeDataBase();
		
		initSearch();			
    }
    
    public void initSearch() {    	
        final EditText searchEditText = (EditText)findViewById(R.id.search_edittext);
        
    	//added by Mike, 20170530
    	if (!searchEditTextString.equals("")) {
        	searchEditText.setText(searchEditTextString);
    	}
        
        //Reference: https://stackoverflow.com/questions/3205339/android-how-to-make-keyboard-enter-button-say-search-and-handle-its-click;
    	//last accessed: 20170523
    	//answer by: Robby Pond; edited by: sergej shafarenka
    	searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
    	    @Override
    	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    	        if (actionId == EditorInfo.IME_ACTION_SEARCH) {    	        	
    	        	//added by Mike, 20170605
    	        	//hide keyboard
    	        	InputMethodManager imm = (InputMethodManager) instance.getSystemService(Activity.INPUT_METHOD_SERVICE);
    	        	imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    	        	
    	        	processSearch(searchEditText);
    	            return true;
    	        }
    	        return false;
    	    }
    	});

    	//Reference: https://stackoverflow.com/questions/13135447/setting-onclicklistner-for-the-drawable-right-of-an-edittext;
    	//last accessed: 20170525
    	//answer by: AZ_
    	searchEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_LEFT = 0;
//                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
//                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (searchEditText.getRight() - searchEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
        	        	//added by Mike, 20170605
        	        	//hide keyboard
        	        	InputMethodManager imm = (InputMethodManager) instance.getSystemService(Activity.INPUT_METHOD_SERVICE);
        	        	imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    	processSearch(searchEditText);
/*                    	
                    	// your action here
        	            performSearch(searchEditText.getText().toString());
        	            showSearchResults();
        	            searchEditText.clearFocus();
*/        	            
                        return true;
                    }
                }
                return false;
            }
        });
    }
    
    public void processSearch(EditText searchEditText) {
        performSearch(searchEditText.getText().toString());
        showSearchResults();
        searchEditText.clearFocus();

    	//added by Mike, 20170530
    	if (isInMerchantShop) {
    		isInMerchantShop=false;
    		searchEditTextString = searchEditText.getText().toString();
        	setContentView(R.layout.main);

        	hasPerformedSearch=true;
        	
        	initSearch();
    		initTreeLoader();

/*    		myProgressDialog = ProgressDialog.show(instance, "Loading...",
    				  "This takes only a short while.", true, false);				  
    		new MyBackgroundTask().execute();  
*/    		
    	}    	        	
    	else {
    		searchEditTextString = "";    	        		
    	}
//    	setContentView(R.layout.main);
/*
        performSearch(searchEditText.getText().toString());
        showSearchResults();
        searchEditText.clearFocus();
*/        
    }
    
    public void loadMerchantStore(String merchantName) { 
    	isInMerchantShop=true;
		setContentView(R.layout.tree_list_interface);				
    	initSearch(); //added by Mike, 20170529		
		
		merchantName = merchantName.substring(2); //remove "隨假ｿｽ "
/*		
    	myDbHelper = new UsbongDbHelper(this);
        myDbHelper.initializeDataBase();
*/
        listOfTreesArrayList.clear(); //added by Mike, 20170525
        categoryButtonsList.clear();
        categoryListInteger.clear();
        
        try {
	         mySQLiteDatabase = myDbHelper.getReadableDatabase();
	         
		     String getMerchantId = "select * from '" + "merchant" + "'" + " where merchant_name LIKE '%"+merchantName+"%'";
		     Cursor cMerchantId = mySQLiteDatabase.rawQuery(getMerchantId, null);
		     
		     merchantId=1; //1 is the default
		     if (cMerchantId != null) {
			     if (cMerchantId.moveToFirst()) {
		     		 merchantId = Integer.parseInt(cMerchantId.getString(cMerchantId.getColumnIndex("merchant_id")));
			     }
		     }
		     
     		 //added by Mike, 20170529
     	     String getMerchantBrandMotto = "select merchant_motto from '" + "merchant" + "'" + " where merchant_id="+merchantId;
     	     Cursor cMerchantBrandMotto = mySQLiteDatabase.rawQuery(getMerchantBrandMotto, null);

     	     String getMerchantBrandMottoFontColor = "select merchant_motto_font_color from '" + "merchant" + "'" + " where merchant_id="+merchantId;
     	     Cursor cMerchantBrandMottoFontColor = mySQLiteDatabase.rawQuery(getMerchantBrandMottoFontColor, null);
     	     
     	     if (cMerchantBrandMotto != null) {
 		        if (cMerchantBrandMotto.moveToFirst()) { // if Cursor is not empty
 		     		 TextView brandMottoTextView = (TextView)findViewById(R.id.brand_motto);
 		     		 brandMottoTextView.setText(Html.fromHtml(cMerchantBrandMotto.getString(cMerchantBrandMotto.getColumnIndex("merchant_motto"))));
 		     		 
 		     	     if (cMerchantBrandMottoFontColor != null) {
 		 		        if (cMerchantBrandMottoFontColor.moveToFirst()) { // if Cursor is not empty
 		 		        	brandMottoTextView.setTextColor(Color.parseColor(cMerchantBrandMottoFontColor.getString(cMerchantBrandMottoFontColor.getColumnIndex("merchant_motto_font_color"))));
 		 		        }
 		     	     }
 		        }
     	     }
     	         	     
     		 //Reference: http://www.anddev.org/tinytut_-_get_resources_by_name__getidentifier_-t460.html; last accessed 14 Sept 2011
             Resources myRes = instance.getResources();

     		 LinearLayout brandBgLinearLayout = (LinearLayout)findViewById(R.id.brand_bg);
     		 brandBgLinearLayout.setBackground(Drawable.createFromStream(myRes.getAssets().open("merchants/"+merchantName+"_brand_bg.jpg"), null));
     		 ImageView brandLogoImageView = (ImageView)findViewById(R.id.brand_logo);
//     		 brandLogoImageView.setBackground(UsbongUtils.getImageDrawable(this, merchantName+"_brand_logo.jpg"));
     		 brandLogoImageView.setImageDrawable(Drawable.createFromStream(myRes.getAssets().open("merchants/"+merchantName+"_brand_logo.jpg"), null));
/*
     		 //added by Mike, 20170605
     		 categoryListInteger = new ArrayList<Integer>();
//     		 ArrayList<String> categoryListString = new ArrayList<String>();
*/     		      		

		     String getMerchantProducts = "select * from '" + "product" + "'" + " where merchant_id="+merchantId;
		     Cursor c = mySQLiteDatabase.rawQuery(getMerchantProducts, null);
		     
		     if (c != null) {
			        if (c.moveToFirst()) { // if Cursor is not empty
			        	while (!c.isAfterLast()) {			        		
				        	//edited by Mike, 20170610
				        	int cat = Integer.parseInt(c.getString(c.getColumnIndex("product_type_id")));
				        	if (!categoryListInteger.contains(cat)) {
				        		categoryListInteger.add(cat);
				        	}			
				        	
				        	c.moveToNext();
				        }
			        }
			        else {
			           // Cursor is empty
			        	Log.d(">>>>>", "cursor is empty");
			        }
			     }
			     else {
			        // Cursor is null
			        	Log.d(">>>>>", "cursor is null");
			     }

	     		String getProductTypeIDs = "select * from '" + "product_type" + "'";
				Cursor cProductTypeIDs = mySQLiteDatabase.rawQuery(getProductTypeIDs, null);
				ArrayList<String> productTypeNamesList = new ArrayList<String>();
				ArrayList<Integer> productTypeIDsList = new ArrayList<Integer>();
							
			    if (cProductTypeIDs != null) {
			        if (cProductTypeIDs.moveToFirst()) { // if Cursor is not empty
			        	while (!cProductTypeIDs.isAfterLast()) {
			        		String productTypeName = cProductTypeIDs.getString(cProductTypeIDs.getColumnIndex("product_type_name"));
			        		productTypeNamesList.add(productTypeName);

			        		int productTypeID = cProductTypeIDs.getInt(cProductTypeIDs.getColumnIndex("product_type_id"));
			        		productTypeIDsList.add(productTypeID);

			        		cProductTypeIDs.moveToNext();
			        	}
			        }
			    }

		   //added by Mike, 20170605
				LinearLayout categoryLinearLayout = (LinearLayout)findViewById(R.id.category_linearlayout);
				for(int i=0; i<categoryListInteger.size(); i++) {
					 Button b = new Button(this);
					 b.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
					 b.setTextColor(Color.parseColor("#554223"));
					 b.setTextSize(14);
	/*					 if (i==0) {
						 b.setTypeface(Typeface.DEFAULT_BOLD);						 
					 }
	*/					 
					 b.setBackgroundColor(Color.TRANSPARENT);  
					 
					 final int cat = categoryListInteger.get(i);
					 b.setText("    "+productTypeNamesList.get(categoryListInteger.get(i)-1).toUpperCase()+"    ");
					 					 
			         b.setOnClickListener(new OnClickListener() {
			            @Override
			            public void onClick(View v) {
			                initTreeLoaderDynamically(cat);
			            }
			         });    
					 
					 categoryLinearLayout.addView(b);
					 categoryButtonsList.add(b);
				}								
				currProductTypeId = categoryListInteger.get(0);
	     		 
				
				//added by Mike, 20170825
				 String getMerchantProductsForFirstCategoryOnly = "select * from '" + "product" + "'" + " where merchant_id="+merchantId+" and product_type_id="+categoryListInteger.get(0);
			     Cursor c2 = mySQLiteDatabase.rawQuery(getMerchantProductsForFirstCategoryOnly, null);
			     
			     if (c2 != null) {
				        if (c2.moveToFirst()) { // if Cursor is not empty
				        	while (!c2.isAfterLast()) {
				        		//edited by Mike, 20170725
				        		String price;
				        		String prevPrice="";//added by Mike, 20170825
				        		int quantityInStock = c2.getInt(c2.getColumnIndex("quantity_in_stock"));
				        		if (quantityInStock<1) {
				        			price = "out of stock";
				        		}
				        		else {
				        			price = "₱" + c2.getString(c.getColumnIndex("price"));
				        			
				        			//added by Mike, 2017
					        		prevPrice = c2.getString(c2.getColumnIndex("previous_price"));
					        		if (prevPrice!=null) {
					        			prevPrice = "\n<i>(from ₱" + prevPrice + ")</i>";
					        		}			     
					        		else {
					        			prevPrice="";
					        		}
				        		}

				        		//added by Mike, 20170725			        		 
							    currProductTypeId = c2.getInt(c2.getColumnIndex("product_type_id"));
				        		
				        		String productDetails="";

			    				String authorString = c2.getString(c2.getColumnIndex("author"));
			    				if (authorString==null) {
			    					authorString="";
			    				}
			    				else {
			    					authorString = authorString +"\n";
			    				}
/*			    				
				        		productDetails =  "<b>"+c2.getString(c2.getColumnIndex("name"))+"</b>\n"+
			   							 authorString+
			   							 "<font color='#644d17'><b>"+price+prevPrice+"</b>\n[Free Delivery]</font>"+
			   							 "MerchantName: "+merchantName+
			   							 "currProductTypeId: "+currProductTypeId;
*/				        		
			    				//edited by Mike, 20180419
				        		productDetails =  "<b>"+c2.getString(c2.getColumnIndex("name"))+"</b>\n"+
			   							 authorString+
			   							 "<font color='#644d17'><b>"+price+prevPrice+"</b>\n[Free Delivery]</font>"+
			   							 "MerchantName: "+merchantName+
			   							 "currProductTypeId: "+currProductTypeId+
			   							 "productOverview: " + c2.getString(c2.getColumnIndex("product_overview"));//added by Mike, 20180419

					        	listOfTreesArrayList.add(productDetails);
					        	
					        	//edited by Mike, 20170610
					        	int cat = Integer.parseInt(c2.getString(c2.getColumnIndex("product_type_id")));
					        	if (!categoryListInteger.contains(cat)) {
					        		categoryListInteger.add(cat);
					        	}			
					        	
					        	c2.moveToNext();
					        }
				        }
				        else {
				           // Cursor is empty
				        	Log.d(">>>>>", "cursor is empty");
				        }
				     }
				     else {
				        // Cursor is null
				        	Log.d(">>>>>", "cursor is null");
				     }
				
        } catch (Exception ex) {
           ex.printStackTrace();
        } /*finally {
            try {
                myDbHelper.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                myDbHelper.close();
            }        	 
        }*/
        
	     
        
        
/*        
        //added by Mike, 20170610    	
    	int highestCategory=0;
    	for (int i=0; i<categoryListInteger.size(); i++) { 
    		if (highestCategory < categoryListInteger.get(i)) {
    			highestCategory = categoryListInteger.get(i);
    		}    		
    	}
    	//get the lowestcategoryListInteger 
    	int lowestCategory=highestCategory; 
    	for (int i=0; i<categoryListInteger.size(); i++) { 
    		if (lowestCategory > categoryListInteger.get(i)) {
    			lowestCategory = categoryListInteger.get(i);
    		}
    	}		
*/    	
		hasPerformedSearch=true;
/*
        //show only product items of merchant for the default (lowest/first) category
    	initTreeLoaderDynamically(lowestCategory);
*/    	
		//edited by Mike, 20170810
    	initTreeLoaderDynamically();
    	
    	
/*    			
        //edited by Mike, 20170530
    	mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
		mCustomAdapter.sort(); //edited by Mike, 20170203

		treesListView = (ListView)findViewById(R.id.tree_list_view);
		treesListView.setLongClickable(true);
		treesListView.setAdapter(mCustomAdapter);		
*/		
    }
    
    public void performSearch(String s) {
        listOfTreesArrayList.clear(); //added by Mike, 20170525
        
        try {
	         mySQLiteDatabase = myDbHelper.getReadableDatabase();
	         
		     String table = "product";
		     String query = "";
		     
		     if (s==null) {
		    	 if (merchantId==-1) {
				     query = "select * from '" + table + "'" + " where product_type_id="+currProductTypeId;		    		 
		    	 }
		    	 else {
				     query = "select * from '" + table + "'" + " where product_type_id="+currProductTypeId
								 							 + " AND merchant_id="+merchantId;		    		 
		    	 }
		     }
		     else {
			     query = "select * from '"+table+"' where NAME like '%"+s+"%' OR author LIKE '%"+s+"%'";		     		    	 
//			     currProductTypeId = UsbongConstants.PRODUCT_TYPE_BOOKS; //.ITEMS_LIST_DEFAULT;
		     }		     
		     
//		     String query = "select * from '" + table + "'" + " where product_type_id="+currProductTypeId;
		     Cursor c = mySQLiteDatabase.rawQuery(query, null);
		     if (c != null) {
			        if (c.moveToFirst()) { // if Cursor is not empty
			        	while (!c.isAfterLast()) {
			        		//edited by Mike, 20170725
			        		String price="";
			        		String prevPrice=""; //added by Mike, 20170825
			        		
			        		int quantityInStock = c.getInt(c.getColumnIndex("quantity_in_stock"));
			        		if (quantityInStock<1) {
			        			price = "out of stock";
			        		}
			        		else {
			        			price = "₱" + c.getString(c.getColumnIndex("price"));
			        			
			        			//added by Mike, 2017
				        		prevPrice = c.getString(c.getColumnIndex("previous_price"));
				        		if (prevPrice!=null) {
				        			prevPrice = "\n<i>(from ₱" + prevPrice + ")</i>";
				        		}			     
				        		else {
				        			prevPrice="";
				        		}
			        		}
			        		
/*			        		
			        		String price = c.getString(c.getColumnIndex("price"));
			        		if (price==null) {
			        			price = "out of stock";
			        		}
			        		else {
			        			price = "₱" + price;
			        		}
*/			        		
			        		
			        		//added by Mike, 20170725			        		 
						    currProductTypeId = c.getInt(c.getColumnIndex("product_type_id"));
			        		
			        		String productDetails="";
/*			        		if (s==null) {
  */
		    			     String queryMerchantName = "select merchant_name from '" + "merchant" + "'" + " where merchant_id="+c.getString(c.getColumnIndex("merchant_id"));
		    			     Cursor cMerchantName = mySQLiteDatabase.rawQuery(queryMerchantName, null);
		    			     String merchantName="N/A";
		    			     if (cMerchantName.moveToFirst()) {
		    			    	 merchantName = cMerchantName.getString(cMerchantName.getColumnIndex("merchant_name"));
		    			     }
/*		    			     
				        		switch(currCategory) {
					    			case UsbongConstants.ITEMS_LIST_BEVERAGES:
						        		productDetails =  "<b>"+c.getString(c.getColumnIndex("name"))+"</b>\n"+
					   							 "<font color='#644d17'><b>"+price+"</b>\n[Free Delivery]</font>"+
					   							 "MerchantName: "+merchantName;
						        		break;
					    			default:
*/					    			
					    				String authorString = c.getString(c.getColumnIndex("author"));
					    				if (authorString==null) {
					    					authorString="";
					    				}
					    				else {
					    					authorString = authorString +"\n";
					    				}
					    				
					    				//edited by Mike, 20180419
						        		productDetails =  "<b>"+c.getString(c.getColumnIndex("name"))+"</b>\n"+
					   							 authorString+
					   							 "<font color='#644d17'><b>"+price+prevPrice+"</b>\n[Free Delivery]</font>"+
					   							 "MerchantName: "+merchantName+
					   							 "currProductTypeId: "+currProductTypeId+
					   							 "productOverview: " + c.getString(c.getColumnIndex("product_overview"));//added by Mike, 20180419
/*					    				break;
				        		}
*/				        		
/*				        
			        		}
			        		else {
*/			        		
				        		/*if (Integer.parseInt(c.getString(c.getColumnIndex("product_type_id")))
				        				==UsbongConstants.PRODUCT_TYPE_BEVERAGES) {
				        			//TODO: remove field labels
				        			productDetails =  "Name: "+c.getString(c.getColumnIndex("name"))+"\n"+
				   							 "Price: "+price+"\n"+
				   							 "Language: "+c.getString(c.getColumnIndex("language"));			        			
				        		}
				        		else {*/
/*			        			
					        		productDetails =  "Title: "+c.getString(c.getColumnIndex("name"))+"\n"+
				   							 "Author: "+c.getString(c.getColumnIndex("author"))+"\n"+
				   							 "Price: "+price+"\n"+
				   							 "<b>Format:</b> "+c.getString(c.getColumnIndex("format"))+"\n"+	
				   							 "Language: "+c.getString(c.getColumnIndex("language"));			        			
*/
/*			        			
				        		productDetails =  "<b>"+c.getString(c.getColumnIndex("name"))+"</b>\n\n"+
			   							 ""+c.getString(c.getColumnIndex("author"))+"\n\n"+
			   							 "<b><font color='#7c693d'>"+price+"</font></b>";	
*/				        		
/*				        		}			        		
*/	
/*				        		
			        		}
*/
				        	listOfTreesArrayList.add(productDetails);
			        	    c.moveToNext();
			        	  }
			        }
			        else {
			           // Cursor is empty
			        	Log.d(">>>>>", "cursor is empty");
			        }
			     }
			     else {
			        // Cursor is null
			        	Log.d(">>>>>", "cursor is null");
			     }
        } catch (Exception ex) {
           ex.printStackTrace();
        } finally {
            try {
                myDbHelper.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                myDbHelper.close();
            }        	 
        }
    }

    public void showSearchResults() {
    	mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
		mCustomAdapter.sort(); //edited by Mike, 20170203
	
		/*
		//Reference: http://stackoverflow.com/questions/8908549/sorting-of-listview-by-name-of-the-product-using-custom-adaptor;
		//last accessed: 2 Jan. 2014; answer by Alex Lockwood
		mCustomAdapter.sort(new Comparator<String>() {
		    public int compare(String arg0, String arg1) {
		        return arg0.compareTo(arg1);
		    }
		});
		*/		
		treesListView = (ListView)findViewById(R.id.tree_list_view);
		treesListView.setLongClickable(true);
		treesListView.setAdapter(mCustomAdapter);
		
//		String pleaseMakeSureThatXMLTreeExistsString = (String) getResources().getText(R.string.pleaseMakeSureThatXMLTreeExistsString);
//		String alertString = (String) getResources().getText(R.string.alertStringValueEnglish);
		
		if (listOfTreesArrayList.isEmpty()){
			String alertTitle;
		    //Reference: http://stackoverflow.com/questions/23024831/android-shared-preferences-example
	        //; last accessed: 20150609
	        //answer by Elenasys
	        //added by Mike, 20150207
			SharedPreferences prefs = getSharedPreferences(UsbongConstants.MY_ACCOUNT_DETAILS, MODE_PRIVATE);
	        if (prefs!=null) {
	        	if (!prefs.getString("firstName", "").trim().equals("")) {
		        	alertTitle = "Hey, "+prefs.getString("firstName", "")+"!";	        		
	        	}
	        	else {
		        	alertTitle = "Hey!";	        		        		
	        	}
	        }
	        else {
	        	alertTitle = "Hey!";	        	
	        }
	        
			new AlertDialog.Builder(UsbongMainActivity.this).setTitle(alertTitle)
			.setMessage("Sorry, we didn't find what you are looking for at our store.")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {					
				@Override
				public void onClick(DialogInterface dialog, int which) {
					instance.initTreeLoader();
				}
			}).show();	        		        	
		  }	
    }
    
    //added by Mike, 20170525
    //Reference: http://stackoverflow.com/questions/13017122/how-to-show-progressdialog-across-launching-a-new-activity;
    //last accessed: 29 Sept. 2015; answer by: Slartibartfast, 23 Oct. 2012
    class MyBackgroundTask extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected void onPreExecute() {
			Log.d(">>>>","onPreExectue()");
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			Log.d(">>>>","onPostExecute()");
	    	new Thread(new Runnable() {
			    public void run() {
		            Handler mainHandler = new Handler(getInstance().getBaseContext().getMainLooper());
		            Runnable myRunnable = new Runnable() {
		            	@Override
		            	public void run() {		            		
		    				//added by Mike, 20170525
		    				initTreeLoader();
		    				
						    if (instance.myProgressDialog != null) {
						        instance.myProgressDialog.dismiss();
						    }				            		
		            	}
		            };
		            mainHandler.post(myRunnable);
		    		return; //end this background thread
			    }
			}).start();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {		
			Log.d(">>>>","doInBackground()");
			init();
		    //Do all your slow tasks here but don't set anything on UI
		    //ALL UI activities on the main thread 		
		    return true;
		
		}		
	}
    
    //added by Mike, 20170330
    public void initTreeLoader(int currProductTypeId) {
        this.currProductTypeId = currProductTypeId;
        initTreeLoader();
    }
    
	public void initTreeLoader()
	{
//		setContentView(R.layout.tree_list_interface);				

		isInTreeLoader=true;
		
		//added by Mike, 20170813
		merchantId=-1; //search product items of all merchants

		//edited by Mike, 20180427
        Button medButton = (Button)findViewById(R.id.med_button);
        medButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initTreeLoader(UsbongConstants.PRODUCT_TYPE_MED);
            }
        });    

        Button nonMedButton = (Button)findViewById(R.id.non_med_button);
        nonMedButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initTreeLoader(UsbongConstants.PRODUCT_TYPE_NON_MED);
            }
        });    
		
/*		
        Button booksButton = (Button)findViewById(R.id.books_button);
        booksButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initTreeLoader(UsbongConstants.PRODUCT_TYPE_BOOKS);
            }
        });    

        Button childrensButton = (Button)findViewById(R.id.childrens_button);
        childrensButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initTreeLoader(UsbongConstants.PRODUCT_TYPE_CHILDRENS);
            }
        });    

        Button textbooksButton = (Button)findViewById(R.id.textbooks_button);
        textbooksButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initTreeLoader(UsbongConstants.PRODUCT_TYPE_TEXTBOOKS);
            }
        });    

        Button medicalButton = (Button)findViewById(R.id.medical_button);
        medicalButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initTreeLoader(UsbongConstants.PRODUCT_TYPE_MEDICAL);
            }
        });    

        Button foodButton = (Button)findViewById(R.id.food_button);
        foodButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initTreeLoader(UsbongConstants.PRODUCT_TYPE_FOOD);
            }
        });    

        Button beveragesButton = (Button)findViewById(R.id.beverages_button);
        beveragesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initTreeLoader(UsbongConstants.PRODUCT_TYPE_BEVERAGES);
            }
        });    

        Button comicsButton = (Button)findViewById(R.id.comics_button);
        comicsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initTreeLoader(UsbongConstants.PRODUCT_TYPE_COMICS);
            }
        });    

        Button mangaButton = (Button)findViewById(R.id.manga_button);
        mangaButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initTreeLoader(UsbongConstants.PRODUCT_TYPE_MANGA);
            }
        });    

        Button toysAndCollectiblesButton = (Button)findViewById(R.id.toys_collectibles_button);
        toysAndCollectiblesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initTreeLoader(UsbongConstants.PRODUCT_TYPE_TOYS_AND_COLLECTIBLES);
            }
        });    

        Button miscellaneousButton = (Button)findViewById(R.id.miscellaneous_button);
        miscellaneousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initTreeLoader(UsbongConstants.PRODUCT_TYPE_MISCELLANEOUS);
            }
        });    

        Button promosButton = (Button)findViewById(R.id.promos_button);
        promosButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initTreeLoader(UsbongConstants.PRODUCT_TYPE_PROMOS);
            }
        });    
*/
		
		//edited by Mike, 20170530
		if (!hasPerformedSearch) {
			performSearch(null);			
		}	
		else {
			hasPerformedSearch=false;
		}
		
        switch (currProductTypeId) {
        	//edited by Mike, 20180427
    	case UsbongConstants.PRODUCT_TYPE_MED:
            medButton.setTypeface(Typeface.DEFAULT_BOLD);
            nonMedButton.setTypeface(Typeface.DEFAULT);                
            mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
            break;
    	case UsbongConstants.PRODUCT_TYPE_NON_MED:
            medButton.setTypeface(Typeface.DEFAULT);
            nonMedButton.setTypeface(Typeface.DEFAULT_BOLD);                
            mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
            break;

/*        
        	case UsbongConstants.PRODUCT_TYPE_BOOKS:
                booksButton.setTypeface(Typeface.DEFAULT_BOLD);
                childrensButton.setTypeface(Typeface.DEFAULT);                
                textbooksButton.setTypeface(Typeface.DEFAULT);
                medicalButton.setTypeface(Typeface.DEFAULT);
                foodButton.setTypeface(Typeface.DEFAULT);
                beveragesButton.setTypeface(Typeface.DEFAULT);
                comicsButton.setTypeface(Typeface.DEFAULT);
                mangaButton.setTypeface(Typeface.DEFAULT);
                toysAndCollectiblesButton.setTypeface(Typeface.DEFAULT);               
                miscellaneousButton.setTypeface(Typeface.DEFAULT);
                promosButton.setTypeface(Typeface.DEFAULT);
                mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
                break;
        	case UsbongConstants.PRODUCT_TYPE_CHILDRENS:
                booksButton.setTypeface(Typeface.DEFAULT);
                childrensButton.setTypeface(Typeface.DEFAULT_BOLD);                
                textbooksButton.setTypeface(Typeface.DEFAULT);
                medicalButton.setTypeface(Typeface.DEFAULT);
                foodButton.setTypeface(Typeface.DEFAULT);
                beveragesButton.setTypeface(Typeface.DEFAULT);
                comicsButton.setTypeface(Typeface.DEFAULT);
                mangaButton.setTypeface(Typeface.DEFAULT);
                toysAndCollectiblesButton.setTypeface(Typeface.DEFAULT);               
                miscellaneousButton.setTypeface(Typeface.DEFAULT);
                promosButton.setTypeface(Typeface.DEFAULT);
                mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
                break;
        	case UsbongConstants.PRODUCT_TYPE_TEXTBOOKS:
                booksButton.setTypeface(Typeface.DEFAULT);
                childrensButton.setTypeface(Typeface.DEFAULT);                
                textbooksButton.setTypeface(Typeface.DEFAULT_BOLD);
                medicalButton.setTypeface(Typeface.DEFAULT);
                foodButton.setTypeface(Typeface.DEFAULT);
                beveragesButton.setTypeface(Typeface.DEFAULT);
                comicsButton.setTypeface(Typeface.DEFAULT);
                mangaButton.setTypeface(Typeface.DEFAULT);
                toysAndCollectiblesButton.setTypeface(Typeface.DEFAULT);               
                miscellaneousButton.setTypeface(Typeface.DEFAULT);
                promosButton.setTypeface(Typeface.DEFAULT);
                mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
                break;
        	case UsbongConstants.PRODUCT_TYPE_PROMOS:
                booksButton.setTypeface(Typeface.DEFAULT);
                childrensButton.setTypeface(Typeface.DEFAULT);                
                textbooksButton.setTypeface(Typeface.DEFAULT);
                medicalButton.setTypeface(Typeface.DEFAULT_BOLD);            
                foodButton.setTypeface(Typeface.DEFAULT);
                beveragesButton.setTypeface(Typeface.DEFAULT);
                comicsButton.setTypeface(Typeface.DEFAULT);
                mangaButton.setTypeface(Typeface.DEFAULT);
                toysAndCollectiblesButton.setTypeface(Typeface.DEFAULT);               
                miscellaneousButton.setTypeface(Typeface.DEFAULT);
                promosButton.setTypeface(Typeface.DEFAULT);
                mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader_alternative, listOfTreesArrayList);        	
                break;
        	case UsbongConstants.PRODUCT_TYPE_FOOD:
                booksButton.setTypeface(Typeface.DEFAULT);
                childrensButton.setTypeface(Typeface.DEFAULT);                
                textbooksButton.setTypeface(Typeface.DEFAULT);
                medicalButton.setTypeface(Typeface.DEFAULT);            
                foodButton.setTypeface(Typeface.DEFAULT_BOLD);
                beveragesButton.setTypeface(Typeface.DEFAULT);
                comicsButton.setTypeface(Typeface.DEFAULT);
                mangaButton.setTypeface(Typeface.DEFAULT);
                toysAndCollectiblesButton.setTypeface(Typeface.DEFAULT);               
                miscellaneousButton.setTypeface(Typeface.DEFAULT);
                promosButton.setTypeface(Typeface.DEFAULT);            
                mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
                break;
        	case UsbongConstants.PRODUCT_TYPE_BEVERAGES:
                booksButton.setTypeface(Typeface.DEFAULT);
                childrensButton.setTypeface(Typeface.DEFAULT);                
                textbooksButton.setTypeface(Typeface.DEFAULT);
                medicalButton.setTypeface(Typeface.DEFAULT);            
                foodButton.setTypeface(Typeface.DEFAULT);
                beveragesButton.setTypeface(Typeface.DEFAULT_BOLD);
                comicsButton.setTypeface(Typeface.DEFAULT);
                mangaButton.setTypeface(Typeface.DEFAULT);
                toysAndCollectiblesButton.setTypeface(Typeface.DEFAULT);               
                miscellaneousButton.setTypeface(Typeface.DEFAULT);
                promosButton.setTypeface(Typeface.DEFAULT);            
                mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
                break;
        	case UsbongConstants.PRODUCT_TYPE_COMICS:
                booksButton.setTypeface(Typeface.DEFAULT);
                childrensButton.setTypeface(Typeface.DEFAULT);                
                textbooksButton.setTypeface(Typeface.DEFAULT);
                medicalButton.setTypeface(Typeface.DEFAULT);            
                foodButton.setTypeface(Typeface.DEFAULT);
                beveragesButton.setTypeface(Typeface.DEFAULT);
                comicsButton.setTypeface(Typeface.DEFAULT_BOLD);
                mangaButton.setTypeface(Typeface.DEFAULT);
                toysAndCollectiblesButton.setTypeface(Typeface.DEFAULT);               
                miscellaneousButton.setTypeface(Typeface.DEFAULT);
                promosButton.setTypeface(Typeface.DEFAULT);            
                mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
                break;
        	case UsbongConstants.PRODUCT_TYPE_MANGA:
                booksButton.setTypeface(Typeface.DEFAULT);
                childrensButton.setTypeface(Typeface.DEFAULT);                
                textbooksButton.setTypeface(Typeface.DEFAULT);
                medicalButton.setTypeface(Typeface.DEFAULT);            
                foodButton.setTypeface(Typeface.DEFAULT);
                beveragesButton.setTypeface(Typeface.DEFAULT);
                comicsButton.setTypeface(Typeface.DEFAULT);
                mangaButton.setTypeface(Typeface.DEFAULT_BOLD);
                toysAndCollectiblesButton.setTypeface(Typeface.DEFAULT);               
                miscellaneousButton.setTypeface(Typeface.DEFAULT);
                promosButton.setTypeface(Typeface.DEFAULT);            
                mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
                break;
        	case UsbongConstants.PRODUCT_TYPE_TOYS_AND_COLLECTIBLES:
                booksButton.setTypeface(Typeface.DEFAULT);
                childrensButton.setTypeface(Typeface.DEFAULT);                
                textbooksButton.setTypeface(Typeface.DEFAULT);
                medicalButton.setTypeface(Typeface.DEFAULT);            
                foodButton.setTypeface(Typeface.DEFAULT);
                beveragesButton.setTypeface(Typeface.DEFAULT);
                comicsButton.setTypeface(Typeface.DEFAULT);
                mangaButton.setTypeface(Typeface.DEFAULT);
                toysAndCollectiblesButton.setTypeface(Typeface.DEFAULT_BOLD);               
                miscellaneousButton.setTypeface(Typeface.DEFAULT);
                promosButton.setTypeface(Typeface.DEFAULT);            
                mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
                break;
        	case UsbongConstants.PRODUCT_TYPE_MISCELLANEOUS:
                booksButton.setTypeface(Typeface.DEFAULT);
                childrensButton.setTypeface(Typeface.DEFAULT);                
                textbooksButton.setTypeface(Typeface.DEFAULT);
                medicalButton.setTypeface(Typeface.DEFAULT);            
                foodButton.setTypeface(Typeface.DEFAULT);
                beveragesButton.setTypeface(Typeface.DEFAULT);
                comicsButton.setTypeface(Typeface.DEFAULT);
                mangaButton.setTypeface(Typeface.DEFAULT);
                toysAndCollectiblesButton.setTypeface(Typeface.DEFAULT);               
                miscellaneousButton.setTypeface(Typeface.DEFAULT_BOLD);
                promosButton.setTypeface(Typeface.DEFAULT);            
                mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
                break;
*/                
        }
		mCustomAdapter.sort(); //edited by Mike, 20170203
		
/*
		//Reference: http://stackoverflow.com/questions/8908549/sorting-of-listview-by-name-of-the-product-using-custom-adaptor;
		//last accessed: 2 Jan. 2014; answer by Alex Lockwood
		mCustomAdapter.sort(new Comparator<String>() {
		    public int compare(String arg0, String arg1) {
		        return arg0.compareTo(arg1);
		    }
		});
*/		
		treesListView = (ListView)findViewById(R.id.tree_list_view);
		treesListView.setLongClickable(true);
		treesListView.setAdapter(mCustomAdapter);
/*
    	String pleaseMakeSureThatXMLTreeExistsString = (String) getResources().getText(R.string.pleaseMakeSureThatXMLTreeExistsString);
    	String alertString = (String) getResources().getText(R.string.alertStringValueEnglish);

		if (listOfTreesArrayList.isEmpty()){
        	new AlertDialog.Builder(UsbongMainActivity.this).setTitle(alertString)
			.setMessage(pleaseMakeSureThatXMLTreeExistsString)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {					
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//UsbongDecisionTreeEngineActivity is already the Main Menu
//
//					finish();    
//					Intent toUsbongMainActivityIntent = new Intent(UsbongDecisionTreeEngineActivity.this, UsbongMainActivity.class);
//					toUsbongMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
//					startActivity(toUsbongMainActivityIntent);
//					
				}
			}).show();	        		        	
		  }				  
*/		  
	}
    
	public void initTreeLoaderDynamically(int currProductTypeId) {
        this.currProductTypeId = currProductTypeId;
        initTreeLoaderDynamically();
    }
	
	public void initTreeLoaderDynamically()
	{
		isInTreeLoader=true;
		
		//edited by Mike, 20170530
		if (!hasPerformedSearch) {
			performSearch(null);			
		}	
		else {
			hasPerformedSearch=false;
		}

/*		
		for (int i=0; i<categoryButtonsList.size(); i++) {
			Log.d(">>>>>", "cat name: "+categoryButtonsList.get(i).getText().toString());
		}
		categoryButtonsList.get(0).setTypeface(Typeface.DEFAULT_BOLD);						 				
*/
		
		for (int i=0; i<categoryListInteger.size(); i++) {
/*			
			if (categoryListInteger.get(i)==0) {
				categoryButtonsList.get(0).setTypeface(Typeface.DEFAULT_BOLD);						 							
			}
			else {
				categoryButtonsList.get(i).setTypeface(Typeface.DEFAULT);				
			}
*/						
			if (categoryListInteger.get(i)==currProductTypeId) {
				categoryButtonsList.get(i).setTypeface(Typeface.DEFAULT_BOLD);			    
			}
			else {
				categoryButtonsList.get(i).setTypeface(Typeface.DEFAULT);
			}			
		}
        mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader, listOfTreesArrayList);
		mCustomAdapter.sort(); //edited by Mike, 20170203
		
/*
		//Reference: http://stackoverflow.com/questions/8908549/sorting-of-listview-by-name-of-the-product-using-custom-adaptor;
		//last accessed: 2 Jan. 2014; answer by Alex Lockwood
		mCustomAdapter.sort(new Comparator<String>() {
		    public int compare(String arg0, String arg1) {
		        return arg0.compareTo(arg1);
		    }
		});
*/		
		treesListView = (ListView)findViewById(R.id.tree_list_view);
		treesListView.setLongClickable(true);
		treesListView.setAdapter(mCustomAdapter);
/*
    	String pleaseMakeSureThatXMLTreeExistsString = (String) getResources().getText(R.string.pleaseMakeSureThatXMLTreeExistsString);
    	String alertString = (String) getResources().getText(R.string.alertStringValueEnglish);
*/
		if (listOfTreesArrayList.isEmpty()){
        	new AlertDialog.Builder(UsbongMainActivity.this).setTitle("Hey!")
//			.setMessage(pleaseMakeSureThatXMLTreeExistsString)
			.setMessage("There are presently no items in this category.")
        	.setPositiveButton("OK", new DialogInterface.OnClickListener() {					
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//UsbongDecisionTreeEngineActivity is already the Main Menu
/*
					finish();    
					Intent toUsbongMainActivityIntent = new Intent(UsbongDecisionTreeEngineActivity.this, UsbongMainActivity.class);
					toUsbongMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					startActivity(toUsbongMainActivityIntent);
*/					
				}
			}).show();	        		        	
		  }				  
	}
	
    public void reset() {
    	UsbongUtils.generateDateTimeStamp(); //create a new timestamp for this "New Entry"
    }

    //added by Mike, 29 July 2015
    //Reference: http://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android;
    //last accessed: 29 Sept. 2015; answer by Nishant, 2 May 2012; edited by Daniel Nugent, 9 July 2015
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
            	if (myProgressDialog!=null) { 
            		myProgressDialog.dismiss();
            	}
//                String result=data.getStringExtra("result");
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }            
        }                    
    }//onActivityResult

    //added by Mike, July 2, 2015
    @Override
	public void onBackPressed() {
    	//Reference: http://stackoverflow.com/questions/11495188/how-to-put-application-to-background
    	//; last accessed: 14 April 2015, answer by: JavaCoderEx
    	Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);    
    }
    
    //added by Mike, 25 Sept. 2015
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.standard_menu, menu);
		
		//added by Mike, 20170525
		UsbongUtils.cartIcon = menu.findItem(R.id.cart).setIcon(UsbongUtils.cartIconDrawableResourceId);		

		return super.onCreateOptionsMenu(menu); 
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{		
		switch(item.getItemId())
		{
/*		
			case(R.id.settings):
				//Reference: http://stackoverflow.com/questions/16954196/alertdialog-with-checkbox-in-android;
				//last accessed: 20160408; answer by: kamal; edited by: Empty2K12
				final CharSequence[] items = {UsbongConstants.AUTO_NARRATE_STRING, UsbongConstants.AUTO_PLAY_STRING, UsbongConstants.AUTO_LOOP_STRING};
				// arraylist to keep the selected items
				UsbongDecisionTreeEngineActivity.selectedSettingsItems=new ArrayList<Integer>();
				
				//check saved settings
				if (UsbongUtils.IS_IN_AUTO_NARRATE_MODE) {					
					UsbongDecisionTreeEngineActivity.selectedSettingsItems.add(UsbongConstants.AUTO_NARRATE);			
				}
				if (UsbongUtils.IS_IN_AUTO_PLAY_MODE) {
					UsbongDecisionTreeEngineActivity.selectedSettingsItems.add(UsbongConstants.AUTO_PLAY);	
					UsbongDecisionTreeEngineActivity.selectedSettingsItems.add(UsbongConstants.AUTO_NARRATE); //if AUTO_PLAY is checked, AUTO_NARRATE should also be checked
		    	}	        				
				if (UsbongUtils.IS_IN_AUTO_LOOP_MODE) {					
					UsbongDecisionTreeEngineActivity.selectedSettingsItems.add(UsbongConstants.AUTO_LOOP);			
				}
			    
				UsbongDecisionTreeEngineActivity.selectedSettingsItemsInBoolean = new boolean[items.length];
			    for(int k=0; k<items.length; k++) {
			    	UsbongDecisionTreeEngineActivity.selectedSettingsItemsInBoolean[k] = false;			    		
			    }
			    for(int i=0; i<UsbongDecisionTreeEngineActivity.selectedSettingsItems.size(); i++) {
			    	UsbongDecisionTreeEngineActivity.selectedSettingsItemsInBoolean[UsbongDecisionTreeEngineActivity.selectedSettingsItems.get(i)] = true;
			    }
			    		    
			    inAppSettingsDialog = new AlertDialog.Builder(this)
				.setTitle("Settings")
				.setMultiChoiceItems(items, UsbongDecisionTreeEngineActivity.selectedSettingsItemsInBoolean, new DialogInterface.OnMultiChoiceClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
				    	Log.d(">>>","onClick");

				    	if (isChecked) {
				            // If the user checked the item, add it to the selected items
				    		UsbongDecisionTreeEngineActivity.selectedSettingsItems.add(indexSelected);
				            if ((indexSelected==UsbongConstants.AUTO_PLAY) 
					        		&& !UsbongDecisionTreeEngineActivity.selectedSettingsItems.contains(UsbongConstants.AUTO_NARRATE)) {
				                final ListView list = inAppSettingsDialog.getListView();
				                list.setItemChecked(UsbongConstants.AUTO_NARRATE, true);
				            }				           
				        } else if (UsbongDecisionTreeEngineActivity.selectedSettingsItems.contains(indexSelected)) {
				        	if ((indexSelected==UsbongConstants.AUTO_NARRATE) 
				        		&& UsbongDecisionTreeEngineActivity.selectedSettingsItems.contains(UsbongConstants.AUTO_PLAY)) {
				                final ListView list = inAppSettingsDialog.getListView();
				                list.setItemChecked(indexSelected, false);
				        	}
				        	else {        	
					            // Else, if the item is already in the array, remove it
				        		UsbongDecisionTreeEngineActivity.selectedSettingsItems.remove(Integer.valueOf(indexSelected));
				        	}
				        }
				        
				        //updated selectedSettingsItemsInBoolean
					    for(int k=0; k<items.length; k++) {
					    	UsbongDecisionTreeEngineActivity.selectedSettingsItemsInBoolean[k] = false;			    		
					    }
					    for(int i=0; i<UsbongDecisionTreeEngineActivity.selectedSettingsItems.size(); i++) {
					    	UsbongDecisionTreeEngineActivity.selectedSettingsItemsInBoolean[UsbongDecisionTreeEngineActivity.selectedSettingsItems.get(i)] = true;
					    }
				    }
				}).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int id) {
				    	 try {	    	
				 			InputStreamReader reader = UsbongUtils.getFileFromSDCardAsReader(UsbongUtils.BASE_FILE_PATH + "usbong.config");	
				 			BufferedReader br = new BufferedReader(reader);    		
				 	    	String currLineString;        	

				 	    	//write first to a temporary file
							PrintWriter out = UsbongUtils.getFileFromSDCardAsWriter(UsbongUtils.BASE_FILE_PATH + "usbong.config" +"TEMP");

				 	    	while((currLineString=br.readLine())!=null)
				 	    	{ 	
				 	    		Log.d(">>>", "currLineString: "+currLineString);
								if ((currLineString.contains("IS_IN_AUTO_NARRATE_MODE="))
								|| (currLineString.contains("IS_IN_AUTO_PLAY_MODE="))
								|| (currLineString.contains("IS_IN_AUTO_LOOP_MODE="))) {
									continue;
								}	
								else {
									out.println(currLineString);			 	    		
								}
				 	    	}	        				

							for (int i=0; i<items.length; i++) {
								Log.d(">>>>", i+"");
								if (UsbongDecisionTreeEngineActivity.selectedSettingsItemsInBoolean[i]==true) {
									if (i==UsbongConstants.AUTO_NARRATE) {
							    		out.println("IS_IN_AUTO_NARRATE_MODE=ON");
							    		UsbongUtils.IS_IN_AUTO_NARRATE_MODE=true;							
									}								
									else if (i==UsbongConstants.AUTO_PLAY) {
							    		out.println("IS_IN_AUTO_PLAY_MODE=ON");
							    		UsbongUtils.IS_IN_AUTO_PLAY_MODE=true;						
									}	
									else if (i==UsbongConstants.AUTO_LOOP) {
							    		out.println("IS_IN_AUTO_LOOP_MODE=ON");
							    		UsbongUtils.IS_IN_AUTO_LOOP_MODE=true;						
									}
								}
								else {
									if (i==UsbongConstants.AUTO_NARRATE) {
							    		out.println("IS_IN_AUTO_NARRATE_MODE=OFF");
							    		UsbongUtils.IS_IN_AUTO_NARRATE_MODE=false;															
									}							
									else if (i==UsbongConstants.AUTO_PLAY) {
							    		out.println("IS_IN_AUTO_PLAY_MODE=OFF");
							    		UsbongUtils.IS_IN_AUTO_PLAY_MODE=false;	
									}
									else if (i==UsbongConstants.AUTO_LOOP) {
							    		out.println("IS_IN_AUTO_LOOP_MODE=OFF");
							    		UsbongUtils.IS_IN_AUTO_LOOP_MODE=false;	
									}
								}				
							}					
					    	out.close(); //remember to close
					    	
					    	//copy temp file to actual usbong.config file
				 			InputStreamReader reader2 = UsbongUtils.getFileFromSDCardAsReader(UsbongUtils.BASE_FILE_PATH + "usbong.config"+"TEMP");	
				 			BufferedReader br2 = new BufferedReader(reader2);    		
				 	    	String currLineString2;        	

				 	    	//write to actual usbong.config file
							PrintWriter out2 = UsbongUtils.getFileFromSDCardAsWriter(UsbongUtils.BASE_FILE_PATH + "usbong.config");

				 	    	while((currLineString2=br2.readLine())!=null)
				 	    	{ 	
								out2.println(currLineString2);			 	    		
				 	    	}			 	    	
				 	    	out2.close();
				 	    	
				 	    	UsbongUtils.deleteRecursive(new File(UsbongUtils.BASE_FILE_PATH + "usbong.config"+"TEMP"));
				 		}
				 		catch(Exception e) {
				 			e.printStackTrace();
				 		}			 		
				    }
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int id) {
				        //  Your code when user clicked on Cancel
				    }
				}).create();
				inAppSettingsDialog.show();
					return true;
*/					
			case(R.id.cart): //added by Mike, 20170427
				if ((UsbongUtils.itemsInCart==null) || (UsbongUtils.itemsInCart.isEmpty())) {
			    	AlertDialog.Builder emptyCartAlertDialog = new AlertDialog.Builder(UsbongMainActivity.this).setTitle("Your Shopping Cart");
					TextView tv = new TextView(this);
					tv.setText("\nIt is currently empty.");
					tv.setGravity(Gravity.CENTER_HORIZONTAL);
					tv.setTextSize(16);
					emptyCartAlertDialog.setView(tv);
					emptyCartAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
				}
				else {
					finish();
					//added by Mike, 20170216
					Intent toCartActivityIntent = new Intent().setClass(getInstance(), CartActivity.class);
	//				toCartActivityIntent.putExtra("newSellActivity", true); //added by Mike, 20170328
					toCartActivityIntent.putExtra("activityCaller", UsbongConstants.USBONG_MAIN_ACTIVITY); //added by Mike, 20170525	            				
					toCartActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(toCartActivityIntent);
				}
				return true;
			case(R.id.sell): //added by Mike, 20170308
				finish();
				//added by Mike, 20170216
				Intent toSellActivityIntent = new Intent().setClass(getInstance(), SellActivity.class);
				toSellActivityIntent.putExtra("newSellActivity", true); //added by Mike, 20170328
				toSellActivityIntent.putExtra("activityCaller", UsbongConstants.USBONG_MAIN_ACTIVITY); //added by Mike, 20170525	            				
				toSellActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(toSellActivityIntent);
				return true;
			case(R.id.request):
				finish();
				//added by Mike, 20170216
				Intent toRequestActivityIntent = new Intent().setClass(getInstance(), RequestActivity.class);
				toRequestActivityIntent.putExtra("newRequestActivity", true); //added by Mike, 20170330
				toRequestActivityIntent.putExtra("activityCaller", UsbongConstants.USBONG_MAIN_ACTIVITY); //added by Mike, 20170525	            				
				toRequestActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(toRequestActivityIntent);
				return true;
			case(R.id.contact):
				finish();
				//added by Mike, 20170216
				Intent toContactActivityIntent = new Intent().setClass(getInstance(), ContactActivity.class);
				toContactActivityIntent.putExtra("newContactActivity", true); //added by Mike, 20180204
				toContactActivityIntent.putExtra("activityCaller", UsbongConstants.USBONG_MAIN_ACTIVITY); //added by Mike, 20180204         				
				toContactActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(toContactActivityIntent);
				return true;
			case(R.id.refresh):
				new UsbongHTTPConnect(this).execute();
				return true;
			case(R.id.about):
		    	new AlertDialog.Builder(UsbongMainActivity.this).setTitle("About")
				.setMessage(UsbongUtils.readTextFileInAssetsFolder(UsbongMainActivity.this,"credits.txt")) //don't add a '/', otherwise the file would not be found
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
				return true;
			case(R.id.account):
				final EditText firstName = new EditText(this);
				firstName.setHint("First Name");
				final EditText surName = new EditText(this);
				surName.setHint("Surname");
				final EditText contactNumber = new EditText(this);
				contactNumber.setHint("Contact Number");
				contactNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
	/*				
				//added by Mike, 20170223
				final RadioGroup preference = new RadioGroup(this);
				preference.setOrientation(RadioGroup.HORIZONTAL);
				
				RadioButton meetup = new AppCompatRadioButton(this);
				meetup.setText("Meet-up");
				preference.addView(meetup);
								
				RadioButton shipping = new AppCompatRadioButton(this);
				shipping.setText("Shipping");
				preference.addView(shipping);				
	*/				
				final EditText shippingAddress = new EditText(this);
				shippingAddress.setHint("Shipping Address");
				shippingAddress.setMinLines(5);
	
				//added by Mike, 20170223
				final RadioGroup modeOfPayment = new RadioGroup(this);
				modeOfPayment.setOrientation(RadioGroup.VERTICAL);
	/*				
				RadioButton cashUponMeetup = new AppCompatRadioButton(this);
				cashUponMeetup.setText("Cash upon meet-up");
				modeOfPayment.addView(cashUponMeetup);
	*/									
				RadioButton bankDeposit = new AppCompatRadioButton(this);
				bankDeposit.setText("Bank Deposit (BDO/BPI)");
				modeOfPayment.addView(bankDeposit);
	/*
				RadioButton peraPadala = new AppCompatRadioButton(this);
				peraPadala.setText("Pera Padala");
				modeOfPayment.addView(peraPadala);
	*/
				RadioButton paypal = new AppCompatRadioButton(this);
				paypal.setText("PayPal");
				modeOfPayment.addView(paypal);
				
				RadioButton meetupAtMOSC = new AppCompatRadioButton(this);
				meetupAtMOSC.setText("Meetup at MOSC (Less ₱70)");
				modeOfPayment.addView(meetupAtMOSC);

			    //Reference: http://stackoverflow.com/questions/23024831/android-shared-preferences-example
		        //; last accessed: 20150609
		        //answer by Elenasys
		        //added by Mike, 20150207
		        SharedPreferences prefs = getSharedPreferences(UsbongConstants.MY_ACCOUNT_DETAILS, MODE_PRIVATE);
		        if (prefs!=null) {
		          firstName.setText(prefs.getString("firstName", ""));//"" is the default value.
		          surName.setText(prefs.getString("surname", "")); //"" is the default value.
		          contactNumber.setText(prefs.getString("contactNumber", "")); //"" is the default value.
	/*
		          //added by Mike, 20170223
		          ((RadioButton)preference.getChildAt(prefs.getInt("preference", UsbongConstants.defaultPreference))).setChecked(true);
	*/				  		          
		          shippingAddress.setText(prefs.getString("shippingAddress", "")); //"" is the default value.
		          
			      //added by Mike, 20170223				  
		          ((RadioButton)modeOfPayment.getChildAt(prefs.getInt("modeOfPayment", UsbongConstants.defaultModeOfPayment))).setChecked(true);
		        }
				
				LinearLayout ll=new LinearLayout(this);
				ll.setOrientation(LinearLayout.VERTICAL);
				ll.addView(firstName);
				ll.addView(surName);
				ll.addView(contactNumber);
	/*				ll.addView(preference);*/
				ll.addView(shippingAddress);				
				ll.addView(modeOfPayment);
	
				new AlertDialog.Builder(this).setTitle("My Account")
				.setView(ll)
				.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int id) {
				        //ACTION
				    }
				})
				.setPositiveButton("Save & Exit",  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int id) {
				        //ACTION
				        //Reference: http://stackoverflow.com/questions/23024831/android-shared-preferences-example
				        //; last accessed: 20150609
				        //answer by Elenasys
				        //added by Mike, 20170207
				        SharedPreferences.Editor editor = getSharedPreferences(UsbongConstants.MY_ACCOUNT_DETAILS, MODE_PRIVATE).edit();
				        editor.putString("firstName", firstName.getText().toString());
				        editor.putString("surname", surName.getText().toString());
				        editor.putString("contactNumber", contactNumber.getText().toString());
	/*
				        for (int i=0; i< preference.getChildCount(); i++) {
				        	if (((RadioButton)preference.getChildAt(i)).isChecked()) {
				        		currPreference=i;
				        	}
				        }
				        editor.putInt("preference", currPreference); //added by Mike, 20170223				        
	*/				        
				        editor.putString("shippingAddress", shippingAddress.getText().toString());
	
				        for (int i=0; i< modeOfPayment.getChildCount(); i++) {
				        	if (((RadioButton)modeOfPayment.getChildAt(i)).isChecked()) {
				        		currModeOfPayment=i;
				        	}
				        }
				        editor.putInt("modeOfPayment", currModeOfPayment); //added by Mike, 20170223
				        editor.commit();		
/*				        
				        if (currScreen!=BUY_SCREEN) {
					        //added by Mike, 20170222
					        setContentView(R.layout.account);	
					        init();				        	
				        }
*/				        
				    }
				}).show();
				return true;
			case android.R.id.home: //added by Mike, 22 Sept. 2015
	/*//commented out by Mike, 201702014; UsbongDecisionTreeEngineActivity is already the main menu				
				processReturnToMainMenuActivity();
	*/				    	//added by Mike, 20170216
				//return to UsbongDecisionTreeEngineActivity
				//edited by Mike, 20170525
				finish();
				Intent toMainActivityIntent = new Intent(getInstance(), UsbongMainActivity.class);
				toMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				startActivity(toMainActivityIntent);
		        return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private class CustomDataAdapter extends ArrayAdapter<String>
	{
		private ArrayList<String> items;
		
		public CustomDataAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
            super(context, textViewResourceId, items);
            this.items = items;            
		}
		
		//added by Mike, 20170203
		public void sort() {
//			Collections.sort(items);
			
			//Reference: http://stackoverflow.com/questions/8908549/sorting-of-listview-by-name-of-the-product-using-custom-adaptor;
			//last accessed: 2 Jan. 2014; answer by Alex Lockwood
			Collections.sort(items, new Comparator<String>() {
			    public int compare(String arg0, String arg1) {
			        return arg0.compareTo(arg1);
			    }
			});			
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
            	final String o = items.get(position);
            	final View v; //edited by Mike, 20170530
            	
/*                View v = convertView;
                if (v == null) {
*/                
            	//Reference: https://stackoverflow.com/questions/14156996/out-of-memory-on-android-app-on-scrolling-list-with-images;
            	//last accessed: 20170607
            	//answer by: dev_android
            	if (convertView == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
/*                    if (o.contains("PROMO")||o.contains("RetroCC")||o.toLowerCase().contains("manga")) { //TODO: make this more generic 
                        v = vi.inflate(R.layout.tree_loader_alternative, null);                    	
                    }
                    else {*/
                        v = vi.inflate(R.layout.tree_loader, null);                
/*                  } 
 */
            	}
            	else {
            		v = convertView;
            	}

/*
                    switch (currCategory) {
                    	case UsbongConstants.ITEMS_LIST_COMBOS:
                            v = vi.inflate(R.layout.tree_loader_alternative, null);
                            break;
                        default:
//                    	case UsbongConstants.ITEMS_LIST_BOOKS:
//                    	case UsbongConstants.ITEMS_LIST_TEAS:
                            v = vi.inflate(R.layout.tree_loader, null);
                        	break;                            
                    }
*/                    
/*                    
                }
*/
//                final String o = items.get(position);
                if (o != null) {
                	try {       
                    	TextView dataCurrentTextView = (TextView)v.findViewById(R.id.tree_item);
                    	//edited by Mike, 20170522
                    	final String s;
                    	String tempS;
                    	
                    	//added by Mike, 20170810
	    				String tempS2 = o.toString();
	    				String currProductTypeIdWithLabel = tempS2.substring(tempS2.indexOf("currProductTypeId: "), tempS2.indexOf("productOverview: "));
	    				currProductTypeId = Integer.parseInt(currProductTypeIdWithLabel.substring("currProductTypeId: ".length()));
	    						
	    				//added by Mike, 20180419
	    				String tempS3 = o.toString();
	    				final String currProductOverview = tempS3.substring(tempS3.indexOf("productOverview: ")+"productOverview: ".length()).toString();
	    				
	            		//Reference: http://www.anddev.org/tinytut_-_get_resources_by_name__getidentifier_-t460.html; last accessed 14 Sept 2011
	                    Resources myRes = instance.getResources();
	                    final String imageFileName;	                    
	                    final String folderName; //added by Mike, 20170725
	                    	                	
                    	switch(currProductTypeId) {
/*                    	
			    			case UsbongConstants.PRODUCT_TYPE_BEVERAGES:
			    				folderName = UsbongConstants.PRODUCT_TYPE_BEVERAGES_STRING;
			    				break;
			    			case UsbongConstants.PRODUCT_TYPE_MEDICAL:
			    				folderName = UsbongConstants.PRODUCT_TYPE_MEDICAL_STRING;
			    				break;
			    			case UsbongConstants.PRODUCT_TYPE_PROMOS:
			    				folderName = UsbongConstants.PRODUCT_TYPE_PROMOS_STRING;
			    				break;
			    			case UsbongConstants.PRODUCT_TYPE_COMICS:
			    				folderName = UsbongConstants.PRODUCT_TYPE_COMICS_STRING;
			    				break;
			    			case UsbongConstants.PRODUCT_TYPE_MANGA:
			    				folderName = UsbongConstants.PRODUCT_TYPE_MANGA_STRING;
			    				break;
			    			case UsbongConstants.PRODUCT_TYPE_TOYS_AND_COLLECTIBLES:
			    				folderName = UsbongConstants.PRODUCT_TYPE_TOYS_AND_COLLECTIBLES_STRING;
			    				break;		
			    			case UsbongConstants.PRODUCT_TYPE_TEXTBOOKS:
			    				folderName = UsbongConstants.PRODUCT_TYPE_TEXTBOOKS_STRING;
			    				break;		
			    			case UsbongConstants.PRODUCT_TYPE_CHILDRENS:
			    				folderName = UsbongConstants.PRODUCT_TYPE_CHILDRENS_STRING;
			    				break;		
			    			case UsbongConstants.PRODUCT_TYPE_FOOD:
			    				folderName = UsbongConstants.PRODUCT_TYPE_FOOD_STRING;
			    				break;		
			    			case UsbongConstants.PRODUCT_TYPE_MISCELLANEOUS:
			    				folderName = UsbongConstants.PRODUCT_TYPE_MISCELLANEOUS_STRING;
			    				break;		
			    			default:
			    				folderName = UsbongConstants.PRODUCT_TYPE_BOOKS_STRING;
			    				break;
*/			    				
                    		//edited by Mike, 20180427
			    			case UsbongConstants.PRODUCT_TYPE_MED:
			    				folderName = UsbongConstants.PRODUCT_TYPE_MED_STRING;
			    				break;		
			    			default:
			    				folderName = UsbongConstants.PRODUCT_TYPE_NON_MED_STRING;
			    				break;
                    	}
	                    
                    	switch(currProductTypeId) {
/*//commented out by Mike, 20180427                    	
			    			case UsbongConstants.PRODUCT_TYPE_BEVERAGES:		                    		
			    				tempS = o.toString().replace("\n", "<br>");
			    				s = tempS.subSequence(0, tempS.indexOf("MerchantName: ")).toString();
			    				imageFileName = folderName+"/"+o.toString().substring(0, o.toString().indexOf("</b>"))
		                    		.replace("<b>","")
		                    		.replace("’","")
		                    		.replace("'","")
		                    		.replace(":","")+".jpg"; //edited by Mike, 20170202
			    				break;
*/			    				
			    			default:
			    				tempS = o.toString().replace("\n", "<br>");
			    				s = tempS.subSequence(0, tempS.indexOf("MerchantName: ")).toString();
			                    imageFileName = folderName+"/"+o.toString().substring(0/*o.indexOf("T")*/, o.toString().indexOf("</b>"))
			                    		.replace("<b>","")
			                    		.replace("’","")
			                    		.replace("'","")
			                    		.replace(":","")+".jpg"; //edited by Mike, 20170202
			    				break;
                    	}

                    	//edited by Mike, 20180311
                    	InputStream myInputStream;
                    	
            		    List<String> myList = Arrays.asList(myRes.getAssets().list(folderName));
            		    if (myList.contains(imageFileName.replace(folderName +"/",""))) {
/*
                    	if (Arrays.asList(getResources().getAssets().list("folderName")).contains(imageFileName.replace("/"+folderName,""))) {
*/                    	
                        	myInputStream = myRes.getAssets().open(imageFileName);
                    	}
                    	else {
                    		myInputStream = UsbongUtils.getFileFromSDCardAsInputStream(UsbongUtils.BASE_IMAGE_FILE_PATH + imageFileName);
                    	}
                    	
	                    final Drawable myDrawableImage = Drawable.createFromStream(myInputStream, null); 

	                    final ImageView image = (ImageView) v.findViewById(R.id.tree_item_image_view);
		            	
                        final Button merchantNameButton = (Button)v.findViewById(R.id.merchant_button);
                        int index = o.indexOf("MerchantName: ")+"MerchantName: ".length();
                        int endIndex = o.indexOf("currProductTypeId: ");
                        merchantNameButton.setText("☗ "+o.toString().subSequence(
                        								index, endIndex));
                        merchantNameButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            	loadMerchantStore(merchantNameButton.getText().toString());                            	
                            }
                        });
	            		
	                	dataCurrentTextView.setText(Html.fromHtml(s));
//	                	dataCurrentTextView.setText(o.toString());
	                	dataCurrentTextView.setOnClickListener(new OnClickListener() {
	            			@Override
	            			public void onClick(View v) {
/*	            				
	            				//added by Mike, 20170203
	                        	setVariableOntoMyUsbongVariableMemory(UsbongConstants.ITEM_VARIABLE_NAME, s);
	            				setVariableOntoMyUsbongVariableMemory(UsbongConstants.ITEM_IMAGE_NAME, imageFileName); //added by Mike, 20160203
	                    		image.setImageDrawable(myDrawableImage);		                    		
	                        	initParser(UsbongConstants.TREE_TYPE_BUY);           				
*/
                				//added by Mike, 20170216
	            				Intent toBuyActivityIntent = new Intent().setClass(getInstance(), BuyActivity.class);
	            				toBuyActivityIntent.putExtra(UsbongConstants.ITEM_VARIABLE_NAME, s);
	            				toBuyActivityIntent.putExtra(UsbongConstants.ITEM_IMAGE_NAME, imageFileName);
	            				toBuyActivityIntent.putExtra(UsbongConstants.MERCHANT_NAME, merchantNameButton.getText().toString()); //added by Mike, 20170528        					            				
	            				toBuyActivityIntent.putExtra(UsbongConstants.ITEM_PRODUCT_OVERVIEW, currProductOverview); //added by Mike, 20180419
	            				startActivityForResult(toBuyActivityIntent,1);
	            			}
	                	});
                		image.setImageDrawable(myDrawableImage);                		
/*
                		//added by Mike, 20170203
                		//make the image icon in the list smaller
                		image.setAdjustViewBounds(true);
                		image.setMaxHeight(100);
                		image.setMaxWidth(100);                		
*/                		
                		image.setOnClickListener(new OnClickListener() {
                			@Override
                			public void onClick(View v) {
/*
                				//added by Mike, 20170203
                            	setVariableOntoMyUsbongVariableMemory(UsbongConstants.ITEM_VARIABLE_NAME, s);
                				setVariableOntoMyUsbongVariableMemory(UsbongConstants.ITEM_IMAGE_NAME, imageFileName); //added by Mike, 20160203
                        		image.setImageDrawable(myDrawableImage);	
                				initParser(UsbongConstants.TREE_TYPE_BUY); //added by Mike, 20160202          				                	
*/
                				//added by Mike, 20170216
	            				Intent toBuyActivityIntent = new Intent().setClass(getInstance(), BuyActivity.class);
	            				toBuyActivityIntent.putExtra(UsbongConstants.ITEM_VARIABLE_NAME, s);
	            				toBuyActivityIntent.putExtra(UsbongConstants.ITEM_IMAGE_NAME, imageFileName);
	            				toBuyActivityIntent.putExtra(UsbongConstants.MERCHANT_NAME, merchantNameButton.getText().toString()); //added by Mike, 20170529   				
	            				toBuyActivityIntent.putExtra(UsbongConstants.ITEM_PRODUCT_OVERVIEW, currProductOverview); //added by Mike, 20180419
	            				toBuyActivityIntent.putExtra("activityCaller", UsbongConstants.USBONG_MAIN_ACTIVITY); //added by Mike, 20170525	            				
	            				startActivityForResult(toBuyActivityIntent,1);
                			}
                		});
                	}
	            	catch(Exception e) {
	            		e.printStackTrace();
	            	}
                }
                return v;
        }
	}

	//added by Mike, 20180214
	public void syncDB(String result) {		
		try {			
			JSONArray obj = new JSONArray(result);			

        	myDbHelper = new UsbongDbHelper(this);
            myDbHelper.initializeDataBase();

            mySQLiteDatabase = myDbHelper.getReadableDatabase();
	        myDbHelper.syncInternalDBwithServerDB(mySQLiteDatabase, obj);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
            try {
                myDbHelper.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                myDbHelper.close();
            }        	 
        }
	}
}