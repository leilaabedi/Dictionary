package com.maktab.dictionaryedit.controller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maktab.dictionaryedit.R;
import com.maktab.dictionaryedit.controller.fragment.WordsAdapter;
import com.maktab.dictionaryedit.database.DatabaseAccess;
import com.maktab.dictionaryedit.model.Words;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editTextSearch;
    ArrayList<Words> allEnWords;
    ArrayList<Words> allFaWords;
    WordsAdapter mAdapter;
    RecyclerView wordView;
    RadioGroup rdgroup;
    RadioButton rden1, rdfa1;
    DatabaseAccess databaseAccess;
    public static Boolean lang = false;
    FloatingActionButton add_btn;
    private boolean mIsSubtitleVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findview();
        initview();
        listener();
    }


    private void findview() {
        editTextSearch = findViewById(R.id.editTextSearch);
        wordView = findViewById(R.id.word_list);
        rdgroup = findViewById(R.id.radiogroup);
        rden1 = findViewById(R.id.entofa);
        rdfa1 = findViewById(R.id.fatoen);
        add_btn = findViewById(R.id.add_word);

    }


    private void initview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        wordView.setLayoutManager(linearLayoutManager);
        wordView.setHasFixedSize(true);
        databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        selectEnglish();
    }


    private void selectEnglish() {
        allEnWords = databaseAccess.listEnglishWords();
        if (allEnWords.size() > 0) {
            wordView.setVisibility(View.VISIBLE);
            mAdapter = new WordsAdapter(this, allEnWords);
            wordView.setAdapter(mAdapter);
        } else {
            wordView.setVisibility(View.GONE);
            //Toast.makeText(this, "There is no contact in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
    }


    private void selectPersion() {
        allFaWords = databaseAccess.listPersianWords();
        if (allFaWords.size() > 0) {
            wordView.setVisibility(View.VISIBLE);
            mAdapter = new WordsAdapter(this, allFaWords);
            wordView.setAdapter(mAdapter);
        } else {
            wordView.setVisibility(View.GONE);
            //Toast.makeText(this, "There is no contact in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
    }

    private void listener() {

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        rdgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rden1.getId()) {
                    rden1.setChecked(true);
                    lang = false;
                    selectEnglish();
                } else if (checkedId == rdfa1.getId()) {
                    rdfa1.setChecked(true);
                    lang = true;
                    selectPersion();

                }
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
            }
        });

    }


    private void addTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_word, null);
        final EditText EnField = subView.findViewById(R.id.addEnword);
        final EditText FaField = subView.findViewById(R.id.addFaword);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new WORD");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("ADD WORD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String enword = EnField.getText().toString();
                final String faword = FaField.getText().toString();
                if (TextUtils.isEmpty(enword) || TextUtils.isEmpty(faword)) {
                    Toast.makeText(MainActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    Words newWord = new Words(enword, faword);

                    if (lang == false)
                        databaseAccess.addWordEn(newWord);
                    else {
                        databaseAccess.addWordFa(newWord);
                        finish();
                        startActivity(getIntent());
                        selectPersion();

                    }
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    private void filter(String text) {
        ArrayList<Words> filterdNames = new ArrayList<>();
        ArrayList<Words> templist = new ArrayList<>();
        if (lang == false)
            templist = allEnWords;
        else
            templist = allFaWords;

        for (Words item : templist) {
            if (item.getEnword().toLowerCase().contains(text.toLowerCase())) {
                filterdNames.add(item);
            }
        }

        mAdapter.filterList(filterdNames);
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.menu_item_subtitle);
        setMenuItemSubtitle(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_item_subtitle:
                mIsSubtitleVisible = !mIsSubtitleVisible;
                updateSubtitle();
                setMenuItemSubtitle(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        int numberOfCrimes = databaseAccess.listEnglishWords().size();
        String wordText = mIsSubtitleVisible ? numberOfCrimes + " words" : null;


        this.getSupportActionBar().setSubtitle(wordText);
    }

    private void setMenuItemSubtitle(MenuItem item) {
        item.setTitle(
                mIsSubtitleVisible ?
                        R.string.menu_item_hide_subtitle :
                        R.string.menu_item_show_subtitle);
    }


}