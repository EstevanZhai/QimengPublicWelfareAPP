package com.zxy.and2;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.and2.placeholder.VolPlaceholderContent.VolunteeringItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link VolunteeringItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyVolunteeringItemRecyclerViewAdapter extends RecyclerView.Adapter<MyVolunteeringItemRecyclerViewAdapter.ViewpnHolder> implements View.OnClickListener {

    private final List<VolunteeringItem> mVolunteeringList;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private RecyclerView recyclerView;

    public MyVolunteeringItemRecyclerViewAdapter(List<VolunteeringItem> items) {
        mVolunteeringList = items;
    }

    @Override
    public ViewpnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerView=(RecyclerView) parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_volunteering_item,parent,false);
        view.setOnClickListener(this);
        return new ViewpnHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewpnHolder holder, int position) {
//        holder.itemView.setTag(position);
//        holder.onBind(position);
        holder.mItem = mVolunteeringList.get(position);

//        if (onRecyclerViewItemClickListener != null) {
//            holder.itemView.setOnClickListener(onRecyclerViewItemClickListener);
//        }
        holder.mImageView.setImageDrawable(mVolunteeringList.get(position).image);
        holder.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.mTitleView.setText(mVolunteeringList.get(position).title);
        holder.mContentView.setText(mVolunteeringList.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mVolunteeringList.size();
    }

    public class ViewpnHolder extends RecyclerView.ViewHolder {
        int mId;
        ImageView mImageView;
        TextView mTitleView;
        TextView mContentView;
        public VolunteeringItem mItem;

        public ViewpnHolder(View view) {
            super(view);
            mId = view.getId();
            mImageView = view.findViewById(R.id.vol_image);
            mTitleView = view.findViewById(R.id.vol_title);
            mContentView = view.findViewById(R.id.vol_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

    @Override
    public void onClick(View view) {
        int position=recyclerView.getChildAdapterPosition(view);
        if(onRecyclerViewItemClickListener!=null)
            onRecyclerViewItemClickListener.onItemClick(recyclerView,view,position);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener){
        this.onRecyclerViewItemClickListener=onRecyclerViewItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(RecyclerView recyclerView,View view,int position);
    }
}