/*
 * PSU SWENG 500 - Software Engineering Studio
 * Summer 2014
 * TEAM 5:	Ryan Donovan
 * 			Daniel Montanez
 * 			Tricia Murray
 * 			Jimmy Zhang
 */

/**
 * ExportActivity.java
 * 
 *Activity for exporting current transactions to a .csv file.
 * 
 **/

package com.example.budgetnotebook;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ExportActivity extends Activity {
	// String constants
	public static final String TAG = "ExportActivity";
	public static final String FILE_DIR_NAME = "export";
	public static final String FILE_NAME = "BudgeNotes";
	public static final String[] CSV_HEADER = { "CSV_HEADER", "CSV_HEADER" };
	public static final String FLAG_EXPORT_S = "Export Sucess";
	public static final String FLAG_MEMORY_ERR = "Memory Error";
	public static final String FLAG_EXPORT_F = "Export Failure";
	
	// UI Components
	Button button;
	TextView filePath;
	DBHelper db;
	String currentDateString;
	File exportDir;
	//private static int progress = 0x1;
	private ProgressBar mProgress;
	//private Handler mHandler= new Handler();

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.export_transaction);
		mProgress = (ProgressBar) findViewById(R.id.progressBarExport);
		currentDateString = new SimpleDateFormat("ddMMyyyyhhmmss")
				.format(new java.util.Date());
		exportDir = new File(Environment.getExternalStorageDirectory()
				+ File.separator + FILE_DIR_NAME, "");
		String exportFileStr = exportDir.toString() + File.separator
				+ FILE_NAME + currentDateString + ".csv";// to show in UI
		// Create Database instance
		db = new DBHelper(getBaseContext());
		filePath = (TextView) findViewById(R.id.exportPath);
		filePath.setText(exportFileStr);
		button = (Button) findViewById(R.id.executeExport);
		// because we implement OnClickListener we only have to pass "this"
		button.setOnClickListener(onExport);
	}

	private View.OnClickListener onExport = new View.OnClickListener() {
		public void onClick(View view) {
			// detect the view that was "clicked"
			switch (view.getId()) {
			case R.id.executeExport:
				new exportOperation().execute("");
				mProgress.setVisibility(View.VISIBLE);
			//	mProgress.setProgress(progress);
				break;
			}
		}
	};

	private class exportOperation extends AsyncTask<String, Integer, String> {
		@SuppressWarnings("unused")
		private final ProgressDialog dialog = new ProgressDialog(
				ExportActivity.this);
		boolean memoryErr = false;

		@Override
		protected String doInBackground(String... params) {
			String success = "false";
			
			// Open the database file
			File dbFile = getDatabasePath(DBHelper.DATABASE_NAME);
			Log.v(TAG, "Db path is: " + dbFile); // get the path of db

			// calculate free space
			long freeBytesInternal = new File(getApplicationContext()
					.getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
			long megAvailable = freeBytesInternal / 1048576;

			if (megAvailable < 0.1) {
				System.out.println("Please check" + megAvailable);
				memoryErr = true;
			} else {
				Log.v(TAG, "exportDir path::" + exportDir);
				if (!exportDir.exists()) {
					exportDir.mkdirs();
				}
				try {
					// We have enough free space so write the file.
					List<Transaction> listdata = db.getAllListTransactions(0);

					File file;

					file = new File(exportDir, FILE_NAME + currentDateString
							+ ".csv");
					file.createNewFile();
					CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

					// this is the Column of the table and same for Header of
					// CSV
					// file

					csvWrite.writeNext(CSV_HEADER);

					String arrStr1[] = { "ID", "A_ID", "NAME", "DATE",
							"AMOUNT", "CATEGORY", "TYPE", "INTERVAL",
							"DESCRIPTION" };
					csvWrite.writeNext(arrStr1);

					Transaction t;
					if (listdata.size() > 0) {
						for (int index = 0; index < listdata.size(); index++) {
							t = listdata.get(index);

							String arrStr[] = { Integer.toString(t.getId()),
									Integer.toString(t.getAID()), t.getName(),
									t.getDate(), t.getAmount(),
									t.getCategory(), t.getType(),
									t.getInterval(), t.getDescription() };

							csvWrite.writeNext(arrStr);
						/*
						// Adding percentage number info
							progress += 100/listdata.size();
							
							mHandler.post(new Runnable() {
								public void run(){
									mProgress.setProgress(progress);
								}
							});
						*/
						}
						success = "true";

					}
					csvWrite.close();

				} catch (IOException e) {
					Log.e("ExportActivity", e.getMessage(), e);
					return success;
				}
			}
			return success;

		}

		@Override
		protected void onPostExecute(String result) {
			// Ensure that we succeeded writing the file.
			Context context = getApplicationContext();
			Toast toast;
			int duration = Toast.LENGTH_SHORT;

			if (result == "true") {
				toast = Toast.makeText(context, FLAG_EXPORT_S, duration);
			} else {
				if (memoryErr == true) {
					toast = Toast.makeText(context, FLAG_MEMORY_ERR, duration);
				} else {
					toast = Toast.makeText(context, FLAG_EXPORT_F, duration);
				}
			}
			toast.show();
			mProgress.setVisibility(View.GONE);
		}

		@Override
		protected void onPreExecute() {
		}

		protected void onProgressUpdate(Integer... values) {
		}
	}
}