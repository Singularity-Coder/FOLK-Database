package com.singularitycoder.folkdatabase.database.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.database.model.FolkGuideItem;
import com.singularitycoder.folkdatabase.database.view.DatabaseActivity;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.profile.view.ProfileActivity;

import java.util.ArrayList;

public class FolkGuidesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<FolkGuideItem> folkGuidesList;
    private OnItemClickListener clickListener;
    private HelperGeneral helperObject = new HelperGeneral();


    public FolkGuidesAdapter() {
    }

    public FolkGuidesAdapter(Context context, ArrayList<FolkGuideItem> adminList) {
        this.context = context;
        this.folkGuidesList = adminList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new FolkGuidesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FolkGuideItem folkGuideItem = folkGuidesList.get(position);

        if (null != holder) {
            FolkGuidesViewHolder folkGuidesViewHolder = (FolkGuidesViewHolder) holder;
            helperObject.glideProfileImage(context, folkGuideItem.getStrProfileImage(), folkGuidesViewHolder.imgProfileImage);
            folkGuidesViewHolder.imgProfileImage.setOnClickListener(view -> {
                DatabaseActivity databaseActivity = new DatabaseActivity();
                helperObject.showQuickInfoDialog(context, folkGuideItem.getStrName(), folkGuideItem.getStrProfileImage(), folkGuideItem.getStrPhone(), folkGuideItem.getStrWhatsApp(), folkGuideItem.getStrEmail());
            });

            folkGuidesViewHolder.tvName.setText(folkGuideItem.getStrFolkGuideShortName());
            folkGuidesViewHolder.tvSubTitle1.setText("Name: " + folkGuideItem.getStrName());
            folkGuidesViewHolder.tvSubTitle2.setText("Zone: " + folkGuideItem.getStrZone());
        }
    }


    @Override
    public int getItemCount() {
        return folkGuidesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void flterList(ArrayList<FolkGuideItem> list) {
        this.folkGuidesList = list;
        notifyDataSetChanged();
    }

    class FolkGuidesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircularImageView imgProfileImage;
        TextView tvName, tvSubTitle1, tvSubTitle2;

        public FolkGuidesViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfileImage = itemView.findViewById(R.id.img_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvSubTitle1 = itemView.findViewById(R.id.tv_subtitle_1);
            tvSubTitle2 = itemView.findViewById(R.id.tv_subtitle_2);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            clickListener.onItemClick(view, getAdapterPosition());
            FolkGuideItem folkGuideItem = folkGuidesList.get(getAdapterPosition());
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("profileKey", "FOLKGUIDE");
            intent.putExtra("folkguideItem", folkGuideItem);
            intent.putExtra("email", folkGuideItem.getStrEmail());
            context.startActivity(intent);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
