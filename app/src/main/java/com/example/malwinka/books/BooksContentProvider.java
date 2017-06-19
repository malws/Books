package com.example.malwinka.books;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.malwinka.books.dummy.DummyContent;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Malwinka on 2016-01-10.
 */
public class BooksContentProvider extends ContentProvider {

    static final String AUTHORITY = "com.example.malwinka.provider.BooksContentProvider";
    static final String TABLE_NAME = MySQLiteHelper.TABLE_BOOKS;
    static final String URL = "content://" + AUTHORITY + "/" + TABLE_NAME;
    static final Uri CONTENT_URI = Uri.parse(URL);

    public static final int BOOKS = 1;
    public static final int BOOKS_ID = 2;
    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, TABLE_NAME, BOOKS);
        sURIMatcher.addURI(AUTHORITY, TABLE_NAME + "/#",
                BOOKS_ID);
    }

    private SQLiteDatabase db;
    private MySQLiteHelper myDB;
    public static Map<String, DummyContent.DummyItem> ITEM_MAP = new HashMap<>();

    @Override
    public boolean onCreate() {
        myDB = new MySQLiteHelper(getContext());
        db = myDB.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        switch (sURIMatcher.match(uri)) {
            case BOOKS:
                //qb.setProjectionMap(BooksDataSource.ITEM_MAP);
                break;

            case BOOKS_ID:
                qb.appendWhere( MySQLiteHelper.COLUMN_ID + "=" + uri.getPathSegments());
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on student names
             */
            sortOrder = MySQLiteHelper.COLUMN_TITLES;
        }
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);

        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(	TABLE_NAME, "", values);

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new IllegalArgumentException("Unknown URI: "
                + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int count = 0;
        switch (sURIMatcher.match(uri)){
            case BOOKS:
                count = db.delete(TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case BOOKS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    count = db.delete(TABLE_NAME,
                            MySQLiteHelper.COLUMN_ID + "=" + id,
                            null);
                } else {
                    count = db.delete(TABLE_NAME,
                            MySQLiteHelper.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (sURIMatcher.match(uri)){
            case BOOKS:
                count = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case BOOKS_ID:
                //count = db.update(TABLE_NAME, values, MySQLiteHelper.COLUMN_ID +
                //        " = " + uri.getLastPathSegment() +
                //(!TextUtils.isEmpty(selection) ? " AND (" +
                //selection + ')' : ""), selectionArgs);
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    count =
                            db.update(TABLE_NAME,
                                    values,
                                    MySQLiteHelper.COLUMN_ID + "=" + id,
                                    null);
                } else {
                    count =
                            db.update(TABLE_NAME,
                                    values,
                                    MySQLiteHelper.COLUMN_ID + "=" + id + " and "
                                            + selection,
                                    selectionArgs);
                }

                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }
}
