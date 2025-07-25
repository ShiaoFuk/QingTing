package com.example.qingting.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qingting.R;

import java.util.List;

public class HotListAdapter extends RecyclerView.Adapter<HotItemViewHolder> {

    List<String> contentList;
    EditText search;
    public HotListAdapter(EditText search, List<String> contentList) {
        this.contentList = contentList;
        this.search = search;
    }

    @NonNull
    @Override
    public HotItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_item_view, parent, false);
        return new HotItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HotItemViewHolder holder, int position) {
        final String currentHotItem = contentList.get(position);
        holder.setRank(position + 1);
        holder.setContent(contentList.get(position));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击的时候触发搜索
                search.setText(currentHotItem);
                search.onEditorAction(EditorInfo.IME_ACTION_SEARCH);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }


}


class HotItemViewHolder extends RecyclerView.ViewHolder {
    TextView rank;
    TextView content;
    View view;

    public HotItemViewHolder(View view) {
        super(view);
        this.view = view;
        rank = view.findViewById(R.id.rank);
        content = view.findViewById(R.id.content);
    }

    public int getRank() {
        return Integer.valueOf(rank.getText().toString());
    }

    public String getContent() {
        return content.getText().toString();
    }

    public void setRank(int rankNumber) {
        Spannable spannable = new SpannableString(String.valueOf(rankNumber));
        int end = spannable.length();
        if (rankNumber <= 3) {
            spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        rank.setText(spannable);
    }


    public void setContent(String content) {
        this.content.setText(content);
    }
}