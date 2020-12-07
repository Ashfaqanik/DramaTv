package com.example.dramatv.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dramatv.network.ApiClient;
import com.example.dramatv.network.ApiService;
import com.example.dramatv.responses.DramaShowsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {

    private ApiService apiService;

    public SearchRepository() {
        apiService= ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<DramaShowsResponse> searchDrama(String query,int page){
        MutableLiveData<DramaShowsResponse> data = new MutableLiveData<>();
        apiService.searchDrama(query,page).enqueue(new Callback<DramaShowsResponse>() {
            @Override
            public void onResponse(@NonNull Call<DramaShowsResponse> call,@NonNull Response<DramaShowsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DramaShowsResponse> call,@NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
