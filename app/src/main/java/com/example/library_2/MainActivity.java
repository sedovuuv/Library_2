package com.example.library_2;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
public class MainActivity extends AppCompatActivity {

    ListView bookList;
    Button add,del;
    EditText bookName,bookAuthor, bookYear;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    SQLiteDatabase database;
    OpenHelper openHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openHelper = new OpenHelper(getApplicationContext());
        preferences = getSharedPreferences("Books", MODE_PRIVATE);
        editor = preferences.edit();

        bookList = findViewById(R.id.book_list);
        database = openHelper.getWritableDatabase();
        add = findViewById(R.id.add);
        del = findViewById(R.id.del);

        bookName = findViewById(R.id.name);
        bookAuthor = findViewById(R.id.author);
        bookYear = findViewById(R.id.year);
        LinkedList <Book> bookLinkedList = new LinkedList<>();
        Cursor cursor = database.query(openHelper.TABLE_NAME, new String[]{openHelper.COLUMN_TITLE, openHelper.COLUMN_AUTHOR, openHelper.COLUMN_YEAR}, OpenHelper.COLUMN_TITLE + " != \"\"", new String[]{}, null, null, new String());

        cursor.moveToFirst();
        while(cursor.moveToNext()){
            Book book = new Book(cursor.getString(cursor.getColumnIndexOrThrow(openHelper.COLUMN_TITLE)),cursor.getString(cursor.getColumnIndexOrThrow(openHelper.COLUMN_AUTHOR)),cursor.getInt(cursor.getColumnIndexOrThrow(openHelper.COLUMN_YEAR)),R.drawable.book);
            bookLinkedList.add(book);
        }

        String[]keyArray = {"title","author","year","cover","genre"};
        int [] idArray = {R.id.book_title,R.id.author,R.id.year,R.id.image,R.id.genre};

        LinkedList <HashMap<String,Object>> listForAdapter=new LinkedList<>();
        for (int i = 0; i < bookLinkedList.size(); i++) {
            HashMap <String,Object>bookMap = new HashMap<>();

            bookMap.put(keyArray[0],bookLinkedList.get(i).title);
            bookMap.put(keyArray[1],bookLinkedList.get(i).author);
            bookMap.put(keyArray[2],bookLinkedList.get(i).year);
            bookMap.put(keyArray[3],bookLinkedList.get(i).coverId);
            bookMap.put(keyArray[4],bookLinkedList.get(i).genre);

            listForAdapter.add(bookMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listForAdapter,R.layout.list_item,keyArray,idArray);

        bookList.setAdapter(simpleAdapter);
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getApplicationContext(), bookLinkedList.get(i).toString(), Toast.LENGTH_SHORT).show();
                FragmentManager manager = getSupportFragmentManager();

                MyAlertDialog myDialogFragment = new MyAlertDialog();

                Bundle args = new Bundle();

                String a = listForAdapter.get(i).get("title").toString() + listForAdapter.get(i).get("author").toString();
                args.putString("name",a );
                myDialogFragment.setArguments(args);
                FragmentTransaction transaction = manager.beginTransaction();
                myDialogFragment.show(transaction, "dialog");
            }
        });



        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String name1 = bookName.getText().toString();
                String author1 = bookAuthor.getText().toString();
                int year1 = 0;

                try {
                    year1 = Integer.parseInt(bookYear.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Введите год числом", Toast.LENGTH_SHORT).show();

                }
                if (year1!=0) {

                    Book book =  new Book(name1, author1, year1, R.drawable.book);
                    bookLinkedList.add(book);
                    HashMap<String, Object> bookMap = new HashMap<>();

                    bookMap.put(keyArray[0], name1);
                    bookMap.put(keyArray[1], author1);
                    bookMap.put(keyArray[2], year1);
                    bookMap.put(keyArray[3], R.drawable.book);
                    bookMap.put(keyArray[4], book.genre);

                    ContentValues values=new ContentValues();

                    values.put(OpenHelper.COLUMN_AUTHOR,author1);
                    values.put(OpenHelper.COLUMN_TITLE,name1);
                    values.put(OpenHelper.COLUMN_YEAR, year1);

                    database.insert(OpenHelper.TABLE_NAME, null, values);
                    listForAdapter.add(bookMap);
                    simpleAdapter.notifyDataSetChanged();
                }
                editor.commit();
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = bookName.getText().toString();
                String author1 = bookAuthor.getText().toString();
                for (int i = 0; i < listForAdapter.size(); i++) {
                    if(name1.equals(bookLinkedList.get(i).title) && author1.equals(bookLinkedList.get(i).author)){
                        listForAdapter.remove(i);
                        break;
                    }
                }
                database.delete(openHelper.TABLE_NAME, openHelper.COLUMN_TITLE + "=\"" + name1 + "\" AND " + openHelper.COLUMN_AUTHOR + "=\"" + author1+"\"", null);
                simpleAdapter.notifyDataSetChanged();
                editor.commit();
            }
        });

        simpleAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onStop() {
        super.onStop();
        database.close();
    }
}