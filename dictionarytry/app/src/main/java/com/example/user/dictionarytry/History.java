package com.example.user.dictionarytry;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class History extends ListActivity {
    List<String> allHisWord;
    SharedPreferences sp;
    String spreference="hisPre";
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        allHisWord=new ArrayList<String>();

        sp=getSharedPreferences(spreference,MODE_PRIVATE);


        String c1=sp.getString("count","0");
        if(!c1.equals("0"))
        {

            String k1="word";
            for(int i=1;i<=10;i++)
            {
                String key=k1+i;
                if(!sp.getString(key,"null").equals("null"))
                    allHisWord.add(sp.getString(key,"null"));
            }
            Collections.sort(allHisWord);
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,allHisWord);
            setListAdapter(adapter);
        }
        else
            Toast.makeText(History.this,"History is empty as of now.",Toast.LENGTH_SHORT).show();

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
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(History.this);
        databaseAccess.open();
        String meaning;
        meaning=databaseAccess.getMeaning(camelCase(word));
        databaseAccess.close();
        return meaning;
    }
    String camelCase(String word)
    {
        String a1=word.toLowerCase();
        return (a1.substring(0, 1).toUpperCase() + a1.substring(1));
    }

}
