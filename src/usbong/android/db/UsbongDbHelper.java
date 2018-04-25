/*
 * Copyright 2017 Usbong Social Systems, Inc.
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
//Reference: 
//1) https://developer.android.com/training/basics/data-storage/databases.html#WriteDbRow
//last accessed: 20170518
//2) http://stackoverflow.com/questions/513084/ship-an-application-with-a-database
//last accessed: 20170518
//answer by: Danny Remington - OMS; edited by: Austyn Mahoney

package usbong.android.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import usbong.android.pos_app.UsbongMainActivity;
import usbong.android.utils.UsbongDownloadImageTask;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class UsbongDbHelper extends SQLiteOpenHelper {
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 28;
//    public static final String DB_NAME = "usbong_store.db";
    
//    private static String DB_DIR = "/data/data/usbong.android.store_app/databases/";
    private static String DB_NAME = "usbong_store.sql";//"database.sqlite";
    private static String DB_PATH;// = DB_DIR + DB_NAME;
//    private static String OLD_DB_PATH = DB_DIR + "old_" + DB_NAME;
    
    private final Context myContext;

    private boolean createDatabase = false;
    private boolean upgradeDatabase = false;
    
    /* Inner class that defines the table contents */
    public static class UsbongStoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
/*    
    private static final String SQL_CREATE_ENTRIES =
    	    "CREATE TABLE " + UsbongStoreEntry.TABLE_NAME + " (" +
    	    UsbongStoreEntry._ID + " INTEGER PRIMARY KEY," +
    	    UsbongStoreEntry.COLUMN_NAME_TITLE + " TEXT," +
    	    UsbongStoreEntry.COLUMN_NAME_SUBTITLE + " TEXT)";

    	private static final String SQL_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + UsbongStoreEntry.TABLE_NAME;
*/
    public UsbongDbHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        
        myContext = context;
        // Get the path of the database that is based on the context.
        DB_PATH = myContext.getDatabasePath(DB_NAME).getAbsolutePath();
        
//		 getWritableDatabase(); // In the constructor
        
        try {
        	SQLiteDatabase.deleteDatabase(new File(DB_PATH));              		         	
        }
        catch (NullPointerException e) {
        	//no DB to delete
        }
    }
    
    /**
     * Upgrade the database in internal storage if it exists but is not current. 
     * Create a new empty database in internal storage if it does not exist.
     */
    public void initializeDataBase() {
        /*
         * Creates or updates the database in internal storage if it is needed
         * before opening the database. In all cases opening the database copies
         * the database in internal storage to the cache.
         */
        getWritableDatabase();

        if (createDatabase) {
            /*
             * If the database is created by the copy method, then the creation
             * code needs to go here. This method consists of copying the new
             * database from assets into internal storage and then caching it.
             */
            try {
                /*
                 * Write over the empty data that was created in internal
                 * storage with the one in assets and then cache it.
                 */
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        } else if (upgradeDatabase) {
            /*
             * If the database is upgraded by the copy and reload method, then
             * the upgrade code needs to go here. This method consists of
             * renaming the old database in internal storage, create an empty
             * new database in internal storage, copying the database from
             * assets to the new database in internal storage, caching the new
             * database from internal storage, loading the data from the old
             * database into the new database in the cache and then deleting the
             * old database from internal storage.
             */
            try {
/*                FileHelper.copyFile(DB_PATH, OLD_DB_PATH); 
 */
                copyDataBase();
/*                
                SQLiteDatabase old_db = SQLiteDatabase.openDatabase(OLD_DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
                SQLiteDatabase new_db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
*/                
                /*
                 * Add code to load data into the new database from the old
                 * database and then delete the old database from internal
                 * storage after all data has been transferred.
                 */
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {
        /*
         * Close SQLiteOpenHelper so it will commit the created empty database
         * to internal storage.
         */
        close();

        /*
         * Open the database in the assets folder as the input stream.
         */
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        /*
         * Open the empty db in interal storage as the output stream.
         */
        OutputStream myOutput = new FileOutputStream(DB_PATH);

        /*
         * Copy over the empty db in internal storage with the database in the
         * assets folder.
         */
        FileHelper.copyFile(myInput, myOutput);

        /*
         * Access the copied database so SQLiteHelper will cache it and mark it
         * as created.
         */
        getWritableDatabase().close();
    }

    /*
     * This is where the creation of tables and the initial population of the
     * tables should happen, if a database is being created from scratch instead
     * of being copied from the application package assets. Copying a database
     * from the application package assets to internal storage inside this
     * method will result in a corrupted database.
     * <P>
     * NOTE: This method is normally only called when a database has not already
     * been created. When the database has been copied, then this method is
     * called the first time a reference to the database is retrieved after the
     * database is copied since the database last cached by SQLiteOpenHelper is
     * different than the database in internal storage.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    	if (!upgradeDatabase) {   	
	        /*
	         * Signal that a new database needs to be copied. The copy process must
	         * be performed after the database in the cache has been closed causing
	         * it to be committed to internal storage. Otherwise the database in
	         * internal storage will not have the same creation timestamp as the one
	         * in the cache causing the database in internal storage to be marked as
	         * corrupted.
	         */
	        createDatabase = true;
	        
	        /*
	         * This will create by reading a sql file and executing the commands in
	         * it.
	         */
		        try {
		        	InputStream is = myContext.getResources().getAssets().open(
		        			"usbong_store.sql");
		        
		        	String[] statements = FileHelper.parseSqlFile(is);
		        
		        	for (String statement : statements) {
		        		db.execSQL(statement);
		        	}		        			        			        	
		        }catch (Exception ex) {
		        		ex.printStackTrace();
		        	}
	
	            // try {
	            // InputStream is = myContext.getResources().getAssets().open(
	            // "create_database.sql");
	            //
	            // String[] statements = FileHelper.parseSqlFile(is);
	            //
	            // for (String statement : statements) {
	            // db.execSQL(statement);
	            // }
	            // } catch (Exception ex) {
	            // ex.printStackTrace();
	            // }
    	}
    }

    /**
     * Called only if version number was changed and the database has already
     * been created. Copying a database from the application package assets to
     * the internal data system inside this method will result in a corrupted
     * database in the internal data system.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
         * Signal that the database needs to be upgraded for the copy method of
         * creation. The copy process must be performed after the database has
         * been opened or the database will be corrupted.
         */
        upgradeDatabase = true;

        /*
         * Code to update the database via execution of sql statements goes
         * here.
         */

        /*
         * This will upgrade by reading a sql file and executing the commands in
         * it.
         */        
         try {
//        	 myContext.deleteDatabase(DB_PATH);        	 
//        	 String p = myContext.getDatabasePath(DB_NAME).getAbsolutePath();
//        	 myContext.deleteDatabase(p);
        	 
//        	 if (SQLiteDatabase.deleteDatabase(new File(p))) {              		 
	        	 InputStream is = myContext.getResources().getAssets().open(
		         "usbong_store.sql");
	        
	         	 String[] statements = FileHelper.parseSqlFile(is);
	        
		         for (String statement : statements) {
		        	 db.execSQL(statement);
		         }
//        	 }
         } catch (Exception ex) {
        	 ex.printStackTrace();
         }
    }

    /**
     * Called everytime the database is opened by getReadableDatabase or
     * getWritableDatabase. This is called after onCreate or onUpgrade is
     * called.
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    /*
     * Add your public helper methods to access and get content from the
     * database. You could return cursors by doing
     * "return myDataBase.query(....)" so it'd be easy to you to create adapters
     * for your views.
     */

/*    
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
*/
    
    //added by Mike, 20180213
    public void syncInternalDBwithServerDB(SQLiteDatabase db, JSONArray serverProductsTable) {
        try {		        	        	
        		//check first if the internal DB is already synced
            	int serverProductsTableLength = serverProductsTable.length();        	
            	int internalDBProductsTableLength=0;
            	
            	Cursor c = db.rawQuery("SELECT MAX(`product_id`) FROM `product`", null);
            	
            	if (c.moveToFirst()){
            	        internalDBProductsTableLength = c.getInt(0);
            	}
            	c.close();
   		     
            	if (internalDBProductsTableLength==serverProductsTableLength) {            	        	        		
            		return;
            	}

            	//get all the product types from SQLite DB
            	Cursor cProductType = db.rawQuery("SELECT `product_type_name` FROM `product_type`", null);
            	ArrayList<String> listOfProductTypes = new ArrayList<String>();
            	
            	if (cProductType != null) {
 			        if (cProductType.moveToFirst()) { // if Cursor is not empty
 			        	while (!cProductType.isAfterLast()) {			        		
 			        		listOfProductTypes.add(cProductType.getString(cProductType.getColumnIndex("product_type_name")));
 			        		cProductType.moveToNext();
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
            	cProductType.close();
            	
            	Resources myRes = ((UsbongMainActivity)myContext).getResources();
//            	String[][] listOfProductsPerProductType = new String[listOfProductTypes.size()][];
            	
            	HashMap<String, String[]> listOfProductsPerProductTypeHashMap = new HashMap<String, String[]>();
            	
            	//TODO: add: also the products in the sd card if available
            	for(int i=0; i<listOfProductTypes.size(); i++) {
            		String productType = listOfProductTypes.get(i).toLowerCase().replace("'","").replace(" & ", "_and_");
            		listOfProductsPerProductTypeHashMap.put(productType,
            												myRes.getAssets().list(productType));
            	}
             	
            	//delete the entire DB to make sure that the primary keys are the same with the online DB
//        		db.execSQL("delete from " + "product");
        	        	
        		ContentValues insertValues = new ContentValues();        		  
//        		JSONArray json = new JSONArray(serverProductsTable);
/*        			
        		for(int i=0;i<serverProductsTable.length();i++) {
*/        		
        			   JSONArray nestedJsonArray = serverProductsTable; //.optJSONArray(i);        	
/*        			   
        			   JSONArray innerArray = serverProductsTable.getJSONArray(i);
        			   double[] innerResult = new double[innerArray.length()];
*/        			           			          			   
        			   
        			   if (nestedJsonArray != null) {
        				   for(int j=0;j<nestedJsonArray.length();j++) {
        	      	            JSONObject jo_inside = nestedJsonArray.getJSONObject(j);

        	      	            insertValues.put("product_id", jo_inside.getString("product_id"));
        	                	insertValues.put("merchant_id", jo_inside.getString("merchant_id"));
        	                	insertValues.put("product_type_id", jo_inside.getString("product_type_id"));
//        	                	insertValues.put("product_type_name", jo_inside.getString("product_type_name"));
        	                	insertValues.put("name", jo_inside.getString("name"));
        	                	insertValues.put("price", jo_inside.getString("price"));
        	                	insertValues.put("previous_price", jo_inside.getString("previous_price"));
        	                	insertValues.put("language", jo_inside.getString("language"));
        	                	insertValues.put("author", jo_inside.getString("author"));
        	                	insertValues.put("supplier", jo_inside.getString("supplier"));
        	                	insertValues.put("description", jo_inside.getString("description"));
        	                	insertValues.put("image_location", jo_inside.getString("image_location"));
        	                	insertValues.put("format", jo_inside.getString("format"));
        	                	insertValues.put("quantity_in_stock", jo_inside.getString("quantity_in_stock"));
        	                	insertValues.put("translator", jo_inside.getString("translator"));
        	                	insertValues.put("pages", jo_inside.getString("pages"));
        	                	
//        	                	db.insert("product", null, insertValues);	        			
        	                	
        	                	//added by Mike, 20180222
        	                	//verify whether the product item image is already stored in the assets' folder of the .apk
        	                	String product_item_image_name = jo_inside.getString("name").replace("'", "").replace(":", "") + ".jpg";
        	                	//added by Mike, 20180308
        	                	product_item_image_name = Normalizer.normalize(product_item_image_name, Normalizer.Form.NFD);
        	                	product_item_image_name = product_item_image_name.replaceAll("[^\\p{ASCII}]", "");
        	                	
        	                	String url_friendly_product_type_name = jo_inside.getString("product_type_name").toLowerCase().replace("'", "").replace(" & ", "_and_");
        	                	        	                            	                	
        	                    try {
        	                         /*Drawable myDrawableImage = */
        	                    	//this will throw a FileNotFoundException if the file does not exist
//        	                    	Drawable.createFromStream(myRes.getAssets().open(jo_inside.getString("product_type_name").toLowerCase() + "/" + product_item_image_name), null);

//        	                    	String[] myList = myRes.getAssets().list(url_friendly_product_type_name);// + "/");

//    	                    		String path = url_friendly_product_type_name + "/" + product_item_image_name;

/*        	                    	boolean isInList=false;
*/    	               
    	                    		if (listOfProductsPerProductTypeHashMap.get(url_friendly_product_type_name)!=null) {    	                    			
    	                    		    List<String> myList = Arrays.asList(listOfProductsPerProductTypeHashMap.get(url_friendly_product_type_name));
    	                    		    if (!myList.contains(product_item_image_name)) {
    	                    		    	//added by Mike, 20180310
        	        	                	db.insert("product", null, insertValues);	        			
    	                    		    	    	                    		    	
    	                    		    	new UsbongDownloadImageTask().execute("https://store.usbong.ph/assets/images/" + url_friendly_product_type_name + "/" + product_item_image_name.replace(" ", "%20"));  	                    			
    	                    		    }
    	                    		}
    	                    		

/*        	                    	
        	                    	for(int i=0; i<myList.length; i++) {
//            	                    	Log.d(">>>>", myList[i]);
        	                    		if (myList[i].equals(product_item_image_name)) {
            	                    		isInList=true;
        	        	                	break;
            	                    	}        	                    		
        	                    	}
        	                    	
        	                    	if (!isInList) {
            	                    	//Drawable.createFromStream(myRes.getAssets().open(path), null);            	                    		
    	        	                	new UsbongDownloadImageTask().execute("https://store.usbong.ph/assets/images/" + url_friendly_product_type_name + "/" + product_item_image_name.replace(" ", "%20"));
        	                    	}
*/
        	                    	
/*
         	                         if (myDrawableImage == null) {
        	        	                	new UsbongDownloadImageTask().execute("https://store.usbong.ph/assets/images/" + jo_inside.getString("product_type_name").toLowerCase() + "/" + product_item_image_name.replace(" ", "%20"));
        	                         }
*/        	                         
        	                    }
/*        	                    catch (FileNotFoundException e) {
	        	                	new UsbongDownloadImageTask().execute("https://store.usbong.ph/assets/images/" + jo_inside.getString("product_type_name").toLowerCase() + "/" + product_item_image_name.replace(" ", "%20"));
        	                    }
*/        	                    catch (Exception e) {
        	                      e.printStackTrace();
        	                    }
        	                	
        	                	//add the image in the sd card
//        	                	new UsbongDownloadImageTask().execute("https://store.usbong.ph/assets/images/childrens/Harry%20Potter%20and%20the%20Sorcerers%20Stone.jpg");
//        	                	new UsbongDownloadImageTask().execute("https://store.usbong.ph/assets/images/" + jo_inside.getString("product_type_name").toLowerCase() + "/" + product_item_image_name);
        				   }
        			   }
/*        			   
        		}        	
*/        		
        }catch (Exception ex) {
        	ex.printStackTrace();
    	}	
    }
}