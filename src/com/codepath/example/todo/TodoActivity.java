package com.codepath.example.todo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	ArrayList<Long> completionDates;
	ArrayList<Long> ids;

	ArrayAdapter<String> itemsAdapter;
	ListView lvItems;
	
	private ItemsDataSource itemsDataSource;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
		completionDates = new ArrayList<Long>();
		ids = new ArrayList<Long>();
        
		itemsDataSource = new ItemsDataSource(this);
		itemsDataSource.open();
		List<Item> entries = itemsDataSource.getAllItems();
		
		for (Item entry : entries) {
			items.add(entry.getItem());
			completionDates.add(entry.getCompletionDate());
			ids.add(entry.getId());
		}
        
        itemsAdapter = new ArrayAdapter<String>(getBaseContext(), 
        		android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        	
        setupListViewListener();
        setupListClickListener();
    }	
    
    public void addTodoItem(View v) {
    	EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
    	if (etNewItem.getText().length() > 0) {
    		Item item = itemsDataSource.createItem(etNewItem.getText().toString(), new Date().getTime());
    		itemsAdapter.add(item.getItem());
    		etNewItem.setText("");
    		completionDates.add(item.getCompletionDate());
    		ids.add(item.getId());
    	}
    }
    
    private void setupListViewListener() {
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Item item = new Item();
				item.setItem(items.get(position));
				item.setCompletionDate(completionDates.get(position));
				item.setId(ids.get(position));
				itemsDataSource.deleteItem(item);
				
				items.remove(position);
				itemsAdapter.notifyDataSetChanged();
				completionDates.remove(position);
				ids.remove(position);

				return true;
			}
		});
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
         Long editedDate = data.getExtras().getLong(EditItemActivity.EDITED_DATE);

         if (editedText.length() > 0) {
        	 int position = data.getIntExtra(EditItemActivity.EDITED_POSITION, items.size());
				Item item = new Item();
				item.setItem(items.get(position));
				item.setCompletionDate(completionDates.get(position));
				item.setId(ids.get(position));
				itemsDataSource.deleteItem(item);
        	 
				items.set(position, editedText);
				itemsAdapter.notifyDataSetChanged();
				completionDates.set(position, editedDate);

	    		item = itemsDataSource.createItem(editedText, editedDate);
	    		ids.set(position, item.getId());
         }
      }
    }
}
