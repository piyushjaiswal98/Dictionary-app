package com.example.user.dictionarytry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class homepage extends AppCompatActivity {

    EditText editText1;
    Button button,helper,wotd,his,fav;
    String word;
    SQLiteDatabase db;
    String db_Name="DictionaryDatabase";
    SharedPreferences sp;
    String spreference="hisPre";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        editText1=findViewById(R.id.editText);
        button=findViewById(R.id.button);
        helper=findViewById(R.id.helper);
        wotd=findViewById(R.id.wotd);
        his=findViewById(R.id.his);
        fav=findViewById(R.id.fav);

        createDb();
        sp=getSharedPreferences(spreference,MODE_PRIVATE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferWord();

            }
        });
        editText1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    transferWord();
                }
                return false;
            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(homepage.this,favoriteActivity.class);
                startActivity(i);
            }
        });
        helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(homepage.this,Helper.class);
                startActivity(i);
            }
        });
        wotd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(homepage.this,Wotd.class);
                startActivity(i);
            }
        });
        his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(homepage.this,History.class);
                startActivity(i);
            }
        });






    }
    void transferWord()
    {
        word=editText1.getText().toString().trim();
        if(word.length()!=0)
        {
            writeHistory(word);
            Intent i=new Intent(homepage.this,MainActivity.class);
            i.putExtra("word1",word);
            startActivity(i);
        }
        else
            Toast.makeText(homepage.this,"Enter a word first!",Toast.LENGTH_SHORT).show();

    }
    boolean createDb()
    {
        db = openOrCreateDatabase(db_Name, MODE_PRIVATE, null);
        if(db!=null)
        {
            String query="create table if not exists favorites(word varchar(100))";
            db.execSQL(query);
            return true;
        }
        else
        {
            Toast.makeText(homepage.this,"Failed to create Database",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    void writeHistory(String value1)
    {
        String value=camelCase(value1);
        if(!search(value).equals("Word not found"))
        {
            String c1=sp.getString("count","0");
            int c= Integer.parseInt(c1);
            String k1="word";
            if(!checkInsharedpreference(value))
            {
                if(c!=10)
                {
                    c=c+1;
                }
                else
                    c=1;

                String key=k1+c;
                writeSharedpreference(key,value);
                writeSharedpreference("count",""+c);
            }
        }

    }
    void writeSharedpreference(String key,String value)
    {
        SharedPreferences.Editor editor= sp.edit();
        editor.putString(key,value);
        editor.commit();
    }
    boolean checkInsharedpreference(String value)
    {
        String c1=sp.getString("count","0");
        int check=0;
        if(c1.equals("0"))
            check=0;
        else
        {
            int c= Integer.parseInt(c1);
            String k1="word";
            for(int i=1;i<=c;i++)
            {
                String key=k1+i;
                String v1=sp.getString(key,"null");
                if(value.equals(v1))
                {
                    check=1;
                    break;
                }
            }
        }
        if(check==1)
            return true;
        else
            return false;
    }
    String camelCase(String word)
    {
        String a1=word.toLowerCase();
        String a2=a1.substring(0, 1).toUpperCase() + a1.substring(1);
        return a2;
    }
    String search(String word)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(homepage.this);
        databaseAccess.open();
        String meaning;
        meaning=databaseAccess.getMeaning(camelCase(word.trim()));
        databaseAccess.close();
        return meaning;
    }
}
