package com.example.dramatv.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.dramatv.repositories.PopularRepositories;
import com.example.dramatv.responses.DramaShowsResponse;

public class PopularViewModel extends ViewModel {

    private PopularRepositories mPopularRepositories;

    public PopularViewModel(){
        mPopularRepositories = new PopularRepositories();
    }

    public LiveData<DramaShowsResponse> getPopularShows(int page){
        return mPopularRepositories.gerPopularShows(page);
    }

}
