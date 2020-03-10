package com.uhnux.picturefinder.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uhnux.picturefinder.MainActivity;
import com.uhnux.picturefinder.R;
import com.uhnux.picturefinder.data.api.UnsplashClient;
import com.uhnux.picturefinder.data.models.Photo;
import com.uhnux.picturefinder.data.models.SearchResults;
import com.uhnux.picturefinder.data.api.UnsplashInterface;
import com.uhnux.picturefinder.ui.artists.ArtistsFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

public class HomeFragment extends Fragment {

    //private HomeViewModel homeViewModel;
    private SearchView searchView;
    private RecyclerView rvPhotos;
    private SwipeRefreshLayout refreshGesture;

    private int page = 1;
    private PhotoItemAdapter adapter;
    private UnsplashInterface dataService;
    private boolean loading;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.fh_tv_home);
        searchView = root.findViewById(R.id.fh_sb_finder);
        rvPhotos = root.findViewById(R.id.fh_rv_photos);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        rvPhotos.setLayoutManager(layoutManager);
        adapter = new PhotoItemAdapter(new ArrayList<Photo>(), getContext());
        rvPhotos.setAdapter(adapter);
        // SwipeRefreshLayout
        refreshGesture = root.findViewById(R.id.fh_srl_refresh);
        refreshGesture.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshGesture.setRefreshing(false);
                if(searchView.getQuery() != null && !searchView.getQuery().toString().equals("")){
                    search(searchView.getQuery().toString());
                }else{
                    ((MainActivity) getActivity()).pushView(new ArtistsFragment());
                    //resetPhotos();
                }
            }
        });
        refreshGesture.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        rvPhotos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = 0;
                int totalItemCount = layoutManager.getItemCount();
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                //Toast.makeText(getContext(), "Item visible: " + lastVisibleItemPosition, Toast.LENGTH_LONG).show();
                if(totalItemCount > 9 && lastVisibleItemPosition == totalItemCount - 1 && !loading){
                    if(searchView.getQuery() != null && !searchView.getQuery().toString().equals("")){
                        search(searchView.getQuery().toString());
                    }else{
                        loadPhotos();
                        //layoutManager.scrollToPosition(lastVisibleItemPosition);
                    }
                }
            }
        });

//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        dataService = UnsplashClient.getUnsplashClient().create(UnsplashInterface.class);

        resetPhotos();
        return root;
    }

    private void resetPhotos() {
        ((MainActivity) getActivity()).showProgress(true);
        dataService.getPhotos(page,null,"latest")
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                        List<Photo> photos = response.body();
                        Log.d("Photos", "Photos Fetched " + photos.size());
                        //add to adapter
                        page = 1;
                        adapter = new PhotoItemAdapter(photos, getActivity());
                        rvPhotos.setAdapter(adapter);
                        ((MainActivity) getActivity()).showProgress(false);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Photo>> call, Throwable t) {
                        ((MainActivity) getActivity()).showProgress(false);
                    }
                });
    }

    private void loadPhotos() {
        loading = true;
        ((MainActivity) getActivity()).showProgress(true);
        dataService.getPhotos(page,null,"latest")
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                        List<Photo> photos = response.body();
                        Log.d("Photos", "Photos Fetched " + photos.size());
                        //add to adapter
                        page++;
                        adapter.addPhotos(photos);
                        //rvPhotos.setAdapter(adapter);
                        loading = false;
                        ((MainActivity) getActivity()).showProgress(false);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Photo>> call, Throwable t) {
                        loading = false;
                        ((MainActivity) getActivity()).showProgress(false);
                    }
                });
    }

    public void search(String query){
        if(query != null && !query.equals("")) {
            ((MainActivity) getActivity()).showProgress(true);
            dataService.searchPhotos(query,null,null,null)
                    .enqueue(new Callback<SearchResults>() {
                        @Override
                        public void onResponse(retrofit2.Call<SearchResults> call, retrofit2.Response<SearchResults> response) {
                            SearchResults results = response.body();
                            Log.d("Photos", "Total Results Found " + results.getTotal());
                            List<Photo> photos = results.getResults();
                            adapter = new PhotoItemAdapter(photos, getActivity());
                            rvPhotos.setAdapter(adapter);
                            ((MainActivity) getActivity()).showProgress(false);
                        }

                        @Override
                        public void onFailure(retrofit2.Call<SearchResults> call, Throwable t) {
                            Log.d("Unsplash", t.getLocalizedMessage());
                            ((MainActivity) getActivity()).showProgress(false);
                        }
                    });
        }
        else {
            loadPhotos();
        }
    }
    /*
    private void search(String query){
        Toast.makeText(getContext(), "Search: " + query, Toast.LENGTH_LONG).show();
    }
    */
}