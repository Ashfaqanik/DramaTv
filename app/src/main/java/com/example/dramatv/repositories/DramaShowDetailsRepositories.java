package com.example.dramatv.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dramatv.network.ApiClient;
import com.example.dramatv.network.ApiService;
import com.example.dramatv.responses.DramaShowDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DramaShowDetailsRepositories {
    private ApiService apiService;

    public DramaShowDetailsRepositories() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<DramaShowDetailsResponse> getDetails(String dramaId){
        MutableLiveData<DramaShowDetailsResponse> data = new MutableLiveData<>();

        apiService.getDetails(dramaId).enqueue(new Callback<DramaShowDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<DramaShowDetailsResponse> call,@NonNull Response<DramaShowDetailsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DramaShowDetailsResponse> call,@NonNull Throwable t) {
                data.setValue(null);
            }

        });
        return data;
    }
}
