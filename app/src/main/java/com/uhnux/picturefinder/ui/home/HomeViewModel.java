package com.uhnux.picturefinder.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.uhnux.picturefinder.MainActivity;
import com.uhnux.picturefinder.data.api.UnsplashClient;
import com.uhnux.picturefinder.data.api.UnsplashInterface;
import com.uhnux.picturefinder.data.models.Photo;

import java.security.Policy;
import java.util.List;

import retrofit2.Callback;

public class HomeViewModel extends ViewModel {

    private int page = 1;
    private MutableLiveData<String> mText;
    private MutableLiveData<List<Photo>> photos;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        photos = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Photo>> getPhotos(){
        UnsplashInterface dataService = UnsplashClient.getUnsplashClient().create(UnsplashInterface.class);
        dataService.getPhotos(page,null,"latest")
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                        List<Photo> photos = response.body();
                        Log.d("Photos", "Photos Fetched " + photos.size());
                        //add to adapter
                        page = 1;
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Photo>> call, Throwable t) {
                    }
                });
        return photos;
    }
}