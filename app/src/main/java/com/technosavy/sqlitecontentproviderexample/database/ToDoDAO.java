package com.technosavy.sqlitecontentproviderexample.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.technosavy.sqlitecontentproviderexample.database.contentprovider.TodoContentProvider;
import com.technosavy.sqlitecontentproviderexample.database.tables.TodoTable;

/**
 * Created by Nayanesh Gupte
 */
public class ToDoDAO {


    public static void saveState(Context context, Uri todoUri, String category, String summary, String description) {

        // only save if either summary or description
        // is available

        if (description.length() == 0 && summary.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(TodoTable.COLUMN_CATEGORY, category);
        values.put(TodoTable.COLUMN_SUMMARY, summary);
        values.put(TodoTable.COLUMN_DESCRIPTION, description);

        if (todoUri == null) {
            // New todo
            todoUri = context.getContentResolver().insert(TodoContentProvider.CONTENT_URI, values);
        } else {
            // Update todo
            context.getContentResolver().update(todoUri, values, null, null);
        }
    }




}
