package com.supinfo.tp.gostore;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.supinfo.tp.gostore.Utils.ImageRequester;
import com.supinfo.tp.gostore.data.model.OrderEntry;
import com.supinfo.tp.gostore.data.model.ProductEntry;

import org.apache.commons.lang3.StringUtils;

public class ProducViewHolder extends RecyclerView.ViewHolder {
    public NetworkImageView productImage;
    public TextView productTitle;
    public TextView productPrice;
    private ImageRequester imageRequester;

    public ProducViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage = itemView.findViewById(R.id.product_image);
        productTitle = itemView.findViewById(R.id.product_title);
        productPrice = itemView.findViewById(R.id.product_price);
        imageRequester = ImageRequester.getInstance();
    }

    public void bindView(OrderEntry order, ProductEntry product) {
        //productTitle.setText(order.title);
        productPrice.setText("PU:" + product.getPrice() + "€ Qte:" + order.getQte() + " Total: " + order.getTotal_price() + "€");
        //imageRequester.setImageFromUrl(productImage, order.getpRODUC);
        if (StringUtils.isNotBlank(product.getPhotoUrl())) {
            imageRequester.setImageFromUrl(productImage, product.getPhotoUrl());

        }
        if (StringUtils.isNotBlank(product.getProduct_name())) {
            if (product.getProduct_name().length() > 25)
                productTitle.setText(product.getProduct_name().substring(0, 25));
            else
                productTitle.setText(product.getProduct_name());
        }
    }
}
