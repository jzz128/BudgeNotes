package com.example.budgetnotebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ImportActivity extends Activity {

	public static final String TAG = "ImportActivity";
	public static final String FILE_DIR_NAME = "import";
	public static final String FILE_NAME = "BudgeNotes";
	public static final String[] CSV_HEADER = { "CSV_HEADER", "CSV_HEADER" };
	public static final String FLAG_IMPORT_S = "Import Sucess";
	public static final String FLAG_FILE_ERR = "File Error";
	public static final String FLAG_IMPORT_F = "Import Failure";
	Button button;
	TextView dirPath;
	DBHelper db;
	String currentDateString;
	File importDir;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_transaction);

		importDir = new File(Environment.getExternalStorageDirectory()
				+ File.separator + FILE_DIR_NAME, "");
		Log.v(TAG, "exportDir path::" + importDir);
		if (!importDir.exists()) {
			importDir.mkdirs();
		}

		db = new DBHelper(getBaseContext());
		dirPath = (TextView) findViewById(R.id.importPath);
		dirPath.setText(importDir.toString());
		button = (Button) findViewById(R.id.executeImport);

		button.setOnClickListener(onImport);
	}

	private View.OnClickListener onImport = new View.OnClickListener() {
		public void onClick(View view) {
			// detect the view that was "clicked"
			switch (view.getId()) {
			case R.id.executeImport:
				new importOperation().execute("");
				break;
			}
		}
	};

	private class importOperation extends AsyncTask<String, Void, String> {
		@SuppressWarnings("unused")
		private final ProgressDialog dialog = new ProgressDialog(
				ImportActivity.this);
		boolean fileErr = false;

		@Override
		protected String doInBackground(String... params) {
			String success = "false";

			File dbFile = getDatabasePath(DBHelper.DATABASE_NAME);
			Log.v(TAG, "Db path is: " + dbFile); // get the path of db

			EditText fileName = (EditText) findViewById(R.id.importFile);
			String fileNameStr = fileName.getText().toString().trim();

			if (fileNameStr != null && !fileNameStr.isEmpty()) {

				String importFileStr = importDir.toString() + File.separator
						+ fileNameStr;

				Log.v(TAG, "import file path::" + importFileStr);

				try {
					CSVReader reader = new CSVReader(new FileReader(
							importFileStr));

					String[] nextLine;
					@SuppressWarnings("unused")
					int rowNumber = 0;
					int transAccountI;
					String transCategoryS;
					String transIntervalS;
					String transTypeS;
					String transNameS;
					String transDateS;
					String transAmountS;
					String transDescriptionS;
					boolean transAccounted; // Added to incorporate new transaction table.
					String transChange;

					while ((nextLine = reader.readNext()) != null) {
						rowNumber++;
						for (int i = 0; i < nextLine.length; i++) {
							// retrieve CSV values
							System.out.println("Cell column index: " + i);
							System.out.println("Cell Value: " + nextLine[i]);
							System.out.println("---");
							transAccountI = Integer.valueOf(nextLine[0]);
							transNameS = nextLine[1];
							transDateS = nextLine[2];
							transAmountS = nextLine[3];
							transCategoryS = nextLine[4];
							transTypeS = nextLine[5];
							transIntervalS = nextLine[6];
							transDescriptionS = nextLine[7];
							transAccounted = Boolean.parseBoolean(nextLine[8]);
							transChange = nextLine[9];
							
							db.addTransaction(new Transaction(transAccountI,
									transNameS, transDateS, transAmountS,
									transCategoryS, transTypeS, transIntervalS,
									transDescriptionS, transAccounted, transChange));
						}
					}
					success = "true";
					reader.close();
					return success;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fileErr = true;
					return "failure";
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "failure";
				}

			}
			return success;
		}

		@Override
		protected void onPostExecute(String result) {
			Context context = getApplicationContext();
			Toast toast;
			int duration = Toast.LENGTH_SHORT;

			if (result == "true") {
				toast = Toast.makeText(context, FLAG_IMPORT_S, duration);
			} else {
				if (fileErr == true) {
					toast = Toast.makeText(context, FLAG_FILE_ERR, duration);
				} else {
					toast = Toast.makeText(context, FLAG_IMPORT_F, duration);
				}
			}
			toast.show();
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

}
