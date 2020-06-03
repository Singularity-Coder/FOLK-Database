package com.singularitycoder.folkdatabase.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;

import java.util.ArrayList;

public class AllUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<AllUsersItem> allUsersList;
    private OnItemClickListener clickListener;
    private HelperGeneral helperObject = new HelperGeneral();

    public AllUsersAdapter() {
    }

    public AllUsersAdapter(Context context, ArrayList<AllUsersItem> allUsersList) {
        this.context = context;
        this.allUsersList = allUsersList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new AllUsersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AllUsersItem allUsersItem = allUsersList.get(position);

        if (holder != null) {
            AllUsersViewHolder allUsersViewHolder = (AllUsersViewHolder) holder;
            helperObject.glideProfileImage(context, allUsersItem.getStrProfileImage(), allUsersViewHolder.imgProfileImage);
            String fullName = allUsersItem.getStrFirstName();
            allUsersViewHolder.imgProfileImage.setOnClickListener(view -> helperObject.showQuickInfoDialog(context, fullName, allUsersItem.getStrProfileImage(), allUsersItem.getStrPhone(), allUsersItem.getStrWhatsApp(), allUsersItem.getStrEmail()));

            allUsersViewHolder.tvName.setText(allUsersItem.getStrFirstName());
            allUsersViewHolder.tvSubTitle1.setText("Email: " + allUsersItem.getStrEmail());
            allUsersViewHolder.tvSubTitle2.setText("Phone: " + allUsersItem.getStrPhone());
        }
    }


    @Override
    public int getItemCount() {
        return allUsersList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void filterList(ArrayList<AllUsersItem> list) {
        this.allUsersList = list;
        notifyDataSetChanged();
    }


    class AllUsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircularImageView imgProfileImage;
        TextView tvName, tvSubTitle1, tvSubTitle2;

        AllUsersViewHolder(@NonNull View itemView) {
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
