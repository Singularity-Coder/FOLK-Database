package com.singularitycoder.folkdatabase.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.helper.Helper;

import java.util.ArrayList;

public class FolkGuidesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<FolkGuideItem> folkGuidesList;
    private OnItemClickListener clickListener;

    public FolkGuidesAdapter() {
    }

    public FolkGuidesAdapter(Context context, ArrayList<FolkGuideItem> adminList) {
        mContext = context;
        folkGuidesList = adminList;
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

        if (holder != null) {
            FolkGuidesViewHolder folkGuidesViewHolder = (FolkGuidesViewHolder) holder;
            Helper.glideProfileImage(mContext, folkGuideItem.getStrProfileImage(), folkGuidesViewHolder.imgProfileImage);
            folkGuidesViewHolder.imgProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeActivity homeActivity = new HomeActivity();
                    homeActivity.showQuickInfoDialog(mContext);
                }
            });

            folkGuidesViewHolder.tvName.setText(folkGuideItem.getStrFirstName());
            folkGuidesViewHolder.tvSubTitle1.setText("Experience in KC: " + folkGuideItem.getStrKcExperience() + " Years");
            folkGuidesViewHolder.tvSubTitle2.setText("Department: " + folkGuideItem.getStrDepartment());
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
