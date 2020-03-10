package com.uhnux.picturefinder.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("raw")
    @Expose
    private String raw;
    @SerializedName("full")
    @Expose
    private String full;
    @SerializedName("regular")
    @Expose
    private String regular;
    @SerializedName("small")
    @Expose
    private String small;
    @SerializedName("thumb")
    @Expose
    private String thumb;
}
