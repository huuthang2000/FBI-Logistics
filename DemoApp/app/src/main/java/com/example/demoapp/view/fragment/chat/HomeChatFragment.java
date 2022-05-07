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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.adapter.chat.PostsAdapter;
import com.example.demoapp.databinding.FragmentHomeChatBinding;
import com.example.demoapp.model.Post;
import com.example.demoapp.view.activity.chat.AddPostActivity;
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


public class HomeChatFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FragmentHomeChatBinding binding;
    private List<Post> postList;
    private PostsAdapter postsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeChatBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // init firebase
        mAuth = FirebaseAuth.getInstance();

        // recycler view and its properties
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // show newest post first, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        // set layout to recyclerview
        binding.rcvPosts.setLayoutManager(layoutManager);

        // init post list
        postList = new ArrayList<>();

        loadPosts();
        return view;
    }

    private void loadPosts() {
        // path of all post
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        // get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post post = ds.getValue(Post.class);

                    postList.add(post);

                    // adapter
                    postsAdapter = new PostsAdapter(getActivity(), postList);
                    // set adapter to recyclerview
                    binding.rcvPosts.setAdapter(postsAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // in case of error
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchPosts(String searchQuery) {
        // path of all post
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        // get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post post = ds.getValue(Post.class);

                    if (post.getpTitle().toLowerCase().contentEquals(searchQuery.toLowerCase()) ||
                            post.getpDescr().toLowerCase().contentEquals(searchQuery.toLowerCase())) {
                        postList.add(post);
                    }


                    // adapter
                    postsAdapter = new PostsAdapter(getActivity(), postList);
                    // set adapter to recyclerview
                    binding.rcvPosts.setAdapter(postsAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // in case of error
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
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

        // search  to search post by post by post title/ description
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        // searchView listenner
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // called when user press search button
                if (!TextUtils.isEmpty(query)) {
                    searchPosts(query);
                } else {
                    loadPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // called as and when user press any
                if (!TextUtils.isEmpty(newText)) {
                    searchPosts(newText);
                } else {
                    loadPosts();
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
        if (id == R.id.action_add_post) {
            startActivity(new Intent(getActivity(), AddPostActivity.class));
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
}