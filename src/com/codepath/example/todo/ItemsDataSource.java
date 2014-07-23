package com.codepath.example.todo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ItemsDataSource {
	// Database fields
	private SQLiteDatabase database;
	private ToDoMySQLite toDoDB;
	private String[] allColumns = { ToDoMySQLite.COLUMN_ID,
			ToDoMySQLite.COLUMN_ITEM,
			ToDoMySQLite.COLUMN_COMPLETION_DATE };

	public ItemsDataSource(Context context) {
		toDoDB = new ToDoMySQLite(context);
	}

	public void open() throws SQLException {
		database = toDoDB.getWritableDatabase();
	}

	public void close() {
		toDoDB.close();
	}

	public Item createItem(String item_text, long completion_date) {
		ContentValues values = new ContentValues();
		values.put(ToDoMySQLite.COLUMN_ITEM, item_text);
		values.put(ToDoMySQLite.COLUMN_COMPLETION_DATE, completion_date);
		long insertId = database.insert(ToDoMySQLite.TABLE_ITEMS, null, values);
		Cursor cursor = database.query(ToDoMySQLite.TABLE_ITEMS,
				allColumns, ToDoMySQLite.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Item newItem = cursorToItem(cursor);
		cursor.close();
		return newItem;
	}

	public void deleteItem(Item item) {
		long id = item.getId();
		System.out.println("Item deleted with id: " + id);
		database.delete(ToDoMySQLite.TABLE_ITEMS, ToDoMySQLite.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Item> getAllItems() {
		List<Item> items = new ArrayList<Item>();

		Cursor cursor = database.query(ToDoMySQLite.TABLE_ITEMS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Item item = cursorToItem(cursor);
			items.add(item);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return items;
	}

	private Item cursorToItem(Cursor cursor) {
		Item item = new Item();
		item.setId(cursor.getLong(0));
		item.setItem(cursor.getString(1));
		item.setCompletionDate(cursor.getLong(2));
		return item;
	}

}
