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

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<PersonModel> mAdminList;
    private OnItemClickListener clickListener;

    public ContactsAdapter() {
    }

    public ContactsAdapter(Context context, ArrayList<PersonModel> adminList) {
        mContext = context;
        mAdminList = adminList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new AdminViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PersonModel personModel = mAdminList.get(position);

        ((AdminViewHolder) holder).imgProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity homeActivity = new HomeActivity();
                homeActivity.showQuickInfoDialog(mContext);
            }
        });

        ((AdminViewHolder) holder).tvName.setText(personModel.getFirstName());
        ((AdminViewHolder) holder).tvSubTitle1.setText("FOLK Guide: " + personModel.getStrFolkGuide());
        ((AdminViewHolder) holder).tvSubTitle2.setText("Occupation: " + personModel.getStrOccupation());
    }


    @Override
    public int getItemCount() {
        return mAdminList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void flterList(ArrayList<PersonModel> list) {
        this.mAdminList = list;
        notifyDataSetChanged();
    }


    class AdminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircularImageView imgProfileImage;
        TextView tvName, tvSubTitle1, tvSubTitle2;

        public AdminViewHolder(@NonNull View itemView) {
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
