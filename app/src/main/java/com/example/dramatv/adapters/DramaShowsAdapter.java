package com.example.dramatv.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dramatv.R;
import com.example.dramatv.databinding.ItemContainerTvShowBinding;
import com.example.dramatv.listeners.DramaShowsListener;
import com.example.dramatv.models.DramaShows;

import java.util.List;

public class DramaShowsAdapter extends RecyclerView.Adapter<DramaShowsAdapter.DramaShowViewHolder>{
    private List<DramaShows> mDramaShows;
    private LayoutInflater layoutInflater;
    private DramaShowsListener mDramaShowsListener;

    public DramaShowsAdapter(List<DramaShows> dramaShows,DramaShowsListener dramaShowsListener) {
        mDramaShows = dramaShows;
        mDramaShowsListener = dramaShowsListener;
    }

    @NonNull
    @Override
    public DramaShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater==null){
            layoutInflater=LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding showBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_container_tv_show,parent,false);
        return new DramaShowViewHolder(showBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DramaShowViewHolder holder, int position) {
        holder.bindDramaShow(mDramaShows.get(position));
    }

    @Override
    public int getItemCount() {
        return mDramaShows.size();
    }

    class DramaShowViewHolder extends RecyclerView.ViewHolder{
        private ItemContainerTvShowBinding mItemContainerTvShowBinding;


        public DramaShowViewHolder(ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            this.mItemContainerTvShowBinding = itemContainerTvShowBinding;
        }

        public void bindDramaShow(DramaShows dramaShows){
            mItemContainerTvShowBinding.setDramaShow(dramaShows);
            mItemContainerTvShowBinding.executePendingBindings();
            mItemContainerTvShowBinding.getRoot().setOnClickListener(view -> mDramaShowsListener.onDramaShowClicked(dramaShows));
        }
    }


}
