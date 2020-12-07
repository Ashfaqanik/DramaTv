package com.example.dramatv.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import com.example.dramatv.R;
import com.example.dramatv.adapters.DramaShowsAdapter;
import com.example.dramatv.databinding.ActivityMainBinding;
import com.example.dramatv.listeners.DramaShowsListener;
import com.example.dramatv.models.DramaShows;
import com.example.dramatv.responses.DramaShowsResponse;
import com.example.dramatv.viewModels.PopularViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DramaShowsListener {
    private PopularViewModel viewModel;
    private ActivityMainBinding mActivityMainBinding;
    private List<DramaShows> mDramaShows= new ArrayList<>();
    private DramaShowsAdapter mDramaShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        doInitialization();

    }
    private void doInitialization(){
        mActivityMainBinding.recyclerview.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(PopularViewModel.class);
        mDramaShowsAdapter = new DramaShowsAdapter(mDramaShows,this);
        mActivityMainBinding.recyclerview.setAdapter(mDramaShowsAdapter);
        mActivityMainBinding.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!mActivityMainBinding.recyclerview.canScrollVertically(1)){
                    if(currentPage <= totalAvailablePage){
                        currentPage += 1;
                        getPopularShows();
                    }
                }
            }
        });
        mActivityMainBinding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SearchActivity.class));
            }
        });
        getPopularShows();
    }

    private void getPopularShows(){
        toggleLoading();
        viewModel.getPopularShows(currentPage).observe(this, new Observer<DramaShowsResponse>() {
            @Override
            public void onChanged(DramaShowsResponse dramaShowsResponse) {
                toggleLoading();
                if(dramaShowsResponse!=null) {
                    totalAvailablePage = dramaShowsResponse.getPages();

                    if (dramaShowsResponse.getDramaShows() != null) {
                        int oldCount = mDramaShows.size();
                        mDramaShows.addAll(dramaShowsResponse.getDramaShows());
                        mDramaShowsAdapter.notifyItemRangeInserted(oldCount, mDramaShows.size());
                    }
                }

            }
        });
    }
    private void toggleLoading() {
        if (currentPage == 1) {
            if (mActivityMainBinding.getIsLoading() != null && mActivityMainBinding.getIsLoading()) {
                    mActivityMainBinding.setIsLoading(false);
                } else {
                    mActivityMainBinding.setIsLoading(true);
                }
            }
        else {
            if (mActivityMainBinding.getIsLoadingMore() != null && mActivityMainBinding.getIsLoadingMore()) {
                mActivityMainBinding.setIsLoadingMore(false);
            } else {
                mActivityMainBinding.setIsLoadingMore(true);
            }
        }

    }

    @Override
    public void onDramaShowClicked(DramaShows dramaShows) {
        Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
        intent.putExtra("dramaShow", dramaShows);
        startActivity(intent);
    }
}