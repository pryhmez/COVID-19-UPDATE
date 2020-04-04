package com.example.covid_19_update;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionHolder>{

    Context context;
    List<Question> list = new ArrayList<>();

    public QuestionsAdapter(Context context, List<Question> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diagnose_question, parent,false);

        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(final QuestionHolder holder, final int position) {

        final Question fruits = list.get(position);

        holder.tv_question.setText(fruits.getQuestion());

        holder.checkBox.setChecked(fruits.isSelected());
        holder.checkBox.setTag(list.get(position));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Question fruits1 = (Question) holder.checkBox.getTag();

                fruits1.setSelected(holder.checkBox.isChecked());

                list.get(position).setSelected(holder.checkBox.isChecked());

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getAverage() {
        int sum = 0;
        float avg;
        int data = 0;
        for (int j=0; j<list.size();j++){

            if (list.get(j).isSelected() == true){
                data = data + Integer.parseInt(list.get(j).getPoint());
            }
        }
        for (int i = 0; i < list.size(); i++) {
            sum = sum + Integer.parseInt(list.get(i).getPoint());
        }

        avg =(float) data/sum*100;
//        Toast.makeText(context, "Selected Fruits : \n " + data/sum + "/" + list.size() + "=" + avg, Toast.LENGTH_SHORT).show();

        return (int) avg;
    }

    public static class QuestionHolder extends RecyclerView.ViewHolder{

        TextView tv_question,tv_price;
        CheckBox checkBox;

        public QuestionHolder(View itemView) {
            super(itemView);

            tv_question = itemView.findViewById(R.id.question);
            checkBox = itemView.findViewById(R.id.checkBox_select);
        }
    }

    public List<Question> getFruitsList(){
        return list;
    }
}
