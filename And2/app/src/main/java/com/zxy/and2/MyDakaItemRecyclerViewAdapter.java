package com.zxy.and2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zxy.and2.placeholder.DakaPlaceholderContent.DakaItem;

import java.io.File;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DakaItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDakaItemRecyclerViewAdapter extends RecyclerView.Adapter<MyDakaItemRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private final List<DakaItem> mDakaList;
    private MyDakaItemRecyclerViewAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private RecyclerView recyclerView;

    public MyDakaItemRecyclerViewAdapter(List<DakaItem> items) {
        mDakaList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        recyclerView = (RecyclerView) parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_daka_item, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
//    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_companion_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mDakaList.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(new File(mDakaList.get(position).cachePath).getAbsolutePath());
        holder.mImageView.setImageBitmap(bitmap);
        holder.mTextView.setText(mDakaList.get(position).content.length()>25?mDakaList.get(position).content.substring(0,25)+"...":mDakaList.get(position).content);
        holder.mDateView.setText(mDakaList.get(position).date);
    }

    @Override
    public int getItemCount() {
        return mDakaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;
        public final TextView mTextView;
        public final TextView mDateView;
        public DakaItem mItem;

        public ViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.daka_item_image);
            mTextView = view.findViewById(R.id.daka_item_content);
            mDateView=view.findViewById(R.id.daka_item_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText() + "'";
        }
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        if (onRecyclerViewItemClickListener != null)
            onRecyclerViewItemClickListener.onItemClick(recyclerView, view, position);
    }

    public void setOnRecyclerViewItemClickListener(MyDakaItemRecyclerViewAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(RecyclerView recyclerView, View view, int position);
    }
}