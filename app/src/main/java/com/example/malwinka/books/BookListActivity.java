package com.example.malwinka.books;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.malwinka.books.dummy.DummyContent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * An activity representing a list of Books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link BookListFragment} and the item details
 * (if present) is a {@link BookDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link BookListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class BookListActivity extends AppCompatActivity
        implements BookListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public BookDetailFragment fragment;

    public List<DummyContent.DummyItem> getAll(){

        List<DummyContent.DummyItem> AllBooks = new ArrayList<>();

        Uri books = BooksContentProvider.CONTENT_URI;
        Cursor c = getContentResolver().query(books, null, null, null, "title");

        if (c.moveToFirst()) {
            do{
                DummyContent.DummyItem book = new DummyContent.DummyItem(c.getLong(c.getColumnIndex(MySQLiteHelper.COLUMN_ID)),c.getString(c.getColumnIndex(MySQLiteHelper.COLUMN_AUTHORS)),c.getString(c.getColumnIndex(MySQLiteHelper.COLUMN_TITLES)),c.getString(c.getColumnIndex(MySQLiteHelper.COLUMN_SHORTCUTS)),c.getString(c.getColumnIndex(MySQLiteHelper.COLUMN_YEARS)),c.getBlob(c.getColumnIndex(MySQLiteHelper.COLUMN_IMAGES)));
                AllBooks.add(book);
                BooksContentProvider.ITEM_MAP.put(book.getBookId(), book);
            } while (c.moveToNext());
        }
        c.close();
        return AllBooks;

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //BooksDataSource datasource;
        //DummyContent dummy;
        //boolean firstTime = false;
        //datasource = new BooksDataSource(this);
        //File dbFile = this.getDatabasePath("Books.db");
        if (MySQLiteHelper.firstTime)
        {
            List<DummyContent.DummyItem> books = new ArrayList<>();
            books = DummyContent.getItems(this,"data.txt");
            for (DummyContent.DummyItem x : books)
                addNewBook(x);
            MySQLiteHelper.firstTime = false;

        }
        //datasource.open(this, firstTime);


        //dummy = new DummyContent();
        //dummy.getItems(this,"data.txt");




        setContentView(R.layout.activity_book_app_bar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GetNewBook = new Intent(getBaseContext(), NewBookActivity.class);
                final int result = 1;
                startActivityForResult(GetNewBook, result);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
            }

        });


        if (findViewById(R.id.book_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((BookListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.book_list))
                    .setActivateOnItemClick(true);
        }






        // TODO: If exposing deep links into your app, handle intents here.
    }

    public void addNewBook(DummyContent.DummyItem book){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_AUTHORS,book.getAuthor());
        values.put(MySQLiteHelper.COLUMN_TITLES,book.getTitle());
        values.put(MySQLiteHelper.COLUMN_SHORTCUTS, book.getShortcut());
        values.put(MySQLiteHelper.COLUMN_YEARS, book.getYear());
        values.put(MySQLiteHelper.COLUMN_IMAGES, book.getImagename());

        Uri uri = getContentResolver().insert(BooksContentProvider.CONTENT_URI,values);
        book.setId(Long.valueOf(uri.getLastPathSegment()));

        //BooksContentProvider.ITEM_MAP.put(String.valueOf(uri.getLastPathSegment()), book);

    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            String author = data.getStringExtra("Author");
            String title = data.getStringExtra("Title");
            String shortcut = data.getStringExtra("Shortcut");
            String year = data.getStringExtra("Year");
            byte[] image = data.getByteArrayExtra("Image");
            //ContentValues values = new ContentValues();

            //values.put(MySQLiteHelper.COLUMN_AUTHORS, author);
            //values.put(MySQLiteHelper.COLUMN_TITLES, title);
            //values.put(MySQLiteHelper.COLUMN_SHORTCUTS, shortcut);
            //values.put(MySQLiteHelper.COLUMN_YEARS, year);
            //values.put(MySQLiteHelper.COLUMN_IMAGES, image);


            DummyContent.DummyItem nowa = new DummyContent.DummyItem(null, author, title, shortcut, year, image);
            addNewBook(nowa);
            UpdateAdapter();
        }

        if(requestCode == 2) {
            String id = data.getStringExtra("id");
            String author = data.getStringExtra("Author");
            String title = data.getStringExtra("Title");
            String shortcut = data.getStringExtra("Shortcut");
            String year = data.getStringExtra("Year");
            byte[] image = data.getByteArrayExtra("Image");
            ContentValues values = new ContentValues();

            values.put(MySQLiteHelper.COLUMN_AUTHORS, author);
            values.put(MySQLiteHelper.COLUMN_TITLES, title);
            values.put(MySQLiteHelper.COLUMN_SHORTCUTS, shortcut);
            values.put(MySQLiteHelper.COLUMN_YEARS, year);
            values.put(MySQLiteHelper.COLUMN_IMAGES, image);
            String ItemURI = BooksContentProvider.URL + "/" + id;
            Uri uri = Uri.parse(ItemURI);


            getContentResolver().update(uri, values, null, null);
            UpdateAdapter();
            //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //ft.detach(fragment).commit();
            onItemSelected(id);


        }


        //BooksDataSource.createBook(author, title, shortcut, year, image);
               // ((BookListFragment) getSupportFragmentManager()
                 //       .findFragmentById(R.id.book_list))
                //.update();
        //UpdateAdapter();

    }

    public void UpdateAdapter(){
        ((BookListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.book_list))
                .update();
    }

    /**
     * Callback method from {@link BookListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(BookDetailFragment.ARG_ITEM_ID, id);
            //BookDetailFragment fragment = new BookDetailFragment();
            fragment = new BookDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.book_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, BookDetailActivity.class);
            detailIntent.putExtra(BookDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }




    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }



}
