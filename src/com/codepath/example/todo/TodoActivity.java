package com.codepath.example.todo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {
	public final static String MODULE = "TodoActivity";
	private final int REQUEST_CODE = 10;
	
	ArrayList<String> items;
	ArrayList<String> completionDates;
	ArrayAdapter<String> itemsAdapter;
	ListView lvItems;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
		completionDates = new ArrayList<String>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(getBaseContext(), 
        		android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        setupListClickListener();
    }	
    
    public void addTodoItem(View v) {
    	EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
    	if (etNewItem.getText().length() > 0) {
    		itemsAdapter.add(etNewItem.getText().toString());
    		etNewItem.setText("");
    		completionDates.add(new Date().toString());
    		saveItems();
    	}
    }
    
    private void setupListViewListener() {
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				items.remove(position);
				itemsAdapter.notifyDataSetChanged();
				completionDates.remove(position);
				saveItems();
				return true;
			}
		});
    }
    
    private void readItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	File datesFile = new File(filesDir, "dates.txt");
    	try {
    		items = new ArrayList<String>(FileUtils.readLines(todoFile));
    		completionDates = new ArrayList<String>(FileUtils.readLines(datesFile));
    	} catch (Exception e) {
    		items = new ArrayList<String>();
    		completionDates = new ArrayList<String>();
			Log.e(MODULE, "Exception during reading items from the file", e);
    	}
    }
    
    private void saveItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	File datesFile = new File(filesDir, "dates.txt");
    	try {
    		FileUtils.writeLines(todoFile, items);
    		FileUtils.writeLines(datesFile, completionDates);
    	} catch (IOException e) {
			Log.e(MODULE, "Exception during writing lines to the file", e);
    	}
    }
    
    private void setupListClickListener() {
    	lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				launchEditView(position);
			}    		
		});
    }
    
    public void launchEditView(int position) {
      Intent intent = new Intent(this, EditItemActivity.class);
      intent.putExtra(EditItemActivity.INPUT_POSITION, position);
      intent.putExtra(EditItemActivity.INPUT_TEXT, items.get(position));
      intent.putExtra(EditItemActivity.INPUT_DATE, completionDates.get(position));
      startActivityForResult(intent, REQUEST_CODE); // brings up the edit item activity
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
         String editedText = data.getExtras().getString(EditItemActivity.EDITED_TEXT);
         String editedDate = data.getExtras().getString(EditItemActivity.EDITED_DATE);

         if (editedText.length() > 0) {
        	 int position = data.getIntExtra(EditItemActivity.EDITED_POSITION, items.size());
        	 items.set(position, editedText);
        	 itemsAdapter.notifyDataSetChanged();
        	 completionDates.set(position, editedDate);
        	 saveItems();
         }
      }
    }
}
