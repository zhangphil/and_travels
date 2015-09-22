package chinamobile.iot.andtravels;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{

	private final String LOG_TAG = "DBHelper";
   
    private SQLiteDatabase database;
    private Context mContext;
    public static final String DB_NAME = "city.db"; 
    public static final String PACKAGE_NAME = "chinamobile.iot.andtravels";
    public static final String DB_PATH = "/data"+ Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME; 
 
    
    public DBHelper(Context context, String databaseName,
            CursorFactory factory, int version) {
        super(context, databaseName, factory, version);
        mContext = context;
    }
    
    public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }
 
    private SQLiteDatabase openDatabase(String dbfile) {
        	
        	try{
        		Log.e(LOG_TAG, "新建数据库了！！！");
        		SQLiteDatabase database = mContext.openOrCreateDatabase(DB_PATH + "/cityData.db", Context.MODE_PRIVATE, null);  
        		database.execSQL("DROP TABLE IF EXISTS city");  
                executeAssetsSQL(database, "city.sql");
                return database;
        	}catch(Exception e){
        		Log.e(LOG_TAG, e.toString());
        		return null;
        	} 
       
    }

    public void closeDatabase() {
    	if(database!=null){
    		 database.close();
    	}
      
    }
    
    private void executeAssetsSQL(SQLiteDatabase db, String schemaName) {  
        BufferedReader in = null;  
        try {  
        	Log.e(LOG_TAG, "把SQL文件添加到数据库中去！！！");
            in = new BufferedReader(new InputStreamReader(mContext.getAssets().open(schemaName)));     
            String line;  
            String buffer = "";  
            db.execSQL("CREATE TABLE 'region' ('region_id' int(11) NOT NULL AUTO_INCREMENT,  'parent_region_id' int(11) DEFAULT NULL,  'region_code' varchar(10) NOT NULL,  'region_name' varchar(40) NOT NULL,  'original_code' varchar(10) DEFAULT NULL,  'leaf' tinyint(1) NOT NULL,  'region_order' smallint(6) NOT NULL,  PRIMARY KEY ('region_id')) ENGINE=InnoDB AUTO_INCREMENT=3445 DEFAULT CHARSET=utf8"); 
            
            /*while ((line = in.readLine()) != null) {  
                buffer += line;  
                if (line.trim().endsWith(";")) { 
                	Log.e(LOG_TAG, "执行SQL语句:" + buffer.replace(";", ""));
                    db.execSQL("CREATE TABLE `region` (  `region_id` int(11) NOT NULL AUTO_INCREMENT,  `parent_region_id` int(11) DEFAULT NULL,  `region_code` varchar(10) NOT NULL,  `region_name` varchar(40) NOT NULL,  `original_code` varchar(10) DEFAULT NULL,  `leaf` tinyint(1) NOT NULL,  `region_order` smallint(6) NOT NULL,  PRIMARY KEY (`region_id`)) ENGINE=InnoDB AUTO_INCREMENT=3445 DEFAULT CHARSET=utf8"); 
                   
                    buffer = "";  
                }  
            }  */
        } catch (IOException e) {  
            Log.e("执行sql文件失败", e.toString());  
        } finally {  
            try {  
                if (in != null)  
                    in.close();  
            } catch (IOException e) {  
                Log.e("db-error", e.toString());  
            }  
        }  
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		executeAssetsSQL(db, "city.sql");
           
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//数据库不升级  
        if (newVersion <= oldVersion) {  
            return;  
        }  
        DBConfig.oldVersion = oldVersion;  
  
        int changeCnt = newVersion - oldVersion;  
        for (int i = 0; i < changeCnt; i++) {  
            // 依次执行updatei_i+1文件      由1更新到2 [1-2]，2更新到3 [2-3]  
            String schemaName = "update" + (oldVersion + i) + "_"  
                    + (oldVersion + i + 1) + ".sql";  
            executeAssetsSQL(db, schemaName);  
        } 
	}
  
	
}
