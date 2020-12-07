package com.example.dramatv.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.dramatv.R;
import com.example.dramatv.adapters.DramaShowsAdapter;
import com.example.dramatv.databinding.ActivitySearchBinding;
import com.example.dramatv.listeners.DramaShowsListener;
import com.example.dramatv.models.DramaShows;
import com.example.dramatv.responses.DramaShowsResponse;
import com.example.dramatv.viewModels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements DramaShowsListener {

    private ActivitySearchBinding mActivitySearchActivityBinding;
    private DramaShowsAdapter dramaShowsAdapter;
    private SearchViewModel mSearchViewModel;
    private List<DramaShows> mDramaShows= new ArrayList<>();
    private int currentPage = 1;
    private int totalAvailablePage = 1;
    private Timer mTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySearchActivityBinding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        doInitialization();
    }
    private void doInitialization(){
        mActivitySearchActivityBinding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mActivitySearchActivityBinding.Srecyclerview.setHasFixedSize(true);
        mSearchViewModel=new ViewModelProvider(this).get(SearchViewModel.class);
        dramaShowsAdapter = new DramaShowsAdapter(mDramaShows,this);
        mActivitySearchActivityBinding.Srecyclerview.setAdapter(dramaShowsAdapter);
        mActivitySearchActivityBinding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mTimer!=null){
                    mTimer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().trim().isEmpty()){
                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    currentPage=1;
                                    totalAvailablePage=1;
                                    searchDramaShow(editable.toString());
                                }
                            });
                        }
                    },500);
                }else {
                    mDramaShows.clear();
                    dramaShowsAdapter.notifyDataSetChanged();
                }
            }
        });
        mActivitySearchActivityBinding.Srecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!mActivitySearchActivityBinding.Srecyclerview.canScrollVertically(1)){
                    if(!mActivitySearchActivityBinding.inputSearch.getText().toString().isEmpty()){
                        if(currentPage < totalAvailablePage){
                            currentPage += 1;
                            searchDramaShow(mActivitySearchActivityBinding.inputSearch.getText().toString());
                        }
                    }
                }
            }
        });
        mActivitySearchActivityBinding.inputSearch.requestFocus();
    }
    private void searchDramaShow(String query){
        toggleLoading();
        mSearchViewModel.searchDrama(query,currentPage).observe(this, new Observer<DramaShowsResponse>() {
            @Override
            public void onChanged(DramaShowsResponse dramaShowsResponse) {
                toggleLoading();
                if(dramaShowsResponse!=null) {
                    totalAvailablePage = dramaShowsResponse.getPages();

                    if (dramaShowsResponse.getDramaShows() != null) {
                        int oldCount = mDramaShows.size();
                        mDramaShows.addAll(dramaShowsResponse.getDramaShows());
                        dramaShowsAdapter.notifyItemRangeInserted(oldCount, mDramaShows.size());
                    }
                }

            }
        });
    }
    private void toggleLoading() {
        if (currentPage == 1) {
            if (mActivitySearchActivityBinding.getIsLoading() != null && mActivitySearchActivityBinding.getIsLoading()) {
                mActivitySearchActivityBinding.setIsLoading(false);
            } else {
                mActivitySearchActivityBinding.setIsLoading(true);
            }
        }
        else {
            if (mActivitySearchActivityBinding.getIsLoadingMore() != null && mActivitySearchActivityBinding.getIsLoadingMore()) {
                mActivitySearchActivityBinding.setIsLoadingMore(false);
            } else {
                mActivitySearchActivityBinding.setIsLoadingMore(true);
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