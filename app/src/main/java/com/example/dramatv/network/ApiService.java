package com.example.dramatv.network;

import com.example.dramatv.responses.DramaShowDetailsResponse;
import com.example.dramatv.responses.DramaShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<DramaShowsResponse> getPopular (
            @Query("page") int page
    );
//show-details?q=29560
    @GET("show-details")
    Call<DramaShowDetailsResponse> getDetails (@Query("q") String dramaId);

    //search?q=arrow&page=1
    @GET("search")
    Call<DramaShowsResponse> searchDrama (
            @Query("q")String query,@Query("page") int page
    );
}
