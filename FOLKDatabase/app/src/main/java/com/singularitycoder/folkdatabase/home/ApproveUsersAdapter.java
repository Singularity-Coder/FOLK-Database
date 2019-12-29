package com.singularitycoder.folkdatabase.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.auth.AuthUserApprovalItem;
import com.singularitycoder.folkdatabase.database.ContactItem;
import com.singularitycoder.folkdatabase.helper.HelperGeneral;

import java.util.ArrayList;

public class ApproveUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AuthUserApprovalItem> approveUsersList;
    private Context context;

    public ApproveUsersAdapter(ArrayList<AuthUserApprovalItem> approveUsersList, Context context) {
        this.approveUsersList = approveUsersList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_person, parent, false);
        return new ApproveUsersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        AuthUserApprovalItem authUserApprovalItem = approveUsersList.get(position);
        if (null != holder) {
            ApproveUsersViewHolder approveUsersViewHolder = ((ApproveUsersViewHolder) holder);
            HelperGeneral.glideProfileImage(context, authUserApprovalItem.getProfileImageUrl(), approveUsersViewHolder.imgProfileImage);
            approveUsersViewHolder.tvName.setText(authUserApprovalItem.getFirstName());
            approveUsersViewHolder.tvSubTitle1.setText("Short Name: " + authUserApprovalItem.getFolkGuideAbbr());
            approveUsersViewHolder.tvSubTitle2.setText("Direct Authority: " + authUserApprovalItem.getDirectAuthority());
            approveUsersViewHolder.tvDate.setText(authUserApprovalItem.getApproveRequestTimeStamp());
        }
    }

    @Override
    public int getItemCount() {
        return approveUsersList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ApproveUsersViewHolder extends RecyclerView.ViewHolder {
        CircularImageView imgProfileImage;
        TextView tvName, tvSubTitle1, tvSubTitle2, tvDate;
        ConstraintLayout personLayout;

        public ApproveUsersViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfileImage = itemView.findViewById(R.id.img_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvSubTitle1 = itemView.findViewById(R.id.tv_subtitle_1);
            tvSubTitle2 = itemView.findViewById(R.id.tv_subtitle_2);
            tvDate = itemView.findViewById(R.id.tv_date);
            personLayout = itemView.findViewById(R.id.con_lay_person_container);
        }
    }
}
