package com.example.dramatv.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.dramatv.repositories.DramaShowDetailsRepositories;
import com.example.dramatv.responses.DramaShowDetailsResponse;
import com.example.dramatv.responses.DramaShowsResponse;

public class DetailsViewModel extends ViewModel {
    private DramaShowDetailsRepositories dramaShowDetailsRepositories;

    public DetailsViewModel(){
        dramaShowDetailsRepositories = new DramaShowDetailsRepositories();
    }

    public LiveData<DramaShowDetailsResponse> getDetails(String dramaId){
        return dramaShowDetailsRepositories.getDetails(dramaId);
    }

}
