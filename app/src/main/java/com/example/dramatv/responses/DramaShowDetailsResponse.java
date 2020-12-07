package com.example.dramatv.responses;

import com.example.dramatv.models.DramaShowDetails;
import com.google.gson.annotations.SerializedName;

public class DramaShowDetailsResponse {

    @SerializedName("tvShow")
    private DramaShowDetails dramaShowDetails;

    public DramaShowDetails getDramaShowDetails() {
        return dramaShowDetails;
    }
}
