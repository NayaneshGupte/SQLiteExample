package com.technosavy.sqlitecontentproviderexample.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.technosavy.sqlitecontentproviderexample.R;
import com.technosavy.sqlitecontentproviderexample.database.ToDoDAO;
import com.technosavy.sqlitecontentproviderexample.database.contentprovider.TodoContentProvider;
import com.technosavy.sqlitecontentproviderexample.database.tables.TodoTable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TodoDetailsActivity extends AppCompatActivity {


    @Bind(R.id.category)
    Spinner mCategory;

    @Bind(R.id.todo_edit_summary)
    EditText mTitleText;

    @Bind(R.id.todo_edit_description)
    EditText mBodyText;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    private Uri todoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_details);

        ButterKnife.bind(this);

        initToolbar();

        getDataFromBundle();
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }


    @OnClick(R.id.fab)
    void addToDo() {

        saveState();

        if (TextUtils.isEmpty(mTitleText.getText().toString())) {

            Toast.makeText(TodoDetailsActivity.this, R.string.enter_summary, Toast.LENGTH_LONG).show();


        } else {
            setResult(RESULT_OK);
            finish();
        }
    }


    private void getDataFromBundle() {

        Bundle bundle = getIntent().getExtras();

        // Or passed from the other activity
        if (bundle != null) {
            todoUri = bundle.getParcelable(TodoContentProvider.CONTENT_ITEM_TYPE);

            fillData(todoUri);
        }
    }


    private void saveState() {

        String category = (String) mCategory.getSelectedItem();
        String summary = mTitleText.getText().toString();
        String description = mBodyText.getText().toString();

        ToDoDAO.saveState(this, todoUri, category, summary, description);
    }

    private void fillData(Uri uri) {

        String[] projection = {TodoTable.COLUMN_SUMMARY, TodoTable.COLUMN_DESCRIPTION, TodoTable.COLUMN_CATEGORY};


        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            String category = cursor.getString(cursor.getColumnIndexOrThrow(TodoTable.COLUMN_CATEGORY));


            for (int i = 0; i < mCategory.getCount(); i++) {

                String s = (String) mCategory.getItemAtPosition(i);
                if (s.equalsIgnoreCase(category)) {
                    mCategory.setSelection(i);
                }
            }

            mTitleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(TodoTable.COLUMN_SUMMARY)));

            mBodyText.setText(cursor.getString(cursor.getColumnIndexOrThrow(TodoTable.COLUMN_DESCRIPTION)));


            // always close the cursor
            cursor.close();
        }
    }
}
