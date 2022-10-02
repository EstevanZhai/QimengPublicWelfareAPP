package com.zxy.and2;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.and2.placeholder.CpnPlaceholderContent.CompanionItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CompanionItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCompanionItemRecyclerViewAdapter extends RecyclerView.Adapter<MyCompanionItemRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private final List<CompanionItem> mConpanionList;
    private MyCompanionItemRecyclerViewAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private RecyclerView recyclerView;

    public MyCompanionItemRecyclerViewAdapter(List<CompanionItem> items) {
        mConpanionList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        recyclerView = (RecyclerView) parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_companion_item, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
//    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_companion_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mConpanionList.get(position);
        holder.mImageView.setImageResource(mConpanionList.get(position).imageId);
        holder.mTextView.setText(mConpanionList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mConpanionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;
        public final TextView mTextView;
        public CompanionItem mItem;

        public ViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.cpn_person_image);
            mTextView = view.findViewById(R.id.cpn_name);
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

    public void setOnRecyclerViewItemClickListener(MyCompanionItemRecyclerViewAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(RecyclerView recyclerView, View view, int position);
    }
}