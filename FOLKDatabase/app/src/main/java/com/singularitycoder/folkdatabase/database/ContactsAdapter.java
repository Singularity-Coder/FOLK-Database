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
import com.singularitycoder.folkdatabase.helper.Helper;
import com.singularitycoder.folkdatabase.profile.ProfileActivity;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ContactItem> contactsList;
    private OnItemClickListener clickListener;

    public ContactsAdapter() {
    }

    public ContactsAdapter(Context context, ArrayList<ContactItem> adminList) {
        this.context = context;
        this.contactsList = adminList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new ContactsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactItem contactItem = contactsList.get(position);

        if (holder != null) {
            ContactsViewHolder contactsViewHolder = (ContactsViewHolder) holder;
//            if (contactsViewHolder.imgProfileImage.equals("") || contactsViewHolder.imgProfileImage.equals(null)) {
//                String startingLetter = contactItem.getFirstName().substring(0, 1).toUpperCase();
//                TextDrawable drawable = TextDrawable.builder().buildRound(startingLetter, R.color.colorAccent);
////                TextDrawable drawable = TextDrawable.builder().beginConfig().width(60).height(60).endConfig().buildRound(startingLetter, R.color.colorAccent);
//                contactsViewHolder.imgProfileImage.setImageDrawable(drawable);
//            } else {
//                Helper.glideProfileImage(context, contactItem.getStrProfileImage(), contactsViewHolder.imgProfileImage);
//            }

            Helper.glideProfileImage(context, contactItem.getStrProfileImage(), contactsViewHolder.imgProfileImage);
            contactsViewHolder.imgProfileImage.setOnClickListener(view -> {
                DatabaseActivity databaseActivity = new DatabaseActivity();
                databaseActivity.showQuickInfoDialog(context, contactItem.getFirstName(), contactItem.getStrProfileImage(), contactItem.getStrPhone(), contactItem.getStrWhatsApp(), contactItem.getStrEmail());
            });

            contactsViewHolder.tvName.setText(contactItem.getFirstName());
            contactsViewHolder.tvSubTitle1.setText("FOLK Guide: " + contactItem.getStrFolkGuide());
            contactsViewHolder.tvSubTitle2.setText("Occupation: " + contactItem.getStrOccupation());
        }

    }


    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void flterList(ArrayList<ContactItem> list) {
        this.contactsList = list;
        notifyDataSetChanged();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircularImageView imgProfileImage;
        TextView tvName, tvSubTitle1, tvSubTitle2;

        public ContactsViewHolder(@NonNull View itemView) {
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
            ContactItem contactItem = contactsList.get(getAdapterPosition());
            Intent contactIntent = new Intent(context, ProfileActivity.class);
            contactIntent.putExtra("profileKey", "CONTACT");
            contactIntent.putExtra("contactItem", contactItem);
            context.startActivity(contactIntent);

        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
