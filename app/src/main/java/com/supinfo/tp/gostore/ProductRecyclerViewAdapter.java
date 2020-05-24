package com.supinfo.tp.gostore;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.supinfo.tp.gostore.Utils.ImageRequester;
import com.supinfo.tp.gostore.data.model.ProductEntry;

import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProducViewHolder> {
    List<ProductEntry> productList;
    private ImageRequester imageRequester;

    public ProductRecyclerViewAdapter(List<ProductEntry> list) {
        this.productList = list;
        imageRequester = ImageRequester.getInstance();
    }

    @NonNull
    @Override
    public ProducViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProducViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProducViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            ProductEntry product = productList.get(position);
            holder.productTitle.setText(product.title);
            holder.productPrice.setText(product.price);
            imageRequester.setImageFromUrl(holder.productImage, product.url);
        }
    }

    @Override
    public int getItemCount() {
        return this.productList != null ? this.productList.size() : 0;
    }
}
