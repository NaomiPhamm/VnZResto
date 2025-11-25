package com.example.vnzresto.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnzresto.Model.MenuItem;
import com.example.vnzresto.R;
import com.example.vnzresto.Resto_User_Activity.DetailsActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private Context context;
    private ArrayList<MenuItem> menuItems;

    public MenuAdapter(Context context, ArrayList<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public MenuAdapter.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.menu_item,parent,false);
      return  new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MenuViewHolder holder, int position) {
        MenuItem item = menuItems.get(position);
        holder.nametxt.setText(item.getFoodName());
        holder.pricetxt.setText(item.getFoodPrice());
        String imageURL = item.getFoodImage();
        if(imageURL!= null && !imageURL.isEmpty()){
            new ImageLoadTask(imageURL,holder.imageView).execute();
        }else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.addToCart.setOnClickListener( v ->{
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("MenuItemName", item.getFoodName());
            intent.putExtra("MenuItemPrice", item.getFoodPrice());
            intent.putExtra("MenuItemImage", item.getFoodImage());
            intent.putExtra("MenuItemDescription", item.getFoodDescription());
            intent.putExtra("MenuItemIngredients", item.getFoodIngredient());

            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return menuItems!= null ? menuItems.size(): 0;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nametxt, pricetxt, addToCart;
        public MenuViewHolder(@NonNull View view){
            super(view);
            imageView = view.findViewById(R.id.menuImage);
            nametxt = view.findViewById(R.id.menuFoodName);
            pricetxt = view.findViewById(R.id.menuPrice);
            addToCart = view.findViewById(R.id.menuAddToCart);

        }
    }
    private static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }
}
