package com.singularitycoder.folkdatabase.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.singularitycoder.folkdatabase.R;

import java.util.ArrayList;

public class ProfileActivitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ProfileContactItem> activitiesList;
    Context context;

    public ProfileActivitiesAdapter(ArrayList<ProfileContactItem> arrayList, Context context) {
        this.activitiesList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_activity, parent, false);
        return new ProfileActivitiesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProfileContactItem profileContactItem = activitiesList.get(position);

        ((ProfileActivitiesViewHolder) holder).profilePic.setImageResource(profileContactItem.getProfileImage());
        ((ProfileActivitiesViewHolder) holder).name.setText(profileContactItem.getName());
        ((ProfileActivitiesViewHolder) holder).dateTime.setText(profileContactItem.getDateTime());
        ((ProfileActivitiesViewHolder) holder).activityName.setText(profileContactItem.getActivityName());
    }

    @Override
    public int getItemCount() {
        return activitiesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ProfileActivitiesViewHolder extends RecyclerView.ViewHolder {

        CircularImageView profilePic;
        TextView name;
        TextView dateTime;
        TextView activityName;

        public ProfileActivitiesViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.img_profile_pic);
            name = itemView.findViewById(R.id.tv_name);
            dateTime = itemView.findViewById(R.id.tv_subtitle_1);
            activityName = itemView.findViewById(R.id.tv_activity_name);

        }
    }
}
