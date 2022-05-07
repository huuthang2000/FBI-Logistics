package com.example.demoapp.view.fragment.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.adapter.chat.UserAdapter;
import com.example.demoapp.databinding.FragmentUsersBinding;
import com.example.demoapp.model.Users;
import com.example.demoapp.view.activity.login_register.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private FragmentUsersBinding binding;
    private UserAdapter userAdapter;
    private List<Users> usersList;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater, container, false);

        //intit recycview user
        initRecyclerViewUsers();
        usersList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();

        // get all user
        getAllUsers();

        return binding.getRoot();
    }

    private void getAllUsers() {
        // get current user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // get path of database name "Users" cotaining users info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        // get all data from path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Users users = ds.getValue(Users.class);
                    // get all users except currently signed is user
                    if (!users.getUid().equals(firebaseUser.getUid())) {
                        usersList.add(users);
                    }
                    // adapter
                    userAdapter = new UserAdapter(getActivity(), usersList);
                    // set adapter to recycle view
                    binding.rcvUser.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void searchUsers(String query) {
        // get current user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // get path of database name "Users" cotaining users info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        // get all data from path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Users users = ds.getValue(Users.class);
                    // get all searched users except currently signed is user
                    if (!users.getUid().equals(firebaseUser.getUid())) {
                        if (users.getName().toLowerCase().contains(query.toLowerCase())
                                || users.getEmail().toLowerCase().contains(query.toLowerCase())) {
                            usersList.add(users);
                        }

                    }
                    // adapter
                    userAdapter = new UserAdapter(getActivity(), usersList);
                    // refresh adapter
                    userAdapter.notifyDataSetChanged();
                    // set adapter to recycle view
                    binding.rcvUser.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        // hide addpost icon from this fragment
        menu.findItem(R.id.action_add_post).setVisible(false);
        // search view
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        // search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // if search query is not empty the search
                if (!TextUtils.isEmpty(query.trim())) {
                    // search text contains text, search it
                    searchUsers(query);
                } else {
                    // search text empty, get all users
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText.trim())) {
                    // search text contains text, search it
                    searchUsers(newText);
                } else {
                    // search text empty, get all users
                    getAllUsers();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    // handle meni item click

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // get item id
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            mAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkUserStatus() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

        } else {
            startActivity(new Intent(getActivity(), SignInActivity.class));
            getActivity().finish();
        }
    }

    private void initRecyclerViewUsers() {
        binding.rcvUser.setHasFixedSize(true);
        binding.rcvUser.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}