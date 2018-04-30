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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import usbong.android.utils.UsbongConstants;
import usbong.android.utils.UsbongUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//commented out by Mike, 20180427
//import usbong.android.pos_app.UsbongDecisionTreeEngineActivity;

/*
 * This is Usbong's Main Menu activity. 
 */
public class CartActivity extends AppCompatActivity/*Activity*/ 
{	
	private final static int SHOPPING_CART_SCREEN=0;
	private final static int ACCOUNT_SCREEN=1;	
	private static int currScreen;

	//added by Mike, 20170427
	public ListView treesListView;	
	private CustomDataAdapter mCustomAdapter;
	private ArrayList<String> listOfTreesArrayList;

	private static CartActivity instance;
	
	private boolean isSendingData;

	//edited by Mike, 20170225
	private static int currPreference=UsbongConstants.defaultPreference; 	
	private static int currModeOfPayment=UsbongConstants.defaultModeOfPayment; 
	
	private String productDetails; //added by Mike, 20170221
		
	private Button confirmButton;
	private Button buyButton; //added by Mike, 20170220	
	private Button backButton;

/*	private Button sellButton;
	private Button requestButton;
*/	
//	private static BuyActivity instance;
				
	public static String timeStamp;
	
//	private static Date startTime;	
	
//commented out by Mike, 20180427
//	protected UsbongDecisionTreeEngineActivity myUsbongDecisionTreeEngineActivity;
	protected SettingsActivity mySettingsActivity;
	
	private static Activity myActivityInstance;
	private ProgressDialog myProgressDialog;
	
    private AlertDialog inAppSettingsDialog; //added by Mike, 20160417
    
    private ArrayList<String> quantityList; //added by Mike, 20170505
    private ArrayList<String> tempList; //added by Mike, 20170511
    private int orderSubtotalCost; //added by Mike, 20170511
/*    
    private int less70pesosPromoTotal; //added by Mike, 20170902
*/    
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);

        instance=this;
        
        //added by Mike, 27 Sept. 2015
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        myActivityInstance = this;
        
        currScreen=SHOPPING_CART_SCREEN;//default; added by Mike, 20170220
        
        //added by Mike, 25 Sept. 2015
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.usbong)); //app_name
//        getSupportActionBar().setDisplayUseLogoEnabled(true);        


	    setContentView(R.layout.cart_tree_list_interface);	        
//	    setContentView(R.layout.buy);	        
/*//commented out by Mike, 20170216
            //added by Mike, 20161117
        	Bundle extras = getIntent().getExtras();
        	if (extras!=null) {
	        	String message = extras.getString("completed_tree");

	        	if (message.equals("true")) {
			        AppRater.showRateDialog(this); 
	        	}	        		
        	}
*/        	
	        reset();
	        initCart();
    }
    
    public Activity getInstance() {
//    	return instance;
    	return myActivityInstance;
    }
    
    /*
     * Initialize this activity
     */
    public void initCart() {

    	//edited by Mike, 20170429
//    	String currCategory = UsbongConstants.ITEMS_LIST_BOOKS;
//        listOfTreesArrayList = UsbongUtils.getItemArrayList(UsbongUtils.USBONG_TREES_FILE_PATH + currCategory+".txt");
    	if (UsbongUtils.itemsInCart!=null) {
	    	Collections.sort(UsbongUtils.itemsInCart); //added by Mike, 20170509
    		listOfTreesArrayList = UsbongUtils.itemsInCart;
    	}        	
    	else {
    		listOfTreesArrayList = new ArrayList<String>();
    	}
/*    	    	
    	int listOfTreesArrayListSize = listOfTreesArrayList.size();  
    	if (listOfTreesArrayListSize==0) {
    		return;
    	}
*/    	
    	String prev="";
    	int quantity=0;
    	tempList = new ArrayList<String>();
    	quantityList = new ArrayList<String>();
    	
/*    	listOfTreesArrayList = noQuantityList;
 */
    	int listOfTreesArrayListSize = listOfTreesArrayList.size();    	
    	
    	if (listOfTreesArrayListSize != 0) {
//	    	Collections.sort(listOfTreesArrayList);
	    	
	    	for (int i=0; i<listOfTreesArrayListSize; i++) {    					    		
	    		if (prev.equals("")) {
	    			quantity++;
	        		prev = listOfTreesArrayList.get(i);    			
	    		}
	    		else if (listOfTreesArrayList.get(i).equals(prev)) {
	    			quantity++;
	    		}
	    		else {
	    			Log.d(">>>>>>listOfTreesArrayList.get(i)", i+": "+listOfTreesArrayList.get(i));
	    			Log.d(">>>>>>prev", prev);
	    			
	    			tempList.add(listOfTreesArrayList.get(i-1).toString());
	    			quantityList.add(""+quantity); //"<b>Quantity:</b> "+quantity
	        		prev = listOfTreesArrayList.get(i);    			
	        		quantity=1;
	    		}
	    	}    	
			tempList.add(listOfTreesArrayList.get(listOfTreesArrayListSize-1));
			quantityList.add(""+quantity); //"<b>Quantity:</b> "+quantity
			//listOfTreesArrayList = tempList;
    	}
    	
    	mCustomAdapter = new CustomDataAdapter(this, R.layout.tree_loader_cart, tempList); //listOfTreesArrayList
//		mCustomAdapter.sort(); //edited by Mike, 20170203
    			
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

		processPromoAndOrderTotal();
		
		//added by Mike, 20170511
		//added by Mike, 20160126
    	confirmButton = (Button)findViewById(R.id.confirm_button);    	
    	confirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {		
				if (currScreen==SHOPPING_CART_SCREEN) { //this should be true					
					//added by Mike, 20170427
//					UsbongUtils.cartIcon.setIcon(R.drawable.cart_icon_not_empty);
					UsbongUtils.cartIconDrawableResourceId = R.drawable.cart_icon_not_empty;
					myActivityInstance.invalidateOptionsMenu();
					
					currScreen=ACCOUNT_SCREEN;
					setContentView(R.layout.account);	
					init();
				}
			}
    	});    	
    }
    
    //edited by Mike, 20170928
    public void processPromoAndOrderTotal() {    	
		orderSubtotalCost = 0;
/*		
    	less70pesosPromoTotal = 0;
*/    	
		for (int i=0; i<tempList.size(); i++) { 
			String s = tempList.get(i); 	

			String sPart1 = s.substring(s.indexOf("₱"));	            				
			String item_price = sPart1.substring(0,sPart1.indexOf("<"));//("("));//(used), (new)				
/*
			less70pesosPromoTotal+=Integer.parseInt(quantityList.get(i))*70;				
*/			
			orderSubtotalCost+=Integer.parseInt(item_price.replace("₱", "").trim())*Integer.parseInt(quantityList.get(i));							
		}	

/*		
		//edited by Mike, 20170930
		if (tempList.size()>0) {
			less70pesosPromoTotal-=70;		
		}
		
		TextView less70pesosTextView = (TextView)findViewById(R.id.less_70pesos);
		less70pesosTextView.setText("Less ₱70 promo: -₱"+less70pesosPromoTotal);		 				
		
		orderSubtotalCost-=less70pesosPromoTotal;
*/
		
		TextView orderSubtotalCostTextView = (TextView)findViewById(R.id.order_subtotal);
		orderSubtotalCostTextView.setText("Order Total: ₱"+orderSubtotalCost);		 				
    }
    
    public void init()
    {    	    	
    	//added by Mike, 20170310
    	UsbongUtils.deleteRecursive(new File(UsbongUtils.BASE_FILE_PATH_TEMP));
		    //Reference: http://stackoverflow.com/questions/23024831/android-shared-preferences-example
	        //; last accessed: 20150609
	        //answer by Elenasys
	        //added by Mike, 20150207
	        SharedPreferences prefs = getSharedPreferences(UsbongConstants.MY_ACCOUNT_DETAILS, MODE_PRIVATE);
	        if (prefs!=null) {
		      ((TextView)findViewById(R.id.first_name)).setText(prefs.getString("firstName", ""));//"" is the default value.
		      ((TextView)findViewById(R.id.surname)).setText(prefs.getString("surname", "")); //"" is the default value.
		      ((TextView)findViewById(R.id.contact_number)).setText(prefs.getString("contactNumber", "")); //"" is the default value
/*		      
		      //added by Mike, 20170223
		      RadioGroup preferenceRadioButtonGroup = ((RadioGroup)findViewById(R.id.preference_radiogroup));
			  ((RadioButton)preferenceRadioButtonGroup.getChildAt(prefs.getInt("preference", UsbongConstants.defaultPreference))).setChecked(true);
*/		      	
			  ((TextView)findViewById(R.id.address)).setText(prefs.getString("shippingAddress", "")); //"" is the default value

		      //added by Mike, 20170223
			  RadioGroup modeOfPaymentRadioButtonGroup = ((RadioGroup)findViewById(R.id.mode_of_payment_radiogroup));
			  ((RadioButton)modeOfPaymentRadioButtonGroup.getChildAt(prefs.getInt("modeOfPayment", UsbongConstants.defaultModeOfPayment))).setChecked(true);
/*			  
			  //added by Mike, 20180406
			  RadioButton  modeOfPaymentBankDepositRadioButton = ((RadioButton)findViewById(R.id.bank_deposit_radiobutton));
			  modeOfPaymentBankDepositRadioButton.setText( 
								Html.fromHtml("Bank Deposit (<a href='https://www.bdo.com.ph/send-money' target='_blank'> " +
												"BDO</a>" + "/" +
											  "<a href='https://www.bpiexpressonline.com/p/0/6/online-banking' target='_blank'> " +
											  	"BPI</a>)"));
*/											  		
	        }

	    //added by Mike, 20160126
    	confirmButton = (Button)findViewById(R.id.confirm_button);    	
    	confirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {		
				/*if (currScreen==SHOPPING_CART_SCREEN) {
					
					//added by Mike, 20170427
//					UsbongUtils.cartIcon.setIcon(R.drawable.cart_icon_not_empty);
					UsbongUtils.cartIconDrawableResourceId = R.drawable.cart_icon_not_empty;
					myActivityInstance.invalidateOptionsMenu();
					
					currScreen=ACCOUNT_SCREEN;
					setContentView(R.layout.account);	
					init();
				}
				else {*/
					if (verifyFields()) {			
						//save data 
				        //Reference: http://stackoverflow.com/questions/23024831/android-shared-preferences-example
				        //; last accessed: 20150609
				        //answer by Elenasys
				        //added by Mike, 20170207
				        SharedPreferences.Editor editor = getSharedPreferences(UsbongConstants.MY_ACCOUNT_DETAILS, MODE_PRIVATE).edit();
				        editor.putString("firstName", ((TextView)findViewById(R.id.first_name)).getText().toString());
				        editor.putString("surname", ((TextView)findViewById(R.id.surname)).getText().toString());
				        editor.putString("contactNumber", ((TextView)findViewById(R.id.contact_number)).getText().toString());
/*
				        RadioGroup radioButtonGroup = (RadioGroup)findViewById(R.id.preference_radiogroup);				        		
						for (int i=0; i< radioButtonGroup.getChildCount(); i++) {
							if (((RadioButton)radioButtonGroup.getChildAt(i)).isChecked()) {
								currPreference=i;
							}
						}
						editor.putInt("preference", currPreference); //added by Mike, 20170223				        
*/						
				        editor.putString("shippingAddress", ((TextView)findViewById(R.id.address)).getText().toString());
						
						RadioGroup paymentMethodRadioButtonGroup = (RadioGroup)findViewById(R.id.mode_of_payment_radiogroup);
						for (int i=0; i< paymentMethodRadioButtonGroup.getChildCount(); i++) {
							if (((RadioButton)paymentMethodRadioButtonGroup.getChildAt(i)).isChecked()) {
								currModeOfPayment=i;
							}
						}
						editor.putInt("modeOfPayment", currModeOfPayment); //added by Mike, 20170223
				        editor.commit();				    	
										        
						StringBuffer buySummary = new StringBuffer();
						buySummary.append("-Purchase Order Summary-\n");	
						
						int tempListSize = tempList.size();
						for (int i=0; i<tempListSize; i++) {
							//edited by Mike, 20170725
							String o = tempList.get(i).toString();
							buySummary.append(Html.fromHtml(o.substring(0, o.indexOf("ImageFileName: "))
																				.replace("MerchantName: ", "<br>"))
																				+"\n");						
							buySummary.append("Quantity: "+quantityList.get(i)+"\n");							
							
							if (i<tempListSize-1) {
								buySummary.append("-\n");								
							}
						}
						buySummary.append("--\n");
/*						
						buySummary.append("Less ₱70 promo: -₱"+less70pesosPromoTotal+"\n");
*/						
						int paymentMethodRadioButtonID = paymentMethodRadioButtonGroup.getCheckedRadioButtonId();					
						RadioButton paymentMethodRadioButton = (RadioButton) paymentMethodRadioButtonGroup.findViewById(paymentMethodRadioButtonID);
						String paymentMethodSelectedText = paymentMethodRadioButton.getText().toString();	 

						if (paymentMethodSelectedText.contains("Meetup at MOSC (Less ₱70)")) {
							buySummary.append("Meetup at MOSC promo: -₱70\n");							
							orderSubtotalCost-=70;
						}						
						
						buySummary.append("Order Total: ₱"+orderSubtotalCost+"\n");
						buySummary.append("--\n");
						
/*						
						buySummary.append(productDetails+"\n");											
						String quantity = ((TextView)findViewById(R.id.quantity)).getText().toString();
						if (quantity.trim().equals("")) {
							buySummary.append("Quantity: "+ "1"
									+"\n--\n");    	    		
    					}
						else {
							buySummary.append("Quantity: "+ quantity
									+"\n--\n");    	    									
						}
*/						
						buySummary.append("Customer Name: "+
								((TextView)findViewById(R.id.surname)).getText().toString()+", "+
								((TextView)findViewById(R.id.first_name)).getText().toString()+"\n");    	
	
						buySummary.append("Contact Number: "+
								((TextView)findViewById(R.id.contact_number)).getText().toString()+"\n");    	

/*						
//						RadioGroup radioButtonGroup = (RadioGroup)findViewById(R.id.preference_radiogroup);
						int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();				
	//					RadioButton r = (RadioButton) radioButtonGroup.getChildAt(radioButtonID); 
						RadioButton radioButton = (RadioButton) radioButtonGroup.findViewById(radioButtonID);
						String selectedText = radioButton.getText().toString();	 
						buySummary.append("Preference: "+selectedText+"\n");    	
*/	
/*						
						buySummary.append("Address: "+
								((TextView)findViewById(R.id.address)).getText().toString()+"\n");    	
*/						
						if (paymentMethodSelectedText.contains("Meetup at MOSC (Less ₱70)")) {
							buySummary.append("Address: Marikina Orthopedic Specialty Clinic\n");    	
						}						
						else {
							buySummary.append("Address: "+
									((TextView)findViewById(R.id.address)).getText().toString()+"\n");    	
						}
						
/*						
//						RadioGroup paymentMethodRadioButtonGroup = (RadioGroup)findViewById(R.id.mode_of_payment_radiogroup);
						int paymentMethodRadioButtonID = paymentMethodRadioButtonGroup.getCheckedRadioButtonId();					
						RadioButton paymentMethodRadioButton = (RadioButton) paymentMethodRadioButtonGroup.findViewById(paymentMethodRadioButtonID);
						String paymentMethodSelectedText = paymentMethodRadioButton.getText().toString();	 
*/	
						buySummary.append("Payment Method: "+paymentMethodSelectedText+"\n");    	
	
						String additionalInstructionsString = ((TextView)findViewById(R.id.additional_instructions)).getText().toString();					
						if (additionalInstructionsString.trim().equals("")) {
							buySummary.append("Additional Instructions: "+
									"N/A\n");    							
						}
						else {
							buySummary.append("Additional Instructions: "+
									additionalInstructionsString+"\n");    							
						}
						
						String additionalInquiriesString = ((TextView)findViewById(R.id.additional_inquiries)).getText().toString();					
						if (additionalInquiriesString.trim().equals("")) {
							buySummary.append("Additional Inquiries: "+
									"N/A\n");    							
						}
						else {
							buySummary.append("Additional Inquiries: "+
									additionalInquiriesString+"\n");    							
						}
	/*
						//added by Mike, 20170221
						UsbongUtils.generateDateTimeStamp();
						buySummary.append("\nPurchase Order ID:\n"+UsbongUtils.getDateTimeStamp()+"\n");					
	*/					
						buySummary.append("-End of Summary-");    							
											
						
						//added by Mike, 20170511
						String s = "";
						if (tempList.size()>1) {
							s="...";
						}
						
						//http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application;
						//answer by: Jeremy Logan, 20100204
						//added by Mike, 20170220
					    Intent i = new Intent(Intent.ACTION_SEND);
					    i.setType("message/rfc822"); //remove all non-email apps that support send intent from chooser
					    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{UsbongConstants.ORDER_EMAIL_ADDRESS});
//					    i.putExtra(Intent.EXTRA_SUBJECT, "Purchase Order: "+productDetails.substring(0,productDetails.indexOf("\n")).replace("Title: ",""));
					    i.putExtra(Intent.EXTRA_SUBJECT, "Purchase Order: "+tempList.get(0).substring("<b>".length(),tempList.get(0).indexOf("</b>"))+s);
					    i.putExtra(Intent.EXTRA_TEXT   , buySummary.toString());
					    try {
					    	isSendingData=true; //added by Mike, 20170225
					        startActivityForResult(Intent.createChooser(i, "Sending email..."), 1); 
					    } catch (android.content.ActivityNotFoundException ex) {
					        Toast.makeText(CartActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
					    }	

/*						
				    	isSendingData=true; //added by Mike, 20170225

						Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
						intent.setType("message/rfc822");
						intent.putExtra(Intent.EXTRA_SUBJECT, "Purchase Order: "+tempList.get(0).substring("<b>".length(),tempList.get(0).indexOf("</b>"))+s);
						intent.putExtra(Intent.EXTRA_TEXT,  buySummary.toString());
						intent.setData(Uri.parse("mailto:"+UsbongConstants.ORDER_EMAIL_ADDRESS)); // or just "mailto:" for blank
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
						startActivity(intent);
*/						
					}
				}				
/*			}
 */
    	});    	
/*    	
    	//added by Mike, 20160126
    	backButton = (Button)findViewById(R.id.back_button);
    	backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {		
				
				
				//TODO: store product details later
			    setContentView(R.layout.ecommerce_text_image_display_screen);	        			    								
			}
    	});    	
*/    	
    }
    
    public boolean verifyFields() {
    	boolean allFieldsAreFilledUp=true;
    	
    	TextView surnameTextView = ((TextView)findViewById(R.id.surname));
		String surname = surnameTextView.getText().toString();	
		if (surname.trim().equals("")) {
			surnameTextView.setBackgroundColor(Color.parseColor("#fff9b6")); 
			allFieldsAreFilledUp=false;
		}
		else {
			surnameTextView.setBackgroundColor(Color.parseColor("#EEEEEE")); 
		}

    	TextView firstnameTextView = ((TextView)findViewById(R.id.first_name));
		String firstname = firstnameTextView.getText().toString();
		if (firstname.trim().equals("")) {
			firstnameTextView.setBackgroundColor(Color.parseColor("#fff9b6")); 
			allFieldsAreFilledUp=false;
		}
		else {
			firstnameTextView.setBackgroundColor(Color.parseColor("#EEEEEE")); 
		}

    	TextView contactNumberTextView = ((TextView)findViewById(R.id.contact_number));
		String contactNumber = contactNumberTextView.getText().toString();
		if (contactNumber.trim().equals("")) {
			contactNumberTextView.setBackgroundColor(Color.parseColor("#fff9b6")); 
			allFieldsAreFilledUp=false;
		}
		else {
			contactNumberTextView.setBackgroundColor(Color.parseColor("#EEEEEE")); 
		}

    	TextView addressTextView = ((TextView)findViewById(R.id.address));
		String address = addressTextView.getText().toString();
		if (address.trim().equals("")) {
			addressTextView.setBackgroundColor(Color.parseColor("#fff9b6")); 
			allFieldsAreFilledUp=false;
		}
		else {
			addressTextView.setBackgroundColor(Color.parseColor("#EEEEEE")); 
		}
				
		if (!allFieldsAreFilledUp) {
	        Toast.makeText(CartActivity.this, "Please fill up all required fields.", Toast.LENGTH_LONG).show();
	        return false;
		}
		return true;
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

            //added by Mike, 20170225
	    	if (isSendingData) {
	    		isSendingData=false;
	    		
//				returnToProductSelection();
				//added by Mike, 20170730
        		AlertDialog.Builder allowUsToEmptyCartAlertDialog = new AlertDialog.Builder(CartActivity.this).setTitle("Hey!");
				TextView tv = new TextView(getInstance());
				tv.setText("\nAllow us to empty your cart?");
				tv.setGravity(Gravity.CENTER_HORIZONTAL);
				tv.setTextSize(16);
				allowUsToEmptyCartAlertDialog.setView(tv);
				allowUsToEmptyCartAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//added by Mike, 20170730
			    		UsbongUtils.cartIconDrawableResourceId = R.drawable.cart_icon;
			    		UsbongMainActivity.getInstance().invalidateOptionsMenu();
						UsbongUtils.itemsInCart.clear();			            						    	
						
						returnToProductSelection();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
        				}).show();	
/*	    		
	    		//added by Mike, 20170729
	    		UsbongUtils.cartIconDrawableResourceId = R.drawable.cart_icon;
				myActivityInstance.invalidateOptionsMenu();
				UsbongUtils.itemsInCart.clear();
*/
/*				
	    		//edited by Mike, 20170525
	    		finish();
	    		Intent toCallingActivityIntent = new Intent(getInstance(), UsbongMainActivity.class);
	    		toCallingActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	    		startActivity(toCallingActivityIntent);		
*/	    		
/*	    		
		        //added by Mike, 20170225
				finish();    
				Intent toUsbongDecisionTreeEngineActivityIntent = new Intent(CartActivity.this, UsbongDecisionTreeEngineActivity.class);
				toUsbongDecisionTreeEngineActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				startActivity(toUsbongDecisionTreeEngineActivityIntent);
*/				
	    	}
        }
    }//onActivityResult

    //added by Mike, 20170801
    public void returnToProductSelection() {
		final Activity a;
		a = UsbongMainActivity.getInstance(); //edited by Mike, 20180427

/*//commented out by Mike, 20180427
		if ((getIntent().getExtras().getInt("activity caller")==0) 
				|| (getIntent().getExtras().getInt("activity caller")==UsbongConstants.USBONG_MAIN_ACTIVITY)) {
			a = UsbongMainActivity.getInstance();
		}
		else {
			a = UsbongDecisionTreeEngineActivity.getInstance();						
		}
*/

		//edited by Mike, 20170525
		finish();
		Intent toCallingActivityIntent = new Intent(getInstance(), a.getClass());
		toCallingActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		startActivity(toCallingActivityIntent);				
    }
    
    //added by Mike, July 2, 2015
    @Override
	public void onBackPressed() {
/*
    	//edited by Mike, 20160417
		if ((mTts!=null) && (mTts.isSpeaking())) {
			mTts.stop();
		}
		//edited by Mike, 20160417
		if ((myMediaPlayer!=null) && (myMediaPlayer.isPlaying())) {
			myMediaPlayer.stop();
		}
*/
    	//edited by Mike, 20170801
    	returnToProductSelection();
    	
/*
    	//Reference: http://stackoverflow.com/questions/11495188/how-to-put-application-to-background
    	//; last accessed: 14 April 2015, answer by: JavaCoderEx
    	Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);    
*/        
    }
    
    //added by Mike, 25 Sept. 2015
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.standard_menu, menu);

		//added by Mike, 20170427
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
			    	AlertDialog.Builder emptyCartAlertDialog = new AlertDialog.Builder(CartActivity.this).setTitle("Your Shopping Cart");
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
					toCartActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(toCartActivityIntent);
				}
				return true;
			case(R.id.sell): //added by Mike, 20170308
				finish();
				//added by Mike, 20170216
				Intent toSellActivityIntent = new Intent().setClass(getInstance(), SellActivity.class);
				toSellActivityIntent.putExtra("newSellActivity", true); //added by Mike, 20170328
				toSellActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(toSellActivityIntent);
				return true;
			case(R.id.request):
				finish();
				//added by Mike, 20170216
				Intent toRequestActivityIntent = new Intent().setClass(getInstance(), RequestActivity.class);
				toRequestActivityIntent.putExtra("newRequestActivity", true); //added by Mike, 20170330
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
			case(R.id.about):
		    	new AlertDialog.Builder(CartActivity.this).setTitle("About")
				.setMessage(UsbongUtils.readTextFileInAssetsFolder(CartActivity.this,"credits.txt")) //don't add a '/', otherwise the file would not be found
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
				bankDeposit.setText("Bank Deposit" + " (BDO/BPI)");
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
				        
				        if (currScreen!=SHOPPING_CART_SCREEN) {
					        //added by Mike, 20170222
					        setContentView(R.layout.account);	
					        init();				        	
				        }
				    }
				}).show();
				return true;
			case android.R.id.home: //added by Mike, 22 Sept. 2015
/*//commented out by Mike, 201702014; UsbongDecisionTreeEngineActivity is already the main menu				
				processReturnToMainMenuActivity();
*/				    	//added by Mike, 20170216
				//return to UsbongDecisionTreeEngineActivity
				//added by Mike, 20170525
				final Activity a;
				a = UsbongMainActivity.getInstance(); //edited by Mike, 20180427

/*//commented out by Mike, 20180427
				if ((getIntent().getExtras().getInt("activity caller")==0) 
						|| (getIntent().getExtras().getInt("activity caller")==UsbongConstants.USBONG_MAIN_ACTIVITY)) {
					a = UsbongMainActivity.getInstance();
				}
				else {
					a = UsbongDecisionTreeEngineActivity.getInstance();						
				}
*/

				//edited by Mike, 20170525
				finish();
				Intent toCallingActivityIntent = new Intent(getInstance(), a.getClass());
				toCallingActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				startActivity(toCallingActivityIntent);		
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
				//edited by Mike, 20170505
            	final String o = items.get(position);			
				final View v;// = convertView; //edited by Mike, 20170511
//                if (v == null) {
            	//Reference: https://stackoverflow.com/questions/14156996/out-of-memory-on-android-app-on-scrolling-list-with-images;
            	//last accessed: 20170607
            	//answer by: dev_android
            	if (convertView == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
/*                    if (o.contains("PROMO")||o.contains("RetroCC")||o.toLowerCase().contains("manga")) { //TODO: make this more generic 
//                    if (o.toString().substring(0,items.get(position).indexOf("T")).equals(""+UsbongConstants.PRODUCT_TYPE_COMBOS)) { //COMBO
                    		v = vi.inflate(R.layout.tree_loader_alternative_cart, null);
                    }
                    else {
*/                    
	                        v = vi.inflate(R.layout.tree_loader_cart, null);
/*                    }
 */
            	}
            	else {
            		v = convertView;
            	}

                if (o != null) {
                	try {       
                    	TextView dataCurrentTextView = (TextView)v.findViewById(R.id.tree_item);
//                    	final String tempS = o.toString();
//                    	String tempS;
                    	final String s; 
	                    final String imageFileName;
	                    
	            		//Reference: http://www.anddev.org/tinytut_-_get_resources_by_name__getidentifier_-t460.html; last accessed 14 Sept 2011
	                    Resources myRes = instance.getResources();
	    				String tempS = o.toString().replace("\n", "<br>");
	    				s = tempS.subSequence(0, tempS.indexOf("MerchantName: ")).toString();

	    				imageFileName = o.toString().substring(o.indexOf("ImageFileName: ")+"ImageFileName: ".length(), o.indexOf("ProductOverview: "));
/*	    				imageFileName = o.toString().substring(0, o.toString().indexOf("</b>"))
	                    		.replace("<b>","")
	                    		.replace("遯ｶ�ｿｽ","")
	                    		.replace("'","")
	                    		.replace(":","")+".jpg"; //edited by Mike, 20170202
*/
	    				//added by Mike, 20180429
                        int index = o.indexOf("MerchantName: ")+"MerchantName: ".length();
                        int endIndex = o.indexOf("ImageFileName: ");	    				                        
                        
		    			//added by Mike, 20170529
		    			final String merchantName = o.substring(index, endIndex);

		    			//added by Mike, 20180429
	    				String tempS3 = o.toString();
	    				final String currProductOverview = tempS3.substring(tempS3.indexOf("ProductOverview: ")+"ProductOverview: ".length()).toString();

		    			
/*
	                    String imageString = o.toString()
	            				.replace("\n", "<br>")+"<br><br>";	    					    					    					    				
	    				tempS = "<b>"+imageString.substring(0, imageString.indexOf("<br>"))+"</b>" + //name
	    						imageString.substring("<br>", imageString.indexOf("<br>")+	            				
	    						"<font color='#644d17'><b>"+imageString.substring(imageString.indexOf("₱"), );
	    							    				
		                imageFileName = imageString.substring(0, imageString.indexOf("<br>"))
		                    		.replace("<b>","")
		                    		.replace("遯ｶ�ｿｽ","")
		                    		.replace("'","")
		                    		.replace(":","")+".jpg"; //edited by Mike, 20170202
*/
		    			
		            	InputStream myInputStream;
		            	String folderName = imageFileName.substring(0, imageFileName.indexOf("/"));
		            	
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
		    			
		    			
		    			//edited by Mike, 20180429
//			    			InputStream is = myRes.getAssets().open(imageFileName);		    				
		    			if (myInputStream!=null) {
		                    final Drawable myDrawableImage = Drawable.createFromStream(myInputStream, null); //edited by Mike, 20170202		    				
		            		final ImageView image = (ImageView) v.findViewById(R.id.tree_item_image_view);
		                    
		                	if (myDrawableImage!=null) {
		                		image.setImageDrawable(myDrawableImage);                		
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
			            				toBuyActivityIntent.putExtra(UsbongConstants.MERCHANT_NAME, merchantName); //added by Mike, 20170529        				
			            				toBuyActivityIntent.putExtra(UsbongConstants.ITEM_PRODUCT_OVERVIEW, currProductOverview); //added by Mike, 20180429
			            				startActivityForResult(toBuyActivityIntent,1);
		                			}
		                		});	                		
		                	}

		    			}
		            	
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
	            				
/*	            				//commented out by Mike, 20180429
                				//added by Mike, 20170216
	            				Intent toBuyActivityIntent = new Intent().setClass(getInstance(), BuyActivity.class);
	            				toBuyActivityIntent.putExtra(UsbongConstants.ITEM_VARIABLE_NAME, s);
	            				toBuyActivityIntent.putExtra(UsbongConstants.ITEM_IMAGE_NAME, imageFileName);
	            				toBuyActivityIntent.putExtra(UsbongConstants.MERCHANT_NAME, merchantName); //added by Mike, 20170529        				
	            				startActivityForResult(toBuyActivityIntent,1);
*/	            				
	            				
                				//added by Mike, 20170216
	            				Intent toBuyActivityIntent = new Intent().setClass(getInstance(), BuyActivity.class);
	            				toBuyActivityIntent.putExtra(UsbongConstants.ITEM_VARIABLE_NAME, s);
	            				toBuyActivityIntent.putExtra(UsbongConstants.ITEM_IMAGE_NAME, imageFileName);
	            				toBuyActivityIntent.putExtra(UsbongConstants.MERCHANT_NAME, merchantName); //added by Mike, 20170528        					            				
	            				toBuyActivityIntent.putExtra(UsbongConstants.ITEM_PRODUCT_OVERVIEW, currProductOverview); //added by Mike, 20180419
	            				startActivityForResult(toBuyActivityIntent,1);
	            			}
	                	});
	                	
	                	
/*                		
                		//added by Mike, 20170505
	            		TextView quantity = (TextView) v.findViewById(R.id.quantity);
	            		quantity.setText(Html.fromHtml(quantityList.get(position)));
*/
	            		final Spinner quantitySpinner = (Spinner) v.findViewById(R.id.quantity);
	            		
	            		//edited by Mike, 20170508
	            		final int quantity = Integer.parseInt(quantityList.get(position));
	            		ArrayList<String> quantityItems = new ArrayList<String>();
	            		for (int i=quantity; i>0; i--) {
	            			quantityItems.add("  "+i+"  ");
	            		}
	            		quantityItems.add("Remove");

	            		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(instance, android.R.layout.simple_dropdown_item_1line, quantityItems);
	            		quantitySpinner.setAdapter(adapter);	            	
	            			            		
	            		//added by Mike, 20170509	            		
	            		quantitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	            			private int prevQuantityIndex;
							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, final int arg2, long arg3) {
								if (quantitySpinner.getItemAtPosition(arg2).toString().equals("Remove")){
						        	new AlertDialog.Builder(CartActivity.this).setTitle("Hey!")
									.setMessage("Are you sure you want to remove this item from your cart?")
									.setPositiveButton("Yes", new DialogInterface.OnClickListener() {					
										@Override
										public void onClick(DialogInterface dialog, int which) {
											items.remove(position);
											quantityList.remove(position);																								

											updateItemsInCart(items);
											
											//added by Mike, 20170509
											if (UsbongUtils.itemsInCart.isEmpty()) {
												UsbongUtils.cartIconDrawableResourceId = R.drawable.cart_icon;
												myActivityInstance.invalidateOptionsMenu();
											}
											
											prevQuantityIndex=0;
											instance.initCart();
										}
									})
									.setNegativeButton("No", new DialogInterface.OnClickListener() {					
										@Override
										public void onClick(DialogInterface dialog, int which) {
											quantitySpinner.setSelection(prevQuantityIndex);
										}
									}).show();	 
								}								
								else {
//									if (prevQuantityIndex!=arg2) {
										prevQuantityIndex=arg2;
										quantityList.remove(position);
										String q = quantitySpinner.getSelectedItem().toString().trim();
										quantityList.add(position, q);

										//added by Mike, 20170511
										updateItemsInCart(items);
										
					            		processSubtotal(v, Integer.parseInt(q), s);
										//processOrderTotal();
					            		processPromoAndOrderTotal();
//									}
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub
								
							}	            			
	            		});
	            		            		
	            		//added by Mike, 20170511
	            		processSubtotal(v, quantity, s);
	            		
                	}
	            	catch(Exception e) {
	            		e.printStackTrace();
	            	}
                }
                return v;
        }
	}
	
	//added by Mike, 20170511
	public void processSubtotal(View v, int quantity, String s) {
		//added by Mike, 20170508
		TextView price = (TextView) v.findViewById(R.id.price);
		//get item price
		String sPart1 = s.substring(s.indexOf("₱"));	            				
		String item_price = sPart1.substring(0,sPart1.indexOf("<"));//("("));//(used), (new) //500<br>
		price.setText(item_price+"\neach");
		
		//added by Mike, 20170508
		TextView subtotal = (TextView) v.findViewById(R.id.subtotal);
		int subtotalNumber = quantity*Integer.parseInt(item_price.replace("₱", "").trim());
		subtotal.setText("₱"+subtotalNumber+"\n(Subtotal)");		
	}
	
	public void updateItemsInCart(ArrayList<String> items) {
		UsbongUtils.itemsInCart.clear();
		for (int i=0; i<items.size(); i++) {
			int quantitySize = Integer.parseInt(quantityList.get(i));
			for (int k=0; k<quantitySize; k++) {
				UsbongUtils.itemsInCart.add(items.get(i));
			}
		}
	}
}