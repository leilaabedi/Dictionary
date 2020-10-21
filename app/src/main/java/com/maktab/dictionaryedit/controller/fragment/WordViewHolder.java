package com.maktab.dictionaryedit.controller.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.maktab.dictionaryedit.R;


class WordViewHolder extends RecyclerView.ViewHolder {
    TextView tvEnword, tvFaword;
    ImageView editWord,deleteWord,share_btn;


    WordViewHolder(View itemView) {
        super(itemView);
        tvEnword = itemView.findViewById(R.id.Enword);
        tvFaword = itemView.findViewById(R.id.Faword);
        editWord=itemView.findViewById(R.id.editWord);
       deleteWord=itemView.findViewById(R.id.deleteWord);
       share_btn=itemView.findViewById(R.id.share_icon);




    }
}