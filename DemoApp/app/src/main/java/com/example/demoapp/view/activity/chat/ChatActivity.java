package com.example.demoapp.view.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.adapter.chat.ChatAdapter;
import com.example.demoapp.databinding.ActivityChatBinding;
import com.example.demoapp.model.Chats;
import com.example.demoapp.model.Users;
import com.example.demoapp.notifications.APIService;
import com.example.demoapp.notifications.Client;
import com.example.demoapp.notifications.Data;
import com.example.demoapp.notifications.Response;
import com.example.demoapp.notifications.Sender;
import com.example.demoapp.notifications.Token;
import com.example.demoapp.view.activity.login_register.SignInActivity;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDBRef;
    private String hisUid;
    private String myUid;
    private String hisImage;

    private APIService apiService;
    boolean notify = false;

    // for checking if use has seen message or not
    ValueEventListener seenEventListener;
    DatabaseReference useRefForSeen;

    List<Chats> chatsList;
    ChatAdapter chatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // init toolbar
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle("");

        /*
        on clicking user from users list we have passed tha user's UID using intent
        so get that uid here to get the profile  picture, name and start chat with that
        users
         */
        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUid");
        // init firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDBRef = firebaseDatabase.getReference("Users");

        // layout for RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        // recycleview properties
        binding.rcvChat.setHasFixedSize(true);
        binding.rcvChat.setLayoutManager(linearLayoutManager);

        // create api service
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);


        // search user to get that user's info
        Query userQuery = usersDBRef.orderByChild("uid").equalTo(hisUid);
        // get user picture and name
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check until required info is received
                for (DataSnapshot ds : snapshot.getChildren()) {
                    // get data
                    String name = "" + ds.child("name").getValue();
                    hisImage = "" + ds.child("image").getValue();
                    String typingStatus = "" + ds.child("typingTo").getValue();

                    // check typing  status
                    if (typingStatus.equals(myUid)) {
                        binding.tvUserStatus.setText("typing....");
                    } else {
                        // get value of omlinestatus
                        String onlineStatus = "" + ds.child("onlineStatus").getValue();
                        if (onlineStatus.equals("online")) {
                            binding.tvUserStatus.setText(onlineStatus);
                        } else {
                            // convent timestamp to proper time dat
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm aa");
                            String date = df.format(Calendar.getInstance().getTime());
                            binding.tvUserStatus.setText("Last seen at: " + date);
                        }
                    }
                    // set data
                    binding.tvUserNameChat.setText(name);
                    try {
                        // image received, set it to imageview intoolbar
                        Picasso.get().load(hisImage).placeholder(R.drawable.ic_account).into(binding.profileImageChat);
                    } catch (Exception e) {
                        // there is exception getting picture, set default picture
                        Picasso.get().load(R.drawable.ic_account).into(binding.profileImageChat);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // click button to send message
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                // get text from edit text
                String message = binding.etMessage.getText().toString().trim();
                // check if text is empty or not
                if (TextUtils.isEmpty(message)) {
                    // text empty
                    Toast.makeText(ChatActivity.this, "Cannot send the empty message", Toast.LENGTH_SHORT).show();
                } else {
                    // text not empty
                    sendMessage(message);
                }
                //reset edittext after sending message
                binding.etMessage.setText("");

            }
        });
        // click button back
        binding.btnBack.setOnClickListener(v ->{onBackPressed();});

        // check edit text change listener
        binding.etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    checkTypingStatus("noOne");
                } else {
                    checkTypingStatus(hisUid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        readMessage();
        seenMessage();
    }

    private void seenMessage() {
        useRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenEventListener = useRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Chats chats = ds.getValue(Chats.class);
                    if (chats.getReceiver().equals(myUid) && chats.getSender().equals(hisUid)) {
                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                        hasSeenHashMap.put("isSeen", "1");
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage() {
        chatsList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Chats chats = ds.getValue(Chats.class);
                    if (chats.getReceiver().equals(myUid) && chats.getSender().equals(hisUid) ||
                            chats.getReceiver().equals(hisUid) && chats.getSender().equals(myUid)) {
                        chatsList.add(chats);
                    }
                    // adapter
                    chatAdapter = new ChatAdapter(ChatActivity.this, chatsList, hisImage);
                    chatAdapter.notifyDataSetChanged();
                    binding.rcvChat.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("isSeen", "2");
        databaseReference.child("Chats").push().setValue(hashMap);


        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if(notify){
                    senNotification(hisUid, users.getName(), message);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void senNotification(String hisUid, String name, String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("tokensNotification");
        Query query = allTokens.orderByKey().equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(myUid, name+":"+message, "New Message", hisUid, R.drawable.ic_face);

                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(ChatActivity.this,""+response.message(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkUserStatus() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            myUid = user.getUid(); // currently signed is users uid
        } else {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }

    private void checkOnlineStatus(String status) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", status);
        // update value of onlineStatus of current user
        databaseReference.updateChildren(hashMap);
    }

    private void checkTypingStatus(String typing) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("typingTo", typing);
        // update value of onlineStatus of current user
        databaseReference.updateChildren(hashMap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // hide searchview, as don't need it here
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_add_post).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            mAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        //set online
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onResume() {
        //set online
        checkOnlineStatus("online");
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // get timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());

        // set offline with last seen time stamp
        checkOnlineStatus(timestamp);
        checkTypingStatus("noOne");
        useRefForSeen.removeEventListener(seenEventListener);
    }




}