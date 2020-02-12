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

public class TeamLeadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<TeamLeadItem> teamLeadsList;
    private OnItemClickListener clickListener;
    private HelperGeneral helperObject = new HelperGeneral();

    public TeamLeadsAdapter() {
    }

    public TeamLeadsAdapter(Context context, ArrayList<TeamLeadItem> adminList) {
        this.context = context;
        this.teamLeadsList = adminList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new TeamLeadsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TeamLeadItem teamLeadItem = teamLeadsList.get(position);

        if (null != holder) {
            TeamLeadsViewHolder TeamLeadsViewHolder = (TeamLeadsViewHolder) holder;
            helperObject.glideProfileImage(context, teamLeadItem.getStrProfileImage(), TeamLeadsViewHolder.imgProfileImage);
            TeamLeadsViewHolder.imgProfileImage.setOnClickListener(view -> {
                DatabaseActivity databaseActivity = new DatabaseActivity();
                helperObject.showQuickInfoDialog(context, teamLeadItem.getStrName(), teamLeadItem.getStrProfileImage(), teamLeadItem.getStrPhone(), teamLeadItem.getStrWhatsApp(), teamLeadItem.getStrEmail());
            });

            TeamLeadsViewHolder.tvName.setText(teamLeadItem.getstrTeamLeadAbbr());
            TeamLeadsViewHolder.tvSubTitle1.setText("Name: " + teamLeadItem.getStrName());
            TeamLeadsViewHolder.tvSubTitle2.setText("Zone: " + teamLeadItem.getStrZone());
        }
    }


    @Override
    public int getItemCount() {
        return teamLeadsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void flterList(ArrayList<TeamLeadItem> list) {
        this.teamLeadsList = list;
        notifyDataSetChanged();
    }

    class TeamLeadsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircularImageView imgProfileImage;
        TextView tvName, tvSubTitle1, tvSubTitle2;

        public TeamLeadsViewHolder(@NonNull View itemView) {
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
            TeamLeadItem teamLeadItem = teamLeadsList.get(getAdapterPosition());
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("profileKey", "TEAMLEAD");
            intent.putExtra("teamleadItem", teamLeadItem);
            intent.putExtra("email", teamLeadItem.getStrEmail());
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
