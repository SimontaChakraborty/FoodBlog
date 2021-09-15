package edu.ewubd.foodblog;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("ALL")
public class Util {
	private static Util instance = new Util();

	private Util() {}
	public static Util getInstance() {
		return instance;
	}
	//


	@SuppressWarnings("deprecation")
	public String readFromfile(String fileName, Context context) {
		StringBuilder returnString = new StringBuilder();
		InputStream fIn = null;
		InputStreamReader isr = null;
		BufferedReader input = null;
		try {
			fIn = context.getResources().getAssets()
					.open(fileName, Context.MODE_WORLD_READABLE);
			isr = new InputStreamReader(fIn);
			input = new BufferedReader(isr);
			String line = "";
			while ((line = input.readLine()) != null) {
				returnString.append(line + "\n");
			}
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				if (isr != null)
					isr.close();
				if (fIn != null)
					fIn.close();
				if (input != null)
					input.close();
			} catch (Exception e2) {
				//e2.printStackTrace();
			}
		}
		return returnString.toString().trim();
	}


	public void setKeyValue(Context context, String key, String value) {
		try{
			KeyValueDB db = new KeyValueDB(context);
			db.updateValueByKey(key, value);
			db.close();
		} catch (Exception e) {}
	}

	public void deleteByKey(Context context, String key){
		try{
			KeyValueDB db = new KeyValueDB(context);
			db.deleteDataByKey(key);
			db.close();
		} catch (Exception e) {}
	}

	public String getValueByKey(Context context, String key) {
		KeyValueDB db = new KeyValueDB(context);
		String value = db.getValueByKey(key);
		db.close();
		return value;
	}
}