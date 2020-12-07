package com.example.dramatv.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dramatv.R;
import com.example.dramatv.databinding.ItemContainerEpisodeBinding;
import com.example.dramatv.models.Episode;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>{
    private List<Episode> mEpisodes;
    private LayoutInflater layoutInflater;

    public EpisodeAdapter(List<Episode> episodes) {
        mEpisodes = episodes;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater==null){
            layoutInflater=LayoutInflater.from(parent.getContext());
        }
        ItemContainerEpisodeBinding episodeBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_container_episode,parent,false);
        return new EpisodeViewHolder(episodeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        holder.bindEpisode(mEpisodes.get(position));
    }

    @Override
    public int getItemCount() {
        return mEpisodes.size();
    }

    static class EpisodeViewHolder extends RecyclerView.ViewHolder{

        private ItemContainerEpisodeBinding mItemContainerEpisodeBinding;

        public EpisodeViewHolder(ItemContainerEpisodeBinding itemContainerEpisodeBinding) {
            super(itemContainerEpisodeBinding.getRoot());
            this.mItemContainerEpisodeBinding=itemContainerEpisodeBinding;
        }
        public void bindEpisode(Episode episode){
            String title = "S";
            String season = episode.getSeason();
            if(season.length()==1){
                season="0".concat(season);
            }
            String episodeNumber = episode.getEpisode();
            if(episodeNumber.length()==1){
                episodeNumber="0".concat(episodeNumber);
            }
            episodeNumber="E".concat(episodeNumber);
            title = title.concat(season).concat(episodeNumber);
            mItemContainerEpisodeBinding.setTitle(title);
            mItemContainerEpisodeBinding.setName(episode.getName());
            mItemContainerEpisodeBinding.setAirDate(episode.getAir_date());
        }
    }
}
