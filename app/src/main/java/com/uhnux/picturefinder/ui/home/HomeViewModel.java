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

    private UnsplashInterface dataService;
    private int page;
    private Boolean loading;
    private MutableLiveData<String> mText;
    private MutableLiveData<List<Photo>> photos;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        photos = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        dataService = UnsplashClient.getUnsplashClient().create(UnsplashInterface.class);
        loading = false;
        page = 1;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Photo>> resetPhotos(){
        dataService.getPhotos(page,null,"latest")
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                        photos.setValue(response.body());
                        Log.d("Photos", "Photos Fetched " + photos.getValue().size());
                        //add to adapter
                        page = 1;
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Photo>> call, Throwable t) {
                    }
                });
        return photos;
    }

    public LiveData<List<Photo>> loadPhotos(){
        loading = true;
        dataService.getPhotos(page,null,"latest")
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                        List<Photo> newPhotos = response.body();
                        photos.getValue().addAll(newPhotos);
                        Log.d("Photos", "Photos Fetched " + photos.getValue().size());
                        page++;
                        loading = false;
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Photo>> call, Throwable t) {
                        loading = false;
                    }
                });
        return photos;
    }
}