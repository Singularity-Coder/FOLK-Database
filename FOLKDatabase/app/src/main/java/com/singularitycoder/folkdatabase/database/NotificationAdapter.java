package com.singularitycoder.folkdatabase.database;

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

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ContactItem> notifList;
    Context context;

    public NotificationAdapter(ArrayList<ContactItem> notifList, Context context) {
        this.notifList = notifList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        ContactItem contactItem = notifList.get(position);

//        ((NotificationViewHolder) holder).imgProfileImage.setImageResource(contactItem.getImgProfileImage());
        ((NotificationViewHolder) holder).imgProfileImage.requestLayout();
        ((NotificationViewHolder) holder).imgProfileImage.getLayoutParams().width = 100;
        ((NotificationViewHolder) holder).imgProfileImage.getLayoutParams().height = 100;

        ((NotificationViewHolder) holder).tvName.setText(contactItem.getStrName());
        ((NotificationViewHolder) holder).tvName.setTextSize(16);

        ((NotificationViewHolder) holder).tvSubTitle1.setVisibility(View.GONE);
        ((NotificationViewHolder) holder).tvSubTitle1.setText(contactItem.getStrSubTitle1());

        ((NotificationViewHolder) holder).tvSubTitle2.setVisibility(View.VISIBLE);
        ((NotificationViewHolder) holder).tvSubTitle2.setText(contactItem.getStrSubTitle2());
        ((NotificationViewHolder) holder).tvSubTitle2.setMaxLines(10);

        ((NotificationViewHolder) holder).tvDate.setVisibility(View.VISIBLE);
        ((NotificationViewHolder) holder).tvDate.setText(contactItem.getStrDate());
//        ((NotificationViewHolder) holder).tvDate.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

        ((NotificationViewHolder) holder).arrow.setVisibility(View.GONE);

        ((NotificationViewHolder) holder).personLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((NotificationViewHolder) holder).accept.setVisibility(View.VISIBLE);

//                if ( ((NotificationViewHolder) holder).accept.getVisibility() == View.GONE) {
//                    ((NotificationViewHolder) holder).accept.setVisibility(View.VISIBLE);
//                } else {
//                    ((NotificationViewHolder) holder).accept.setVisibility(View.GONE);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        CircularImageView imgProfileImage;
        CircularImageView imgProfileImageTop;
        TextView tvName, tvSubTitle1, tvSubTitle2, tvDate;
        ImageView arrow, accept, stopper;
        ConstraintLayout personLayout;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfileImage = itemView.findViewById(R.id.img_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvSubTitle1 = itemView.findViewById(R.id.tv_subtitle_1);
            tvSubTitle2 = itemView.findViewById(R.id.tv_subtitle_2);
            tvDate = itemView.findViewById(R.id.tv_date);
            arrow = itemView.findViewById(R.id.img_arrow);
            accept = itemView.findViewById(R.id.img_accept);
            personLayout = itemView.findViewById(R.id.con_lay_person_container);
            stopper = itemView.findViewById(R.id.img_stopper);

        }
    }
}
