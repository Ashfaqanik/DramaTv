package com.example.dramatv.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.dramatv.R;
import com.example.dramatv.adapters.EpisodeAdapter;
import com.example.dramatv.adapters.ImageSliderAdapter;
import com.example.dramatv.databinding.ActivityDetailsBinding;
import com.example.dramatv.databinding.LayoutEpisodesBottomSheetBinding;
import com.example.dramatv.models.DramaShows;
import com.example.dramatv.responses.DramaShowDetailsResponse;
import com.example.dramatv.responses.DramaShowsResponse;
import com.example.dramatv.viewModels.DetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding mActivityDetailsBinding;
    private DetailsViewModel mDetailsViewModel;
    private BottomSheetDialog episodeBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding mLayoutEpisodesBottomSheetBinding;
    private DramaShows dramaShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityDetailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_details);
        doInitialization();

    }

    private void doInitialization(){
        mDetailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        mActivityDetailsBinding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        dramaShow = (DramaShows) getIntent().getSerializableExtra("dramaShow");
        getDramaShowDetails();
    }

    private void getDramaShowDetails(){
        mActivityDetailsBinding.setIsLoading(true);

        String dramaId = String.valueOf(dramaShow.getId());

        mDetailsViewModel.getDetails(dramaId).observe(this, new Observer<DramaShowDetailsResponse>() {
            @Override
            public void onChanged(DramaShowDetailsResponse dramaShowDetailsResponse) {
                mActivityDetailsBinding.setIsLoading(false);
                if(dramaShowDetailsResponse.getDramaShowDetails()!=null){
                    if(dramaShowDetailsResponse.getDramaShowDetails().getPictures()!=null){
                        loadImageSlider(dramaShowDetailsResponse.getDramaShowDetails().getPictures());
                    }
                    mActivityDetailsBinding.setDramaShowImageUrl(
                            dramaShowDetailsResponse.getDramaShowDetails().getImage_path()
                    );
                    mActivityDetailsBinding.imageDramaShow.setVisibility(View.VISIBLE);
                    mActivityDetailsBinding.setTextDescription(
                            String.valueOf(
                                    HtmlCompat.fromHtml(
                                            dramaShowDetailsResponse.getDramaShowDetails().getDescription(),
                                            HtmlCompat.FROM_HTML_MODE_LEGACY)));
                    mActivityDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                    mActivityDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                    mActivityDetailsBinding.textReadMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mActivityDetailsBinding.textReadMore.getText().equals("Read More")) {
                                mActivityDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                mActivityDetailsBinding.textDescription.setEllipsize(null);
                                mActivityDetailsBinding.textReadMore.setText(R.string.read_less);
                            }else {
                                mActivityDetailsBinding.textDescription.setMaxLines(4);
                                mActivityDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                mActivityDetailsBinding.textReadMore.setText(R.string.read_more);
                            }
                        }
                    });
                    mActivityDetailsBinding.setRating(String.format(Locale.getDefault(),"%.2f",
                            Double.parseDouble(dramaShowDetailsResponse.getDramaShowDetails().
                                    getRating())));
                    if(dramaShowDetailsResponse.getDramaShowDetails().getGenres() !=null){
                        mActivityDetailsBinding.setGenre(dramaShowDetailsResponse.getDramaShowDetails().getGenres()[0]);
                    }else {
                        mActivityDetailsBinding.setGenre("N/A");
                    }
                    mActivityDetailsBinding.setRuntime(dramaShowDetailsResponse.getDramaShowDetails().getRuntime()+" Min");
                    mActivityDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                    mActivityDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                    mActivityDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);
                    mActivityDetailsBinding.buttonWebsite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(dramaShowDetailsResponse.getDramaShowDetails().getUrl()));
                            startActivity(intent);
                        }
                    });
                    mActivityDetailsBinding.buttonEpisodes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(episodeBottomSheetDialog==null){
                                episodeBottomSheetDialog = new BottomSheetDialog(DetailsActivity.this);
                                mLayoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                        LayoutInflater.from(DetailsActivity.this),
                                        R.layout.layout_episodes_bottom_sheet,
                                        findViewById(R.id.episodeContainer),
                                        false
                                );
                                episodeBottomSheetDialog.setContentView(mLayoutEpisodesBottomSheetBinding.getRoot());
                                mLayoutEpisodesBottomSheetBinding.episodeRecyclerView.setAdapter(new EpisodeAdapter(
                                        dramaShowDetailsResponse.getDramaShowDetails().getEpisodes()
                                ));
                                mLayoutEpisodesBottomSheetBinding.textTitle.setText(
                                        String.format("%s", dramaShow.getName())
                                );
                                mLayoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        episodeBottomSheetDialog.dismiss();
                                    }
                                });
                            }
                            FrameLayout frameLayout = episodeBottomSheetDialog.findViewById(
                                    com.google.android.material.R.id.design_bottom_sheet
                            );
                            if(frameLayout!=null){
                                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                            episodeBottomSheetDialog.show();
                        }
                    });
                    mActivityDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                    mActivityDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);
                    loadDramaShowDetails();
                }
            }
        });
    }

    private void loadImageSlider(String[] sliderImage){
        mActivityDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        mActivityDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImage));
        mActivityDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        mActivityDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setUpSliderIndicators(sliderImage.length);

    }

    private void setUpSliderIndicators(int count){
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for(int i =0;i<indicators.length;i++){
            indicators[i]=new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),R.drawable.background_slider_indicators_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            mActivityDetailsBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        mActivityDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
        mActivityDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });

    }

    private void setCurrentSliderIndicator(int position){
        int childCount = mActivityDetailsBinding.layoutSliderIndicators.getChildCount();
        for(int i =0;i<childCount;i++){
            ImageView imageView = (ImageView) mActivityDetailsBinding.layoutSliderIndicators.getChildAt(i);
            if(i==position){
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.background_slider_indicator));

            }else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),R.drawable.background_slider_indicators_inactive));
            }
        }
    }

    private void loadDramaShowDetails(){
        mActivityDetailsBinding.setTextName(dramaShow.getName());
        mActivityDetailsBinding.setTextNetwork(dramaShow.getNetwork()+"("
        + dramaShow.getCountry()+ ")");
        mActivityDetailsBinding.setTextStatus(dramaShow.getStatus());
        mActivityDetailsBinding.setTextStarted(dramaShow.getStart_date());
        mActivityDetailsBinding.textName.setVisibility(View.VISIBLE);
        mActivityDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        mActivityDetailsBinding.textStatus.setVisibility(View.VISIBLE);
        mActivityDetailsBinding.textStarted.setVisibility(View.VISIBLE);
    }
}