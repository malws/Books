package com.example.malwinka.books.dummy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.malwinka.books.BookListActivity;
import com.example.malwinka.books.BookListFragment;
import com.example.malwinka.books.BooksAdapter;
import com.example.malwinka.books.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    public static List<DummyContent.DummyItem> getItems(Context ctx,String fileName)
    {
        List<DummyContent.DummyItem> books = new ArrayList<>();
        String line;
        BufferedReader br;
        Bitmap bitmap;
        String author;
        String title;
        String shortcut;
        String year;
        String path;
        try {
            br = new BufferedReader(new InputStreamReader(ctx.getAssets().open(fileName)));

            while ((line = br.readLine()) != null) {
                StringTokenizer sTok = new StringTokenizer(line, ":");
                author = sTok.nextToken();
                title = sTok.nextToken();
                shortcut = sTok.nextToken();
                year = sTok.nextToken();
                path = sTok.nextToken() + ".png";
                InputStream ims = ctx.getAssets().open(path);
                    bitmap = BitmapFactory.decodeStream(ims);
                    byte[] frontImage = getBitmapAsByteArray(bitmap);


                DummyItem newbook = new DummyItem(null,author, title, shortcut, year, frontImage);
                books.add(newbook);
                //BooksDataSource.createBook(sTok.nextToken(), sTok.nextToken(), sTok.nextToken(), sTok.nextToken(), sTok.nextToken());


            }
        } catch (IOException e) {
        }
        return books;

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    //public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();



    public static class DummyItem {
        public Long id;
        public String author;
        public String booktitle;
        public String shortcut;
        public String year;
        //public String imagename;
        public byte[] imagename;

        public DummyItem(Long id, String author, String booktitle, String shortcut, String year, byte[] imagename) {
            this.id = id;
            this.author = author;
            this.booktitle = booktitle;
            this.shortcut = shortcut;
            this.year = year;
            this.imagename = imagename;
        }

        public String getBookId() { return Long.toString(id); }
        public String getAuthor() { return author; }
        public String getTitle() { return booktitle; }
        public String getShortcut() { return shortcut; }
        public String getYear() { return year; }
        public byte[] getImagename() { return imagename; }

        public void setId(Long Newid) { this.id = Newid; }

        @Override
        public String toString() {
            return booktitle;
        }

    }
}
