package com.example.vnzresto.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import com.example.vnzresto.Model.MenuItem;import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.vnzresto.Adapter.MenuAdapter;
import com.example.vnzresto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private RecyclerView menuRecyclerView;
    private SearchView searchView;
    private MenuAdapter adapter;
    private FirebaseDatabase database;
    private final List<MenuItem> searchMenuItems= new ArrayList<>();


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_search, container, false);
        menuRecyclerView = view.findViewById(R.id.menuRecyclerView);
        searchView = view.findViewById(R.id.searchView);

        menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        takeMenuItems();
        setupSearchView();

        return view;
    }
    private void takeMenuItems(){
        database = FirebaseDatabase.getInstance();
        DatabaseReference foodReference = database.getReference().child("menu");

        searchMenuItems.clear();

        foodReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    MenuItem menuItem = foodSnapshot.getValue(MenuItem.class);
                    if (menuItem != null) {
                        searchMenuItems.add(menuItem);
                    }
                }

                showAllMenu();
            }
            @Override
            public void onCancelled( DatabaseError error) {

            }
        });
    }
    private void showAllMenu(){
        List<MenuItem> checkMenu = new ArrayList<>(searchMenuItems);
        setAdapter(checkMenu);
    }
    private void    setAdapter(List<MenuItem> checkMenuItems){
        ArrayList<MenuItem> list = new ArrayList<>(checkMenuItems);
        adapter = new MenuAdapter(requireContext(),list);
        menuRecyclerView.setAdapter(adapter);
    }
    private void setupSearchView(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                checkMenuItems(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                checkMenuItems(newText);
                return true;
            }
        });
    }
    private void checkMenuItems(String text){
        if(text == null)    text = "";
        List<MenuItem> checkList = new ArrayList<>();
        for( MenuItem item : searchMenuItems){
            String name = item.getFoodName();
            if (name != null && name.toLowerCase().contains(text.toLowerCase())) {
                checkList.add(item);
            }
        }
        setAdapter(checkList);
    }
}