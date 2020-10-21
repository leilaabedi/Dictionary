package com.maktab.dictionaryedit.controller.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.maktab.dictionaryedit.R;
import com.maktab.dictionaryedit.controller.activity.MainActivity;
import com.maktab.dictionaryedit.database.DatabaseAccess;
import com.maktab.dictionaryedit.model.Words;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class WordsAdapter extends RecyclerView.Adapter<WordViewHolder>
        implements Filterable {
    private Context context;
    private ArrayList<Words> listWords;
    private ArrayList<Words> mArrayList;
    DatabaseAccess databaseAccess;
    Words words;


    public WordsAdapter(Context context, ArrayList<Words> listWords) {
        this.context = context;
        this.listWords = listWords;
        this.mArrayList = listWords;

    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_layout, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        words = listWords.get(position);
        Typeface atf = Typeface.createFromAsset(context.getAssets(), "font/naz.ttf");
        holder.tvFaword.setTypeface(atf);
        holder.tvFaword.setTypeface(atf);
        holder.tvEnword.setText(words.getEnword());
        holder.tvFaword.setText(words.getFaword());

        databaseAccess = DatabaseAccess.getInstance(context.getApplicationContext());
        databaseAccess.open();

        holder.editWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(words);
            }
        });
        holder.deleteWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.lang == false)
                    databaseAccess.deleteEnglish(words);
                else
                    databaseAccess.deletePersian(words);

                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });

        holder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareReportIntent();


            }
        });


    }


    private void shareReportIntent() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getReport());
        sendIntent.setType("text/plain");

        Intent shareIntent =
                Intent.createChooser(sendIntent,"Dictionary Word");

        //we prevent app from crash if the intent has no destination.


        if (sendIntent.resolveActivity( ((Activity) context).getPackageManager()) != null)
            context.startActivity(shareIntent);
    }


    private String getReport() {
        String englishW = words.getEnword();
        String persionW = words.getFaword();
        String report = englishW + persionW;
        return report;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listWords = mArrayList;
                } else {
                    ArrayList<Words> filteredList = new ArrayList<>();
                    for (Words words : mArrayList) {
                        if (words.getEnword().toLowerCase().contains(charString)) {
                            filteredList.add(words);
                        }
                    }
                    listWords = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listWords;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listWords = (ArrayList<Words>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return listWords.size();
    }

    public void filterList(ArrayList<Words> filterdNames) {
        this.listWords = filterdNames;
        this.mArrayList = filterdNames;
        notifyDataSetChanged();
    }


    private void editTaskDialog(final Words words) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_word, null);
        final EditText EnField = subView.findViewById(R.id.addEnword);
        final EditText FaField = subView.findViewById(R.id.addFaword);
        if (words != null) {
            EnField.setText(words.getEnword());
            FaField.setText(words.getFaword());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Word");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("EDIT WORD", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String enword = EnField.getText().toString();
                final String faword = FaField.getText().toString();
                if (TextUtils.isEmpty(enword) || TextUtils.isEmpty(faword)) {
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    Words tempItem = new Words(enword, faword);

                    if (MainActivity.lang == false)
                        databaseAccess.updateEnglish(tempItem);
                    else
                        databaseAccess.updatePersian(tempItem);


                    ((Activity) context).finish();
                    context.startActivity(((Activity)
                            context).getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }


}
