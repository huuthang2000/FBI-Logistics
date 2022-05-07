package com.example.demoapp.adapter.chat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.example.demoapp.model.Chats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<Chats> chatsList;
    private String imageUrl;
    private FirebaseUser firebaseUser;

    public ChatAdapter(Context context, List<Chats> chatsList, String imageUrl) {
        this.context = context;
        this.chatsList = chatsList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder,  int position) {
        // get data
        String message = chatsList.get(position).getMessage();

        // convert time stamp to dd//mm/YYYY hh:mm am/pm
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm aa");
        String date = df.format(Calendar.getInstance().getTime());

        // set data
        holder.tvMessage.setText(message);
        holder.tvTime.setText(date);
        try {
            Picasso.get().load(imageUrl).into(holder.ivProfile);
        } catch (Exception e) {

        }

        // click to show delete dialog
        holder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show delete message confirm dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this message?");
                // delete button
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMesage(position);
                    }
                });
                // cancel delete button
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        // set seen/delivered status of message
        if (position == chatsList.size() - 1) {
            if (chatsList.get(position).getIsSeen().equals("1")) {
                holder.tvIsSeen.setText("Seen");
            } else if (chatsList.get(position).getIsSeen().equals("2")) {
                holder.tvIsSeen.setText("Delivere");
            }
        } else {
            holder.tvIsSeen.setVisibility(View.GONE);
        }
    }

    private void deleteMesage(int i) {
        String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        /*
        processing stream
         - get timestamp of clicked message
         - Compare the timestamp of the click message with all message in chats
         - where both values matches delete that message
         */

        String msgTimeStamp = chatsList.get(i).getTimestamp();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        Query query = databaseReference.orderByChild("timestamp").equalTo(msgTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    /*
                    if your want to allow sender to delete only his message then compare sender value
                    with current user's uid if they match means its the message of sender that is
                    typing to delete
                     */
                    if(ds.child("sender").getValue().equals(myUID)){
                        /*
                    do one of two things here
                        1. remove the message form chats
                        2. set the value of message "This message was deleted..."
                     */
                        //1. remove the message form chats
                        ds.getRef().removeValue();

//                        // 2. set the value of message "This message was deleted..."
//                        HashMap<String, Object> hashMap = new HashMap<>();
//                        hashMap.put("message", "This message was deleted...");
//                        ds.getRef().updateChildren(hashMap);
                        Toast.makeText(context, "message deleted...", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "You can delete only your messages...", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatsList.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    // chat view holder class
    class ChatViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfile;
        TextView tvMessage, tvTime, tvIsSeen;
        LinearLayout messageLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.profile_image);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvIsSeen = itemView.findViewById(R.id.tv_isSeen);
            messageLayout = itemView.findViewById(R.id.layoutMessage);

        }
    }
}
