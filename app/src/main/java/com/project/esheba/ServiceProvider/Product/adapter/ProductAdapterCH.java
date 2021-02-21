package com.project.esheba.ServiceProvider.Product.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.esheba.Constant;
import com.project.esheba.R;
import com.project.esheba.ServiceProvider.Product.ProductDescriptionCHActivity;
import com.project.esheba.ServiceProvider.Product.model.ProductCH;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapterCH extends RecyclerView.Adapter<ProductAdapterCH.MyViewHolder> {

    private List<ProductCH> productCh;
    Context context;

    public ProductAdapterCH(Context context, List<ProductCH> contacts) {
        this.context = context;
        this.productCh = contacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_sp, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(productCh.get(position).getName());
        holder.category.setText(productCh.get(position).getCategory());

        holder.price.setText(Constant.KEY_CURRENCY + productCh.get(position).getPrice());

        String url = Constant.MAIN_URL + "/product_image/" + productCh.get(position).getImage();

        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.loading)
                .error(R.drawable.not_found)
                .into(holder.img_product);


    }

    @Override
    public int getItemCount() {
        return productCh.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, price, category;
        ImageView img_product;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_namePhotographer);
            price = itemView.findViewById(R.id.txt_pricePhotographer);
            category = itemView.findViewById(R.id.txt_sizePhotographer);
            img_product = itemView.findViewById(R.id.img_productPhotographer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, ProductDescriptionCHActivity.class);

            i.putExtra("id", productCh.get(getAdapterPosition()).getProductId());
            i.putExtra("name", productCh.get(getAdapterPosition()).getName());
            i.putExtra("price", productCh.get(getAdapterPosition()).getPrice());
            i.putExtra("image", productCh.get(getAdapterPosition()).getImage());
            i.putExtra("category", productCh.get(getAdapterPosition()).getCategory());
            i.putExtra("quantity", productCh.get(getAdapterPosition()).getQuantity());
            i.putExtra("description", productCh.get(getAdapterPosition()).getDescription());
            i.putExtra("ch_cell", productCh.get(getAdapterPosition()).getChCell());
            context.startActivity(i);
            Toast.makeText(context, productCh.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();

            Log.d("ID", " id: " + productCh.get(getAdapterPosition()).getProductId());
        }
    }
}