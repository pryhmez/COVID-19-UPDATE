package com.example.covid_19_update;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>{

    private List<Article> articleList;
    private Context context;
//    private View.OnClickListener mOnItemClickListener;
    private RecyclerViewClickListener mListener;

//   public ArticlesAdapter(RecyclerViewClickListener listener) {
//        mListener = listener;
//    }

    public  ArticlesAdapter(List<Article> articleList, Context context, RecyclerViewClickListener listener) {
        this.articleList = articleList;
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public ArticlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_item_row, parent, false);

        return new ArticlesViewHolder(itemView, mListener);
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

//    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
//        mOnItemClickListener = itemClickListener;
//    }
    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }
    //view holder for the articles
    class ArticlesViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView title, desc;
        ImageView imageView;
        CardView cardView;
        private RecyclerViewClickListener mListener;
        ArticlesViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            imageView = itemView.findViewById(R.id.thumbnail);
            cardView = itemView.findViewById(R.id.card_view);


            mListener = listener;
            title.setOnClickListener(this);
//            itemView.setTag(this);
//            itemView.setOnClickListener(mOnItemClickListener);
        }
        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }

    }
}
