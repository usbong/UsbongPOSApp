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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import usbong.android.features.node.QRCodeReaderActivity;
import usbong.android.utils.UsbongConstants;
import usbong.android.utils.UsbongUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This is Usbong's Main Menu activity. 
 */
public class ContactActivity extends AppCompatActivity/*Activity*/ 
{	
	private boolean isSendingData;

	//edited by Mike, 20170225
	private static int currPreference=UsbongConstants.defaultPreference; 	
	private static int currModeOfPayment=UsbongConstants.defaultModeOfPayment; 
	
	private String productDetails; //added by Mike, 20170221
		
	private Button sendButton;
	
	//added by Mike, 20170309
	private Button photoCaptureButton;
	private ImageView myImageView;
	public boolean performedCapturePhoto;
	public static Intent photoCaptureIntent;
	private String myPictureName="default"; //change this later in the code
	private List<String> attachmentFilePaths;
				
	public static String timeStamp;
		
	//added by Mike, 20170327
	private Button captureISBN10Button;
	private Button captureISBN13Button;	
	public static Intent captureISBN10Intent;
	public static Intent captureISBN13Intent;
	
	//commented out by Mike, 20180427
//	protected UsbongDecisionTreeEngineActivity myUsbongDecisionTreeEngineActivity;
	protected SettingsActivity mySettingsActivity;
	
	private static Activity myActivityInstance;
	private ProgressDialog myProgressDialog;
	
    private AlertDialog inAppSettingsDialog; //added by Mike, 20160417

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);

        //added by Mike, 27 Sept. 2015
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        myActivityInstance = this;
                
        //added by Mike, 25 Sept. 2015
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.usbong)); //app_name
//        getSupportActionBar().setDisplayUseLogoEnabled(true);        


	    setContentView(R.layout.contact);	        
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
//	        reset();
	        init();
    }
    
    public Activity getInstance() {
//    	return instance;
    	return myActivityInstance;
    }
    
    /*
     * Initialize this activity
     */
    public void init()
    {   
    	//added by Mike, 20170330
    	loadData();

/*    	
    	//added by Mike, 20170309
//	    if (!performedCapturePhoto) {
	    	//Reference: http://stackoverflow.com/questions/2793004/java-lista-addalllistb-fires-nullpointerexception
	    	//Last accessed: 14 March 2012
	    	attachmentFilePaths = new ArrayList<String>();            	

	    	initTakePhotoScreen();
//	    }  	
*/    	        
    	//added by Mike, 20160126
    	sendButton = (Button)findViewById(R.id.send_button);    	
    	sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {		
					if (verifyFields()) {			
						//save data 
				        //Reference: http://stackoverflow.com/questions/23024831/android-shared-preferences-example
				        //; last accessed: 20150609
				        //answer by Elenasys
				        //added by Mike, 20170207
				        SharedPreferences.Editor editor = getSharedPreferences(UsbongConstants.MY_ACCOUNT_DETAILS, MODE_PRIVATE).edit();
				        editor.putString("firstName", ((TextView)findViewById(R.id.first_name)).getText().toString());
				        editor.putString("surname", ((TextView)findViewById(R.id.surname)).getText().toString());
/*				        editor.putString("contactNumber", ((TextView)findViewById(R.id.contact_number)).getText().toString());
				        editor.putString("shippingAddress", ((TextView)findViewById(R.id.address)).getText().toString());
						
						RadioGroup paymentMethodRadioButtonGroup = (RadioGroup)findViewById(R.id.mode_of_payment_radiogroup);
						for (int i=0; i< paymentMethodRadioButtonGroup.getChildCount(); i++) {
							if (((RadioButton)paymentMethodRadioButtonGroup.getChildAt(i)).isChecked()) {
								currModeOfPayment=i;
							}
						}
						editor.putInt("modeOfPayment", currModeOfPayment); //added by Mike, 20170223
				        editor.commit(); //added by Mike, 20170330
*/										        
						StringBuffer contactSummary = new StringBuffer();
						contactSummary.append("-Help & Feedback Summary-\n");					

						RadioGroup itemTypeRadioButtonGroup = (RadioGroup)findViewById(R.id.item_type_radiogroup);
						int itemTypeRadioButtonID = itemTypeRadioButtonGroup.getCheckedRadioButtonId();				
						RadioButton itemTypeRadioButton = (RadioButton) itemTypeRadioButtonGroup.findViewById(itemTypeRadioButtonID);
						String itemTypeSelectedText = itemTypeRadioButton.getText().toString();	 
/*						
						contactSummary.append(""+itemTypeSelectedText+"\n");    	
*/
						contactSummary.append(""+
								((EditText)findViewById(R.id.description)).getText().toString()+"\n");

						contactSummary.append("--\n");    																				
						
						contactSummary.append("Customer Name: "+
								((TextView)findViewById(R.id.surname)).getText().toString()+", "+
								((TextView)findViewById(R.id.first_name)).getText().toString()+"\n");    	
	
						contactSummary.append("-End of Summary-");    							
											
						//http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application;
						//answer by: Jeremy Logan, 20100204
						//added by Mike, 20170220
					    Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE); //changed from ACTION_SEND to ACTION_SEND_MULTIPLE by Mike, 20170313
					    i.setType("message/rfc822"); //remove all non-email apps that support send intent from chooser
					    
					    if (itemTypeSelectedText.contains("help")) {
						    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{UsbongConstants.HELP_EMAIL_ADDRESS});					    	
					    }
					    else {
						    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{UsbongConstants.FEEDBACK_EMAIL_ADDRESS});					    						    	
					    }
					    
					    
					    i.putExtra(Intent.EXTRA_SUBJECT, ""+itemTypeSelectedText+": "+((EditText)findViewById(R.id.subject)).getText().toString());
					    i.putExtra(Intent.EXTRA_TEXT   , contactSummary.toString());
					    
					    try {
					    	isSendingData=true; //added by Mike, 20170225
					        startActivityForResult(Intent.createChooser(i, "Sending email..."), 1); 
					    } catch (android.content.ActivityNotFoundException ex) {
					        Toast.makeText(ContactActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
					    }	
					}
				}					
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
    	
    	EditText firstNameEditText = ((EditText)findViewById(R.id.first_name));
		String firstName = firstNameEditText.getText().toString();	
		if (firstName.trim().equals("")) {
			firstNameEditText.setBackgroundColor(Color.parseColor("#fff9b6")); 
			allFieldsAreFilledUp=false;
		}
		else {
			firstNameEditText.setBackgroundColor(Color.parseColor("#EEEEEE")); 
		}
		
    	EditText surnameEditText = ((EditText)findViewById(R.id.surname));
		String surname = surnameEditText.getText().toString();	
		if (surname.trim().equals("")) {
			surnameEditText.setBackgroundColor(Color.parseColor("#fff9b6")); 
			allFieldsAreFilledUp=false;
		}
		else {
			surnameEditText.setBackgroundColor(Color.parseColor("#EEEEEE")); 
		}

    	EditText subjectEditText = ((EditText)findViewById(R.id.subject));
		String subject = subjectEditText.getText().toString();	
		if (subject.trim().equals("")) {
			subjectEditText.setBackgroundColor(Color.parseColor("#fff9b6")); 
			allFieldsAreFilledUp=false;
		}
		else {
			subjectEditText.setBackgroundColor(Color.parseColor("#EEEEEE")); 
		}

    	TextView descriptionTextView = ((TextView)findViewById(R.id.description));
		String description = descriptionTextView.getText().toString();	
		if (description.trim().equals("")) {
			descriptionTextView.setBackgroundColor(Color.parseColor("#fff9b6")); 
			allFieldsAreFilledUp=false;
		}
		else {
			descriptionTextView.setBackgroundColor(Color.parseColor("#EEEEEE")); 
		}
		
		if (!allFieldsAreFilledUp) {
	        Toast.makeText(ContactActivity.this, "Please fill up all required fields.", Toast.LENGTH_LONG).show();
	        return false;
		}
		return true;
    }
    
    public void saveData() {
		//save data 
        //Reference: http://stackoverflow.com/questions/23024831/android-shared-preferences-example
        //; last accessed: 20150609
        //answer by Elenasys
        //added by Mike, 20170207
        SharedPreferences.Editor editor = getSharedPreferences(UsbongConstants.MY_ACCOUNT_DETAILS, MODE_PRIVATE).edit();
        editor.putString("firstName", ((TextView)findViewById(R.id.first_name)).getText().toString());
        editor.putString("surname", ((TextView)findViewById(R.id.surname)).getText().toString());
        editor.putString("contactNumber", ((TextView)findViewById(R.id.contact_number)).getText().toString());

        editor.putString("bookTitle", ((TextView)findViewById(R.id.book_title)).getText().toString());
        editor.putString("firstNameOfPrincipalAuthor", ((TextView)findViewById(R.id.first_name_of_principal_author)).getText().toString());
        editor.putString("surNameOfPrincipalAuthor", ((TextView)findViewById(R.id.surname_of_principal_author)).getText().toString());
        editor.putString("publisher", ((TextView)findViewById(R.id.publisher)).getText().toString());

		RadioGroup languageRadioButtonGroup = (RadioGroup)findViewById(R.id.language_radiogroup);
		int languageRadioButtonID = languageRadioButtonGroup.getCheckedRadioButtonId();				
		RadioButton languageRadioButton = (RadioButton) languageRadioButtonGroup.findViewById(languageRadioButtonID);
		String languageSelectedText;
		if (languageRadioButton!=null) {
			languageSelectedText = languageRadioButton.getText().toString();	 			
		}
		else {
			languageSelectedText = ((RadioButton) languageRadioButtonGroup.getChildAt(0)).getText().toString();
		}
        editor.putString("language", languageSelectedText);

		if (languageSelectedText.equals("Other")) {
	        editor.putString("otherLanguage", ((TextView)findViewById(R.id.other_language)).getText().toString());
		}
		
		RadioGroup formatRadioButtonGroup = (RadioGroup)findViewById(R.id.format_radiogroup);
		int formatRadioButtonID = formatRadioButtonGroup.getCheckedRadioButtonId();				
		RadioButton formatRadioButton = (RadioButton) formatRadioButtonGroup.findViewById(formatRadioButtonID);
		String formatSelectedText;
		if (formatRadioButton!=null) {
			formatSelectedText = formatRadioButton.getText().toString();	 			
		}
		else {
			formatSelectedText = ((RadioButton) formatRadioButtonGroup.getChildAt(0)).getText().toString();
		}
		editor.putString("format", formatSelectedText);

		RadioGroup itemTypeRadioButtonGroup = (RadioGroup)findViewById(R.id.item_type_radiogroup);
		int itemTypeRadioButtonID = itemTypeRadioButtonGroup.getCheckedRadioButtonId();				
		RadioButton itemTypeRadioButton = (RadioButton) itemTypeRadioButtonGroup.findViewById(itemTypeRadioButtonID);
		String itemTypeSelectedText;
		if (itemTypeRadioButton!=null) {
			itemTypeSelectedText = itemTypeRadioButton.getText().toString();	 			
		}
		else {
			itemTypeSelectedText = ((RadioButton) itemTypeRadioButtonGroup.getChildAt(0)).getText().toString();
		}
		editor.putString("productType", itemTypeSelectedText);

		EditText totalBudgetSelectedText = (EditText)findViewById(R.id.total_budget);
/*		int totalBudgetRadioButtonID = totalBudgetRadioButtonGroup.getCheckedRadioButtonId();				
		RadioButton totalBudgetRadioButton = (RadioButton) totalBudgetRadioButtonGroup.findViewById(totalBudgetRadioButtonID);
		String totalBudgetSelectedText;
		if (totalBudgetRadioButton!=null) {
			totalBudgetSelectedText = totalBudgetRadioButton.getText().toString();	 			
		}
		else {
			totalBudgetSelectedText = ((RadioButton) totalBudgetRadioButtonGroup.getChildAt(0)).getText().toString();
		}
*/		
		editor.putString("totalBudget", totalBudgetSelectedText.getText().toString());
/*		
		String isbn_10String = ((TextView)findViewById(R.id.isbn_10)).getText().toString();
        editor.putString("isbn_10", isbn_10String);
*/
		
/*		if (!isbn_10String.equals("")) {
	        editor.putString("isbn_10", isbn_10String);
		}
		else {
	        editor.putString("isbn_10", "N/A");
		}
*/
/*		
		String isbn_13String = ((TextView)findViewById(R.id.isbn_13)).getText().toString();
        editor.putString("isbn_13", isbn_13String);
*/        
/*
		if (!isbn_13String.equals("")) {
	        editor.putString("isbn_13", isbn_13String);
		}
		else {
	        editor.putString("isbn_13", "N/A");
		}
*/
/*        
        editor.putString("numberOfCopies", ((TextView)findViewById(R.id.number_of_copies)).getText().toString());
*/
        editor.putString("quantity", ((TextView)findViewById(R.id.quantity)).getText().toString());
        
/*        editor.putString("totalPriceForAllCopies", ((TextView)findViewById(R.id.total_price_for_all_copies)).getText().toString());
 */
		String commentsString = ((TextView)findViewById(R.id.comments)).getText().toString();					
        editor.putString("comments", commentsString);
/*
		if (commentsString.trim().equals("")) {
	        editor.putString("comments", "N/A");
		}
		else {
	        editor.putString("comments", commentsString);
		}
*/		
        editor.commit();		
    }

    public void loadData() {
	    //Reference: http://stackoverflow.com/questions/23024831/android-shared-preferences-example
        //; last accessed: 20150609
        //answer by Elenasys
        //added by Mike, 20150207
        SharedPreferences prefs = getSharedPreferences(UsbongConstants.MY_ACCOUNT_DETAILS, MODE_PRIVATE);
		
        ((EditText)findViewById(R.id.first_name)).setText(prefs.getString("firstName", ""));//"" is the default value.
        ((EditText)findViewById(R.id.surname)).setText(prefs.getString("surname", ""));//"" is the default value.
/*
        ((EditText)findViewById(R.id.contact_number)).setText(prefs.getString("contactNumber", ""));//"" is the default value.    	            	      

        ((TextView)findViewById(R.id.address)).setText(prefs.getString("shippingAddress", "")); //"" is the default value

	    //added by Mike, 20170223
		RadioGroup modeOfPaymentRadioButtonGroup = ((RadioGroup)findViewById(R.id.mode_of_payment_radiogroup));
		((RadioButton)modeOfPaymentRadioButtonGroup.getChildAt(prefs.getInt("modeOfPayment", UsbongConstants.defaultModeOfPayment))).setChecked(true);
*/
/*        
        if (prefs!=null) {        	
	    	//added by Mike, 20170328
	    	if (getIntent().getBooleanExtra("newContactActivity", false)) {
	          //added by Mike, 20170310
	          UsbongUtils.deleteRecursive(new File(UsbongUtils.BASE_FILE_PATH_TEMP));
    
    		  reset();
	    	}	        
    	}
*/        
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

/*	    		
		        //added by Mike, 20170225
				finish();    
				Intent toUsbongDecisionTreeEngineActivityIntent = new Intent(RequestActivity.this, UsbongDecisionTreeEngineActivity.class);
				toUsbongDecisionTreeEngineActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				startActivity(toUsbongDecisionTreeEngineActivityIntent);
*/				
		        //added by Mike, 20170225
				finish();    
				Intent toUsbongMainActivityIntent = new Intent(ContactActivity.this, UsbongMainActivity.class);
				toUsbongMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				startActivity(toUsbongMainActivityIntent);
	    	}
        }
    }//onActivityResult

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
			    	AlertDialog.Builder emptyCartAlertDialog = new AlertDialog.Builder(ContactActivity.this).setTitle("Your Shopping Cart");
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
			case(R.id.about):
		    	new AlertDialog.Builder(ContactActivity.this).setTitle("About")
				.setMessage(UsbongUtils.readTextFileInAssetsFolder(ContactActivity.this,"credits.txt")) //don't add a '/', otherwise the file would not be found
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
				bankDeposit.setText("Bank Deposit (BDO/BPI)"); //edited by Mike, 20180406
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
				        
					    init();
				    }
				}).show();
				return true;
			case android.R.id.home: //added by Mike, 22 Sept. 2015
/*//commented out by Mike, 201702014; UsbongDecisionTreeEngineActivity is already the main menu				
				processReturnToMainMenuActivity();
*/				    	//added by Mike, 20170216
				//return to UsbongDecisionTreeEngineActivity
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

	//added by Mike, 20170309
    @Override
    public void onRestart() 
    {
        super.onRestart();
        
    	initTakePhotoScreen();
    }
    
	//added by Mike, 20170309
    public void initTakePhotoScreen()
    {
//    	myPictureName=currUsbongNode; //make the name of the picture the name of the currUsbongNode
    	myPictureName=UsbongUtils.processStringToBeFilenameReady(/*((TextView)findViewById(R.id.book_title)).getText().toString()+*/UsbongUtils.getDateTimeStamp()); 
    	
//		String path = "/sdcard/usbong/"+ UsbongUtils.getTimeStamp() +"/"+ myPictureName +".jpg";
		String path = UsbongUtils.BASE_FILE_PATH_TEMP + myPictureName +".jpg";		
		//only add path if it's not already in attachmentFilePaths

		if (!attachmentFilePaths.contains(path)) {
			attachmentFilePaths.add(path);
		}
		
    	myImageView = (ImageView) findViewById(R.id.CameraImage);

    	File imageFile = new File(path);
        
        if(imageFile.exists())
        {
        
        	Bitmap myBitmap = BitmapFactory.decodeFile(path);
        	if(myBitmap != null)
        	{
        		myImageView.setImageBitmap(myBitmap);
/*        		myImageView.setRotation(90);//added by Mike, rotate counter-clockwise once        	
*/        		
 			}
 
        	//Read more: http://www.brighthub.com/mobile/google-android/articles/64048.aspx#ixzz0yXLCazcU                	  
    	}

        photoCaptureButton = (Button)findViewById(R.id.photo_capture_button);
		photoCaptureIntent = new Intent().setClass(this, CameraActivity.class);
		photoCaptureIntent.putExtra("myPictureName",myPictureName);
		photoCaptureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(photoCaptureIntent);
			}
    	});

    }
}