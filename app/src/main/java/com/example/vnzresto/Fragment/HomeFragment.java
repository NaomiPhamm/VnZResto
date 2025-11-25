package com.example.vnzresto.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import com.example.vnzresto.Model.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.vnzresto.Adapter.MenuAdapter;
import com.example.vnzresto.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView populerRecyclerView;
    private View viewAllMenu;
    private ImageSlider imageSlider;
    private DatabaseReference foodata;
    private final List<MenuItem> menuItems = new ArrayList<>();


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@NonNull ViewGroup container,@NonNull Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_home,container,false);
        populerRecyclerView = view.findViewById(R.id.popularRecyclerView);
        viewAllMenu        = view.findViewById(R.id.viewAllMenu);
        imageSlider        = view.findViewById(R.id.image_slider);

        viewAllMenu.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), Menu.class);
            startActivity(intent);
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        foodata = database.getReference("menu");

        loadPopularItems();

        setupImageSlider();
       return view;
    }
    private void loadPopularItems(){
        menuItems.clear();
        foodata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    MenuItem item = foodSnapshot.getValue(MenuItem.class);
                    if (item != null) {
                        menuItems.add(item);
                    }
                }
                if(menuItems.isEmpty()){
                    if(getContext()!= null){
                        Toast.makeText(getContext(),"No menu items found",Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                Collections.shuffle(menuItems);
                int max = Math.min(6,menuItems.size());
                List<MenuItem> popularList = menuItems.subList(0, max);

                ArrayList<MenuItem> popularArray = new ArrayList<>(popularList);

                MenuAdapter adapter = new MenuAdapter(requireContext(),popularArray);
                populerRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                populerRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) {Toast.makeText(getContext(), "Failed to load menu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setupImageSlider() {
        List<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner3, ScaleTypes.FIT));

        imageSlider.setImageList(imageList, ScaleTypes.FIT);

        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int position) {
                String msg = "Selected Image " + position;
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void doubleClick(int position) {

            }
        });
    }


}