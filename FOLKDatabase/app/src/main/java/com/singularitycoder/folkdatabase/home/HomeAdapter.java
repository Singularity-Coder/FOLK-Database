package com.singularitycoder.folkdatabase.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.helper.Helper;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HOME_HEADER = 0;
    private static final int HOME_ITEM = 1;

    ArrayList<HomeItem> homeList;
    Context context;
    OnItemClickListener clickListener;

    public HomeAdapter(ArrayList<HomeItem> homeList, Context context) {
        this.homeList = homeList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case HOME_HEADER:
                v = layoutInflater.inflate(R.layout.item_home_header, parent, false);
                return new HomeHeaderViewHolder(v);
            default:
                v = layoutInflater.inflate(R.layout.item_home, parent, false);
                return new HomeViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        HomeItem homeItem = homeList.get(position);
        if (holder instanceof HomeViewHolder) {
            ((HomeViewHolder) holder).homeImage.setImageResource(homeItem.getIntHomeImage());
            ((HomeViewHolder) holder).homeTitle.setText(homeItem.getStrHomeTitle());
            ((HomeViewHolder) holder).homeCount.setText(homeItem.getStrHomeCount());
        }

        else if (holder instanceof HomeHeaderViewHolder) {
            Helper.glideProfileImage(context, homeItem.getStrImageUrl(), ((HomeHeaderViewHolder) holder).imgUserImage);
            ((HomeHeaderViewHolder) holder).tvUserName.setText(homeItem.getStrUserName());
        }
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HOME_HEADER : HOME_ITEM;
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView homeImage;
        TextView homeTitle;
        TextView homeCount;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            homeImage = itemView.findViewById(R.id.img_dash_stat_icon);
            homeTitle = itemView.findViewById(R.id.tv_dash_stat_title);
            homeCount = itemView.findViewById(R.id.tv_dash_stat_count);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public class HomeHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName;
        CircularImageView imgUserImage;


        public HomeHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            imgUserImage = itemView.findViewById(R.id.img_profile_header);
        }
    }

    // This interface now holds the view and the view info. To get it from this class use a method and call from another class
    interface OnItemClickListener {
       void onItemClick(View view, int position);
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.clickListener = onItemClickListener;
    }

}
