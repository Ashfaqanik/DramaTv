package com.example.dramatv.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.dramatv.repositories.SearchRepository;
import com.example.dramatv.responses.DramaShowsResponse;

public class SearchViewModel extends ViewModel {
    private SearchRepository mSearchRepository;

    public SearchViewModel() {
        mSearchRepository = new SearchRepository();
    }

    public LiveData<DramaShowsResponse> searchDrama(String query,int page){
        return mSearchRepository.searchDrama(query,page);
    }
}
