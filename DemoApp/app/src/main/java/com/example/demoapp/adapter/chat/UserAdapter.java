package com.example.demoapp.adapter.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.example.demoapp.model.Users;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<Users> usersList;

    public UserAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_user, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // get data
        String hisUID = usersList.get(position).getUid();
        String userImage = usersList.get(position).getImage();
        String userName = usersList.get(position).getName();
        String userEmail = usersList.get(position).getEmail();

        // setData
        holder.tvName.setText(userName);
        holder.tvEmail.setText(userEmail);
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.ic_account).into(holder.ivAvatar);
        } catch (Exception e) {

        }
        // handle item click
        holder.itemView.setOnClickListener(v -> {

            // show dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(new String[]{"Profile", "Chat"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        // profile clicked
                        /* click to go there profile activity with uid, this uid is of clicked user
                         which will be used to show user specific data/posts
                        */
//                        Intent intent = new Intent(context, DetailInfoActivity.class);
//                        intent.putExtra("uid", hisUID);
//                        context.startActivity(intent);

                    }
                    if (which == 1) {
                        // chat clicked
                        /*
                            Click user form user list to start chatting/ messaging
                            Start activity by putting UID of receiver
                             we will use that UID to identify the user we are gonna chat
                          */
//                        Intent intent = new Intent(context, ChatActivity.class);
//                        intent.putExtra("hisUid", hisUID);
//                        context.startActivity(intent);

                    }
                }
            });
            builder.create().show();
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    // user viewHolder class
    class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatar;
        TextView tvName, tvEmail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.profile_image);
//            tvName = itemView.findViewById(R.id.tv_name_item);
//            tvEmail = itemView.findViewById(R.id.tv_email_item);
        }
    }
}
