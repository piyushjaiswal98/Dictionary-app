package com.example.user.dictionarytry;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

public class Wotd extends AppCompatActivity {
    TextView word,meaning;
    SharedPreferences s1;
    String sName="wotd";
    int d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wotd);

        word=findViewById(R.id.textView2);
        meaning=findViewById(R.id.textView3);

        s1=getSharedPreferences(sName,MODE_PRIVATE);
        String c1=s1.getString("date","0");
        Calendar c=Calendar.getInstance();
        d=c.get(Calendar.DAY_OF_MONTH);


        if(c1.equals(""+d))
        {
            String rWord=s1.getString("word1","null");
            word.setText(rWord);
            meaning.setText(search(rWord));
        }
        else
        {
            writeSharedpreference("date",""+d);
            String rWord=randomWord();
            writeSharedpreference("word1",rWord);
            word.setText(rWord);
            meaning.setText(search(rWord));
        }


    }

    String search(String word)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(Wotd.this);
        databaseAccess.open();
        String meaning;
        meaning=databaseAccess.getMeaning(word.trim());
        databaseAccess.close();
        return meaning;
    }
    String randomWord()
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(Wotd.this);
        databaseAccess.open();
        String word;
        word=databaseAccess.getRandom();
        databaseAccess.close();
        return word;
    }
    void writeSharedpreference(String key,String value)
    {
        SharedPreferences.Editor editor= s1.edit();
        editor.putString(key,value);
        editor.commit();
    }
}
