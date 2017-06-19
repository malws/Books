package com.example.malwinka.books;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.malwinka.books.dummy.DummyContent;

import java.util.List;

/**
 * Created by Malwinka on 2015-11-19.
 */
public class BooksAdapter extends ArrayAdapter<DummyContent.DummyItem> {

    private List<DummyContent.DummyItem> BooksList;
    private Context context;



    public BooksAdapter(List<DummyContent.DummyItem> BooksList, Context ctx) {
        super(ctx, R.layout.list_item_view_custom, BooksList);
        this.BooksList = BooksList;
        this.context = ctx;
    }


    public int getCount() {
        return BooksList.size();
    }

    public DummyContent.DummyItem getItem(int position) {
        return BooksList.get(position);
    }

    public long getItemId(int position) {
        return BooksList.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        BookHolder holder = new BookHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item_view_custom, null);
            // Now we can fill the layout with the right values

            holder.title = (TextView) v.findViewById(R.id.textView_item_custom);
            holder.author = (TextView) v.findViewById(R.id.textView_item_custom_author);
            holder.image = (ImageView) v.findViewById(R.id.imageView_item_custom);

            v.setTag(holder);
        }
        else
            holder = (BookHolder) v.getTag();

        DummyContent.DummyItem p = BooksList.get(position);
        //int resID = context.getResources().getIdentifier(p.imagename, "drawable", context.getPackageName());
        Bitmap image = BitmapFactory.decodeByteArray(p.getImagename(), 0, p.getImagename().length);
        holder.title.setText(p.getTitle());
        holder.author.setText(p.getAuthor());
        //holder.image.setImageResource(resID);
        holder.image.setImageBitmap(image);


        return v;
    }

		/* *********************************
		 * We use the holder pattern
		 * It makes the view faster and avoid finding the component
		 * **********************************/

    private static class BookHolder {
        public TextView title;
        public TextView author;
        public ImageView image;
    }
}
