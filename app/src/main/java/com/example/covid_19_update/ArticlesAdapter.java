package com.example.covid_19_update;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>{

    private List<Article> articleList;
    private Context context;

    public  ArticlesAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @Override
    public ArticlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_item_row, parent, false);

        return new ArticlesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ArticlesViewHolder holder, final int position) {
        final Article article = articleList.get(position);
        holder.title.setText(article.getTitle());
        holder.desc.setText(article.getDescription());

        Glide.with(context)
                .load(article.getUrlToImage())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    //view holder for the articles
    class ArticlesViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc;
        ImageView imageView;

        ArticlesViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            imageView = itemView.findViewById(R.id.thumbnail);
        }
    }
}
