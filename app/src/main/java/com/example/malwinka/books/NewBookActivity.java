package com.example.malwinka.books;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class NewBookActivity extends AppCompatActivity {

    String ID;
    Bitmap front;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            ID = extras.getString("id");
            EditText Author = (EditText) findViewById(R.id.author_et);
            Author.setText(extras.getString("author"));
            EditText Title = (EditText) findViewById(R.id.title_et);
            Title.setText(extras.getString("title"));
            EditText Year = (EditText) findViewById(R.id.year_et);
            Year.setText(extras.getString("year"));
            //EditText Image = (EditText) findViewById(R.id.image_et);
            //Image.setText(extras.getString("img"));
            byte[] image = extras.getByteArray("img");
            front = BitmapFactory.decodeByteArray(image, 0, image.length);
            ImageView targetImage = (ImageView) findViewById(R.id.newdata_imageview);
            targetImage.setImageBitmap(front);

            EditText Shortcut = (EditText) findViewById(R.id.shortcut_et);
            Shortcut.setText(extras.getString("shortcut"));
            Button button = (Button) findViewById(R.id.newdata_button);
            button.setText("Modyfikuj");


        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public void sendTheBook (View view) {
        EditText NewAuthor = (EditText) findViewById(R.id.author_et);
        String Author = String.valueOf(NewAuthor.getText());
        EditText NewTitle = (EditText) findViewById(R.id.title_et);
        String Title = String.valueOf(NewTitle.getText());
        EditText NewYear = (EditText) findViewById(R.id.year_et);
        String Year = String.valueOf(NewYear.getText());
        //EditText NewImage = (EditText) findViewById(R.id.image_et);
        //String Image = String.valueOf(NewImage.getText());
        EditText NewShortcut = (EditText) findViewById(R.id.shortcut_et);
        String Shortcut = String.valueOf(NewShortcut.getText());

        byte[] Image = getBitmapAsByteArray(front);


        Intent GoBack = new Intent();
        GoBack.putExtra("id",ID);
        GoBack.putExtra("Author",Author);
        GoBack.putExtra("Title",Title);
        GoBack.putExtra("Year",Year);
        GoBack.putExtra("Image", Image);
        GoBack.putExtra("Shortcut",Shortcut);
        setResult(RESULT_OK, GoBack);

        finish();

    }

    public void getImage(View arg0){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            ImageView targetImage = (ImageView) findViewById(R.id.newdata_imageview);

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                front = Bitmap.createScaledBitmap(bitmap, 350, 500, true);
                targetImage.setImageBitmap(front);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
