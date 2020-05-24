package com.supinfo.tp.gostore.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.supinfo.tp.gostore.Utils.ProductGridItemDecoration;
import com.supinfo.tp.gostore.ProductRecyclerViewAdapter;
import com.supinfo.tp.gostore.R;
import com.supinfo.tp.gostore.data.model.ProductEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_products)
    RecyclerView rvProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        rvProducts.setHasFixedSize(true);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        ProductRecyclerViewAdapter adapter = new ProductRecyclerViewAdapter(
                ProductEntry.initProductEntryList(getResources()));
        rvProducts.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        rvProducts.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
    }
}
