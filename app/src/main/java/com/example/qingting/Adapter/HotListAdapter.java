package com.example.qingting.Adapter;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qingting.R;

import java.util.List;

public class HotListAdapter extends RecyclerView.Adapter<HotListAdapter.HotItemViewHolder> {

    List<String> contentList;
    public HotListAdapter(List<String> contentList) {
        this.contentList = contentList;
    }

    @NonNull
    @Override
    public HotItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_item_view, parent, false);
        return new HotItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HotItemViewHolder holder, int position) {
        holder.setRank(position + 1);
        holder.setContent(contentList.get(position));
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public static class HotItemViewHolder extends RecyclerView.ViewHolder {
        TextView rank;
        TextView content;

        public HotItemViewHolder(View view) {
            super(view);
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
}
