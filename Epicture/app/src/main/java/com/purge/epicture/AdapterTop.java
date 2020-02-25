package com.purge.epicture;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterTop extends RecyclerView.Adapter<AdapterTop.MyViewHolder> {

    List<MyObject> list;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void ontLikeClick(int position);
    }



    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public AdapterTop(List<MyObject> list) {
        this.list = list;
    }

    @Override
    public AdapterTop.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_cards, viewGroup, false);
        MyViewHolder evh = new MyViewHolder(view, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        MyObject myObject = list.get(position);
        myViewHolder.bind(myObject);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewView;
        private ImageView imageView;
        Button like = itemView.findViewById(R.id.likeButton);

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewView = itemView.findViewById(R.id.text);
            imageView = itemView.findViewById(R.id.image);

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.ontLikeClick(position);
                        }
                    }
                }
            });
        }

        public void bind(MyObject myObject) {
            textViewView.setText(myObject.text);
            Picasso.get().load(myObject.imageUrl).centerCrop().fit().into(imageView);
        }
    }
}
