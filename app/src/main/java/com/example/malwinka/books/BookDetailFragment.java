package com.example.malwinka.books;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malwinka.books.dummy.DummyContent;

/**
 * A fragment representing a single Book detail screen.
 * This fragment is either contained in a {@link BookListActivity}
 * in two-pane mode (on tablets) or a {@link BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = BooksContentProvider.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.booktitle);
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_detail, container, false);


        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            //int resID = getResources().getIdentifier(mItem.imagename, "drawable", getContext().getPackageName());
            ((TextView) rootView.findViewById(R.id.book_title)).setText(mItem.booktitle);
            ((TextView) rootView.findViewById(R.id.book_detail)).setText(mItem.shortcut);
            ((TextView) rootView.findViewById(R.id.book_author)).setText(mItem.author);
            ((TextView) rootView.findViewById(R.id.book_year)).setText(mItem.year);
            //((ImageView) rootView.findViewById(R.id.book_image)).setImageResource(resID);
            Bitmap image = BitmapFactory.decodeByteArray(mItem.getImagename(), 0, mItem.getImagename().length);
            ((ImageView) rootView.findViewById(R.id.book_image)).setImageBitmap(image);

        }

        FloatingActionButton fabedit = (FloatingActionButton) rootView.findViewById(R.id.fab_edit);
        fabedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String BookID = mItem.getBookId();
                Intent ModifyBook = new Intent(getContext(), NewBookActivity.class);
                final int result = 2;
                ModifyBook.putExtra("id",BookID);
                ModifyBook.putExtra("author",mItem.author);
                ModifyBook.putExtra("title", mItem.booktitle);
                ModifyBook.putExtra("shortcut",mItem.shortcut);
                ModifyBook.putExtra("year",mItem.year);
                ModifyBook.putExtra("img",mItem.imagename);
                getActivity().startActivityForResult(ModifyBook, result);
            }

        });

        FloatingActionButton fabdelete = (FloatingActionButton) rootView.findViewById(R.id.fab_delete);
        fabdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String BookID = mItem.getBookId();
                String ItemURI = BooksContentProvider.URL + "/" + BookID;
                Uri uri = Uri.parse(ItemURI);
                getActivity().getSupportFragmentManager().beginTransaction().remove(((BookListActivity) getActivity()).fragment).commit();
                getActivity().getBaseContext().getContentResolver().delete(uri, null, null);
                ((BookListActivity) getActivity()).UpdateAdapter();

            }

        });

        return rootView;
    }

}
