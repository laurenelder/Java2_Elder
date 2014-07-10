package com.laurenelder.movielookup;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class FileManager {
	
	private static FileManager manager_instance;
	String tag = "FILE MANAGER CLASS";
	
	// Constructor
	private FileManager() {
		
	}
	
	public static FileManager getInstance() {
		if (manager_instance == null) {
			manager_instance = new FileManager();
		}
		return manager_instance;
	}
	
	public Boolean writeToFile(Context context, String fileName, String fileContent) {
		Boolean completion = false;
		FileOutputStream outputStream = null;
		
		try {
			outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			outputStream.write(fileContent.getBytes());
			completion = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(tag, e.getMessage().toString());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(tag, e.getMessage().toString());
			e.printStackTrace();
		}
		return completion;
	}
}
