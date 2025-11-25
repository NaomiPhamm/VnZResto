package com.example.vnzresto.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vnzresto.Adapter.MenuAdapter;
import com.example.vnzresto.Model.MenuItem;
import com.example.vnzresto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FavoriteFragment extends Fragment {

    private RecyclerView favoriteRecyclerView;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private ArrayList<MenuItem> favoriteList;
    private MenuAdapter adapter;

    public FavoriteFragment() {
        // Required empty public fragment
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView);
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        favoriteList = new ArrayList<>();
        adapter = new MenuAdapter(requireContext(), favoriteList);
        favoriteRecyclerView.setAdapter(adapter);

        loadFavorites();

        return view;
    }

    private void loadFavorites() {

        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "";
        if (userId.isEmpty()) return;

        DatabaseReference menuRef = database.getReference("menu");
        DatabaseReference favRef = database.getReference("user")
                .child(userId)
                .child("Favorites");
        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot menuSnap) {
                ArrayList<String> validNames = new ArrayList<>();
                for (DataSnapshot itemSnap : menuSnap.getChildren()) {
                    String name = itemSnap.child("foodName").getValue(String.class);
                    if (name != null && !name.isEmpty()) {
                        validNames.add(name);
                    }
                }
                favRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot favSnap) {
                        favoriteList.clear();

                        for (DataSnapshot favItemSnap : favSnap.getChildren()) {
                            MenuItem item = favItemSnap.getValue(MenuItem.class);
                            if (item == null) continue;
                            String favName = item.getFoodName();
                            boolean stillExists = false;
                            for (String menuName : validNames) {
                                if (menuName.equals(favName)) {
                                    stillExists = true;
                                    break;
                                }
                            }
                            if (stillExists) {
                                favoriteList.add(item);
                            } else {
                                favItemSnap.getRef().removeValue();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }
}
