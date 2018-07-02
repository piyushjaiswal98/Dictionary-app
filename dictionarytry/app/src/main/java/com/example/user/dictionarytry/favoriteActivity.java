package com.example.user.dictionarytry;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class favoriteActivity extends ListActivity {

    List<String> allwords;
    SQLiteDatabase db;
    String db_Name="DictionaryDatabase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        allwords=new ArrayList<String>();

        db = openOrCreateDatabase(db_Name, MODE_PRIVATE, null);

        String query="select * from favorites";
        Cursor c=db.rawQuery(query,null);
        while (c.moveToNext())
        {
            String word1=c.getString(0);
            allwords.add(word1);
        }
        Collections.sort(allwords);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,allwords);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String word=""+l.getItemAtPosition(position);
        String meaning=""+search(word);
        showData(word,meaning);
    }
    void showData(String word,String meaning)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("----"+word+"----");
        builder.setMessage(meaning);
        builder.setIcon(R.mipmap.icon);
        builder.create();
        builder.show();
    }
    String search(String word)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(favoriteActivity.this);
        databaseAccess.open();
        String meaning;
        meaning=databaseAccess.getMeaning(camelCase(word));
        databaseAccess.close();
        return meaning;
    }
    String camelCase(String word)
    {
        String a1=word.toLowerCase();
        String a2=a1.substring(0, 1).toUpperCase() + a1.substring(1);
        return a2;
    }

}
