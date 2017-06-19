package com.example.malwinka.books;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.malwinka.books.dummy.DummyContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by Malwinka on 2015-11-28.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_BOOKS = "Books";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AUTHORS = "author";
    public static final String COLUMN_TITLES = "title";
    public static final String COLUMN_SHORTCUTS = "shortcut";
    public static final String COLUMN_YEARS = "year";
    public static final String COLUMN_IMAGES = "image";
    //public static final String COLUMN_COMMENT = "comment";

    private static final String DATABASE_NAME = "Books.db";
    private static final int DATABASE_VERSION = 1;
    private static Context ctx;
    public static boolean firstTime = false;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_BOOKS + "("
            + COLUMN_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_AUTHORS  + " TEXT NOT NULL, "
            + COLUMN_TITLES   + " TEXT NOT NULL, "
            + COLUMN_SHORTCUTS  + " TEXT NOT NULL, "
            + COLUMN_YEARS    + " TEXT NOT NULL, "
            + COLUMN_IMAGES   + " BLOB NOT NULL);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        firstTime = true;
        //DummyContent.getItems(ctx,"data.txt");
        //putExampleData(database);
    }

    /*public void putExampleData(SQLiteDatabase database){
        String shortcut1 = "'Mark Watney kilka dni temu był jednym z pierwszych ludzi, którzy stanęli na Marsie. Teraz jest pewien, że będzie pierwszym, który tam umrze! Straszliwa burza piaskowa sprawia, że marsjańska ekspedycja, w której skład wchodzi Mark Watney, musi ratować się ucieczką z Czerwonej Planety. Kiedy ciężko ranny Mark odzyskuje przytomność, stwierdza, że został na Marsie sam w zdewastowanym przez wichurę obozie, z minimalnymi zapasami powietrza i żywności, a na dodatek bez łączności z Ziemią. Co gorsza, zarówno pozostali członkowie ekspedycji, jak i sztab w Houston uważają go za martwego, nikt więc nie zorganizuje wyprawy ratunkowej; zresztą, nawet gdyby wyruszyli po niego niemal natychmiast, dotarliby na Marsa długo po tym, jak zabraknie mu powietrza, wody i żywności. Czyżby to był koniec? Nic z tego. Mark rozpoczyna heroiczną walkę o przetrwanie, w której równie ważną rolę, co naukowa wiedza, zdolności techniczne i pomysłowość, odgrywają niezłomna determinacja i umiejętność zachowania dystansu wobec siebie i świata, który nie zawsze gra fair…'";
        String shortcut2 = "'Pasjonująca, pełna przygód opowieść o najsłynniejszym Indianinie Ameryki - Winnetou, jego bracie krwi - Old Shatterhandzie i innych bohaterach Dzikiego Zachodu - tych szlachetnych i tych wyjętych spod prawa.'";
        String shortcut3 = "'Początek XVIII wieku. Czarnobrody, Czarny Sam Bellamy, Charles Vane i kilku innych wielkich pirackich kapitanów łączy siły, tworząc coś więcej niż przypadkową zbieraninę złodziei. Byli żeglarze, niezadowoleni służący, zbiegli niewolnicy – każdy z nich zwrócił się ku piractwu w wyrazie protestu przeciwko warunkom panującym na statkach i plantacjach. Wspólnymi siłami ustanawiają surową demokrację, wykrawając własną strefę swobody, w której służący stali się wolni, Czarni zyskali status równych, a przywódców wybierano i obalano na drodze głosowania. Posiłkując się szczegółowymi badaniami archiwów brytyjskich i amerykańskich, Colin Woodard opowiada dramatyczną historię Republiki Piratów, która śmiała zatrząść fundamentami brytyjskiego i hiszpańskiego imperium, a także rozprzestrzeniła idee demokratyczne. Kilkadziesiąt lat później doprowadziły one do powstania Stanów Zjednoczonych.'";
        database.execSQL("INSERT INTO " + TABLE_BOOKS + " (_id, author, title, shortcut, year, image) VALUES ('1', 'Andy Weir', 'Marsjanin', " + shortcut1 + ", '2014', 'img1')");
        database.execSQL("INSERT INTO " + TABLE_BOOKS + " (_id, author, title, shortcut, year, image) VALUES ('2', 'Karol May', 'Winnetou', " + shortcut2 + ", '1998', 'img2')");
        database.execSQL("INSERT INTO " + TABLE_BOOKS + " (_id, author, title, shortcut, year, image) VALUES ('3', 'Colin Woodard', 'Republika Piratów', " + shortcut3 + ", '2014', 'img3')");
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

}
