package com.codepath.example.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {
	EditText etEditItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		String itemText = getIntent().getStringExtra("text");
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		etEditItem.setText(itemText);
		etEditItem.setSelection(etEditItem.getText().length());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void saveTodoItem(View c) {
		int position = getIntent().getIntExtra("position", 0);
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		Intent data = new Intent();
		data.putExtra("editedText", etEditItem.getText().toString());
		data.putExtra("position", position);
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish(); // closes the activity, pass data to parent
	}
}
