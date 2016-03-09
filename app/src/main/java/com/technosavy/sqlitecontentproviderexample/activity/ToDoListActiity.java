package com.technosavy.sqlitecontentproviderexample.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.technosavy.sqlitecontentproviderexample.R;
import com.technosavy.sqlitecontentproviderexample.database.contentprovider.TodoContentProvider;
import com.technosavy.sqlitecontentproviderexample.database.tables.TodoTable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ToDoListActiity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final int LOAD_TODO = 0;

    @Bind(R.id.listTods)
    ListView listTods;

    @Bind(R.id.tvEmpty)
    TextView tvEmpty;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        init();

        loadData();

    }


    private void init() {

        setSupportActionBar(toolbar);

        listTods.setOnItemClickListener(this);
    }


    @OnClick(R.id.fab)
    void createTodo() {
        Intent i = new Intent(this, TodoDetailsActivity.class);
        startActivity(i);
    }


    private void loadData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[]{TodoTable.COLUMN_SUMMARY, TodoTable.COLUMN_DESCRIPTION};

        // Fields on the UI to which we map
        int[] to = new int[]{R.id.tvSummary, R.id.tvDescription};

        getLoaderManager().initLoader(LOAD_TODO, null, this);

        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.row_todo, null, from, to, 0);

        listTods.setAdapter(simpleCursorAdapter);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader cursorLoader = null;
        switch (id) {

            case LOAD_TODO:
                String[] projection = {TodoTable.COLUMN_ID, TodoTable.COLUMN_SUMMARY, TodoTable.COLUMN_DESCRIPTION};

                cursorLoader = new CursorLoader(this, TodoContentProvider.CONTENT_URI, projection, null, null, null);

                return cursorLoader;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        simpleCursorAdapter.swapCursor(data);

        if (null != data && data.getCount() > 0) {

            tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // data is not available anymore, delete reference
        simpleCursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent i = new Intent(this, TodoDetailsActivity.class);
        Uri todoUri = Uri.parse(TodoContentProvider.CONTENT_URI + "/" + id);
        i.putExtra(TodoContentProvider.CONTENT_ITEM_TYPE, todoUri);
        startActivity(i);
    }
}
