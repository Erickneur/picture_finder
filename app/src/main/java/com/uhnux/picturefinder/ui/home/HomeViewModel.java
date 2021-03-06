package com.uhnux.picturefinder.ui.home;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.uhnux.picturefinder.MainActivity;
import com.uhnux.picturefinder.data.api.UnsplashClient;
import com.uhnux.picturefinder.data.api.UnsplashInterface;
import com.uhnux.picturefinder.data.models.Photo;
import com.uhnux.picturefinder.data.models.SearchResults;
import com.uhnux.picturefinder.util.GeneralControl;

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
        GeneralControl.getInstance().showProgress(true);
        loading = true;
        dataService.getPhotos(page,null,"latest")
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                        photos.setValue(response.body());
                        Log.d("Photos", "Photos Fetched " + photos.getValue().size());
                        //add to adapter
                        page = 2;
                        loading = false;
                        GeneralControl.getInstance().showProgress(false);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Photo>> call, Throwable t) {
                        loading = false;
                        GeneralControl.getInstance().showProgress(false);
                    }
                });
        return photos;
    }

    public LiveData<List<Photo>> loadPhotos(){
        GeneralControl.getInstance().showProgress(true);
        loading = true;
        dataService.getPhotos(page,null,"latest")
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) { ;
                        photos.setValue(response.body());
                        Log.d("Photos", "Photos Fetched " + photos.getValue().size());
                        page++;
                        loading = false;
                        GeneralControl.getInstance().showProgress(false);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Photo>> call, Throwable t) {
                        loading = false;
                        GeneralControl.getInstance().showProgress(false);
                    }
                });
        return photos;
    }

    public LiveData<List<Photo>> search(String query){
        loading = true;
        page = 1;
        if(query != null && !query.equals("")) {
            GeneralControl.getInstance().showProgress(true);
            dataService.searchPhotos(query,null,null,null)
                    .enqueue(new Callback<SearchResults>() {
                        @Override
                        public void onResponse(retrofit2.Call<SearchResults> call, retrofit2.Response<SearchResults> response) {
                            SearchResults results = response.body();
                            Log.d("Photos", "Total Results Found " + results.getTotal());
                            photos.setValue(results.getResults());
                            loading = false;
                            GeneralControl.getInstance().showProgress(false);
                        }

                        @Override
                        public void onFailure(retrofit2.Call<SearchResults> call, Throwable t) {
                            Log.d("Unsplash", t.getLocalizedMessage());
                            loading = false;
                            GeneralControl.getInstance().showProgress(false);
                        }
                    });
        }
        else {
            photos.setValue(loadPhotos().getValue());
            loading = false;
        }
        return  photos;
    }
}