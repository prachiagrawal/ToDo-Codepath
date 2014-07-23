package com.codepath.example.todo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.example.todo.SetDateFragment.OnDateSelectedListener;

public class EditItemActivity extends Activity implements OnDateSelectedListener {
	public final static String MODULE = "EditItemAcitivity";
	
	EditText etEditItem;
	TextView tvSetDate;	
	Long mDate;
	SimpleDateFormat sdf_input;
	SimpleDateFormat sdf_show;
	Calendar cal;
		
	public final static String INPUT_TEXT = "inputText";
	public final static String INPUT_DATE = "inputDate";
	public final static String INPUT_POSITION = "inputPosition";

	public final static String EDITED_TEXT = "editedText";
	public final static String EDITED_DATE = "editedDate";
	public final static String EDITED_POSITION = "editedPosition";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);

		String itemText = getIntent().getStringExtra(INPUT_TEXT);
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		etEditItem.setText(itemText);
		etEditItem.setSelection(etEditItem.getText().length());
		
		mDate = getIntent().getLongExtra(INPUT_DATE, new Date().getTime());
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(mDate);	
		
		sdf_input = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
		sdf_show = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US);
	
		tvSetDate = (TextView) findViewById(R.id.tvSetDate);
		try {
			tvSetDate.setText("Complete by " + sdf_show.format(sdf_input.parse(new Date(mDate).toString())));
		} catch (ParseException e) {
			Log.e(MODULE, "Exception during parsing the date", e);
		}
	}
	
	public void saveTodoItem(View c) {
		int position = getIntent().getIntExtra(INPUT_POSITION, 0);
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		Intent data = new Intent();
		data.putExtra(EDITED_TEXT, etEditItem.getText().toString());
		data.putExtra(EDITED_POSITION, position);
		data.putExtra(EDITED_DATE, mDate);
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish(); // closes the activity, pass data to parent
	}
	
	public void setDate(View v) {
    	DialogFragment newFragment = new SetDateFragment();
        newFragment.show(getFragmentManager(), "dialog");
	}	
	
	public void onDateSelected(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.YEAR, year);
	    cal.set(Calendar.MONTH, month);
	    cal.set(Calendar.DAY_OF_MONTH, day);
		try {
			tvSetDate.setText("Complete by " + sdf_show.format(sdf_input.parse(cal.getTime().toString())));
		    mDate = cal.getTime().getTime();
		} catch (ParseException e) {
			Log.e(MODULE, "Exception during parsing the date", e);
		}
		CheckBox cbSetDate = (CheckBox) findViewById(R.id.cbSetDate);
		if (cbSetDate.isChecked()) {
			cbSetDate.setChecked(false);
        }
	}
}
