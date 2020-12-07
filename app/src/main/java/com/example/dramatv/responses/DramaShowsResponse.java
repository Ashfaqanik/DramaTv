package com.example.dramatv.responses;

import com.example.dramatv.models.DramaShows;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DramaShowsResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int pages;

    @SerializedName("tv_shows")
    private List<DramaShows> dramaShows;

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public List<DramaShows> getDramaShows() {
        return dramaShows;
    }
}
