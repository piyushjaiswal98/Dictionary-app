package com.example.user.dictionarytry;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    static EditText editText;
    Button button,button2,button3,fav;
    static TextView textView;
    ClipboardManager myClipboard;
    TextToSpeech t1uk,t1us;
    String word1;
    SQLiteDatabase db;
    String db_Name="DictionaryDatabase";
    SharedPreferences sp;
    String spreference="hisPre";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editText);
        button=findViewById(R.id.button);
        textView=findViewById(R.id.textView);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        fav=findViewById(R.id.fav);

        Intent i=getIntent();
        word1=i.getStringExtra("word1");
        editText.setText(word1);
        textView.setText(search(word1));
        sp=getSharedPreferences(spreference,MODE_PRIVATE);
        createDb();
        if(checkFav(word1))
        {
            fav.setBackgroundResource(R.mipmap.likesmallfilled);
        }
        else
            fav.setBackgroundResource(R.mipmap.likesmallempty);
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        t1uk=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1uk.setLanguage(Locale.UK);
                }
            }
        });
        t1us=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1us.setLanguage(Locale.US);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word=editText.getText().toString().trim();
                searchAndDisplay(word);

            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    String word=editText.getText().toString().trim();
                    searchAndDisplay(word);
                    return true;
                }
                return false;
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word=editText.getText().toString();
                t1uk.speak(word,TextToSpeech.QUEUE_FLUSH,null);

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word=editText.getText().toString();
                t1us.speak(word,TextToSpeech.QUEUE_FLUSH,null);

            }
        });

        myClipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData abc = myClipboard.getPrimaryClip();
                ClipData.Item item = abc.getItemAt(0);
                String text = item.getText().toString().trim();
                searchAndDisplay(text);

            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word=editText.getText().toString();
                if(checkFav(word))
                {
                    setUnfav(word);
                }
                else
                {
                    setFav(word);
                }
            }
        });

    }
    void searchAndDisplay(String word)
    {
        editText.setText(word);
        if(word.length()!=0)
        {
            textView.setText(search(word));
            writeHistory(word);
            if(checkFav(word))
            {
                fav.setBackgroundResource(R.mipmap.likesmallfilled);
            }
            else
                fav.setBackgroundResource(R.mipmap.likesmallempty);
        }
        else
            Toast.makeText(MainActivity.this,"Enter word first!",Toast.LENGTH_SHORT).show();
    }
    String search(String word)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
        databaseAccess.open();
        String meaning;
        meaning=databaseAccess.getMeaning(camelCase(word.trim()));
        databaseAccess.close();
        return meaning;
    }
    String camelCase(String word)
    {
        String a1=word.toLowerCase();
        String a2=a1.substring(0, 1).toUpperCase() + a1.substring(1);
        return a2;
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
            Toast.makeText(MainActivity.this,"Failed to create Database",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    void setFav(String word)
    {
        fav.setBackgroundResource(R.mipmap.likesmallfilled);
        addToDb(word);
    }
    void setUnfav(String word)
    {
        fav.setBackgroundResource(R.mipmap.likesmallempty);
        delFromDb(word);

    }
    void addToDb(String word)
    {
        String query="insert into favorites values(?)";
        db.execSQL(query,new String[]{word});
        Toast.makeText(MainActivity.this,"Word:"+word+" inserted into Favourites",Toast.LENGTH_LONG).show();
    }
    boolean checkFav(String word)
    {
        String query="select * from favorites where word=?";
        Cursor c1=db.rawQuery(query,new String[]{word});
        if(c1.moveToNext())
            return true;
        else
            return false;
    }
    void delFromDb(String word)
    {
        String query="delete from favorites where word=?";
        db.execSQL(query,new String[]{word});
        Toast.makeText(MainActivity.this,"Word:"+word+" deleted from Favourites",Toast.LENGTH_LONG).show();
    }
}
