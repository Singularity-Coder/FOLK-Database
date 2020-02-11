package com.singularitycoder.folkdatabase.database;

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
import com.singularitycoder.folkdatabase.helper.HelperGeneral;
import com.singularitycoder.folkdatabase.profile.ProfileActivity;

import java.util.ArrayList;

public class ZonalHeadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ZonalHeadItem> zonalHeadsList;
    private OnItemClickListener clickListener;
    private HelperGeneral helperObject = new HelperGeneral();

    public ZonalHeadsAdapter() {
    }

    public ZonalHeadsAdapter(Context context, ArrayList<ZonalHeadItem> zonalHeadsList) {
        this.context = context;
        this.zonalHeadsList = zonalHeadsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new ZonalHeadsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ZonalHeadItem zonalHeadItem = zonalHeadsList.get(position);

        if (holder != null) {
            ZonalHeadsViewHolder zonalHeadsViewHolder = (ZonalHeadsViewHolder) holder;
            helperObject.glideProfileImage(context, zonalHeadItem.getStrProfileImage(), zonalHeadsViewHolder.imgProfileImage);
            String fullName = zonalHeadItem.getStrFirstName() + " " + zonalHeadItem.getStrLastName();
            zonalHeadsViewHolder.imgProfileImage.setOnClickListener(view -> {
                DatabaseActivity databaseActivity = new DatabaseActivity();
                helperObject.showQuickInfoDialog(context, fullName, zonalHeadItem.getStrProfileImage(), zonalHeadItem.getStrPhone(), zonalHeadItem.getStrWhatsApp(), zonalHeadItem.getStrEmail());
            });

            zonalHeadsViewHolder.tvName.setText(new StringBuilder(zonalHeadItem.getStrFirstName()).append(" ").append(zonalHeadItem.getStrLastName()));
            zonalHeadsViewHolder.tvSubTitle1.setText(new StringBuilder("Experience in KC: ").append(zonalHeadItem.getStrKcExperience()).append(" Years"));
            zonalHeadsViewHolder.tvSubTitle2.setText(new StringBuilder("Member Type: ").append(zonalHeadItem.getStrMemberType()));
        }
    }


    @Override
    public int getItemCount() {
        return zonalHeadsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void filterList(ArrayList<ZonalHeadItem> list) {
        this.zonalHeadsList = list;
        notifyDataSetChanged();
    }


    class ZonalHeadsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircularImageView imgProfileImage;
        TextView tvName, tvSubTitle1, tvSubTitle2;

        ZonalHeadsViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfileImage = itemView.findViewById(R.id.img_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvSubTitle1 = itemView.findViewById(R.id.tv_subtitle_1);
            tvSubTitle2 = itemView.findViewById(R.id.tv_subtitle_2);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
