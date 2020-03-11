package com.uhnux.picturefinder.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.uhnux.picturefinder.R;
import com.uhnux.picturefinder.data.models.Photo;

import java.util.List;

public class PhotoItemAdapter extends RecyclerView.Adapter<PhotoItemAdapter.PhotoItemHolder> {

    private final List<Photo> photoList;
    private Context context;

    public PhotoItemAdapter(List<Photo> photos, Context context) {
        photoList = photos;
        this.context = context;
    }

    public class PhotoItemHolder extends RecyclerView.ViewHolder {
        public ImageView ivPhoto, ivUser;
        public TextView tvDescription, tvLikes;
        public PhotoItemHolder(View view) {
            super(view);
            ivUser = view.findViewById(R.id.ii_iv_user);
            ivPhoto = view.findViewById(R.id.ii_siv_image);
            tvDescription = view.findViewById(R.id.ii_tv_description);
            tvLikes = view.findViewById(R.id.ii_tv_likes);
        }
    }

    @NonNull
    @Override
    public PhotoItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image, viewGroup, false);
        itemView.setEnabled(false);
        return new PhotoItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoItemHolder holder, final int position) {
        Photo photo = photoList.get(position);

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, photo.getId(), Toast.LENGTH_LONG).show();
            }
        });

        holder.tvLikes.setText("Likes: " + photo.getLikes());
        holder.tvDescription.setText(photo.getDescription());
        Picasso.get()
                .load(photo.getUrls().getRegular())
                .resize(256, 256)
                .centerCrop()
                .into(holder.ivPhoto);
        Picasso.get()
                .load(photo.getUser().getUrls().getSmall())
                .resize(64, 64)
                .centerCrop()
                .into(holder.ivUser);
    }

    public void addPhotos(List<Photo> photos){
        int lastCount = getItemCount();
        photoList.addAll(0 ,photos);
        //photoList.addAll(lastCount, photos);
        notifyItemRangeInserted(lastCount, photos.size());
        //notifyItemRangeInserted(0, lastCount);
        Toast.makeText(context, R.string.general_loading_page, Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        if(photoList != null)
            return photoList.size();
        else{
            return 0;
        }
    }
}