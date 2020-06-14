package com.supinfo.tp.gostore.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.gson.Gson;
import com.supinfo.tp.gostore.ProducViewHolder;
import com.supinfo.tp.gostore.R;
import com.supinfo.tp.gostore.Utils.ProductGridItemDecoration;
import com.supinfo.tp.gostore.data.model.OrderEntry;
import com.supinfo.tp.gostore.data.model.ProductEntry;
import com.supinfo.tp.gostore.data.model.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.supinfo.tp.gostore.Utils.Constants.USER_INFO_KEY;

public class ShoppingListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_products)
    RecyclerView rvProducts;
    DatabaseReference ref;
    UserInfo userInfo;
    SharedPreferences preferences;
    FirebaseRecyclerAdapter<OrderEntry, ProducViewHolder> adapter;
    private FirebaseFunctions functions;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        functions = FirebaseFunctions.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        userInfo = gson.fromJson(preferences.getString(USER_INFO_KEY, null), UserInfo.class);


        DatabaseReference myRef = ref.child("orders").getRef();
        FirebaseRecyclerOptions<OrderEntry> options = new FirebaseRecyclerOptions.Builder<OrderEntry>()
                .setQuery(myRef.orderByChild("user_id").equalTo(userInfo.getEmail()), OrderEntry.class)
                .setLifecycleOwner(this)
                .build();


        rvProducts.setHasFixedSize(true);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));


        adapter = new FirebaseRecyclerAdapter<OrderEntry, ProducViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final ProducViewHolder holder, final int position, @NonNull final OrderEntry order) {
                ref.child("products/" + order.getProduct_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            ProductEntry product = dataSnapshot.getValue(ProductEntry.class);
                            holder.bindView(order, product);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public ProducViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final ProducViewHolder holder = new ProducViewHolder(getLayoutInflater().from(parent.getContext()).inflate(R.layout.product_item, parent, false));
                return holder;
            }

            @Override
            public void onDataChanged() {
            }
        };

        rvProducts.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        rvProducts.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

        ref.child("users").child(this.userInfo.getEmail()).child("entries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.getChildren().forEach((DataSnapshot entry) -> {
                        String status = entry.child("status").getValue(String.class);
                        if (status.equals("not-shopping")) {
                           /* Email from = new Email("305028@supinfo.com");
                            String subject = "Sending with SendGrid is Fun";
                            Email to = new Email("mosktmp+alcrb@gmail.com");
                            Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
                            Mail mail = new Mail(from, subject, to, content);
                            mail.setSubject(subject);
                            mail.setTemplateId("d-1da7fff0c01947e38f49de4ec6834434");

                            SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
                            Request request = new Request();
                            try {
                                request.setMethod(Method.POST);
                                request.setEndpoint("mail/send");
                                request.setBody(mail.build());
                                Response response = sg.api(request);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }*/
                            //System.out.println("shopping");
                            Intent intent = new Intent(ShoppingListActivity.this, QrcodeHomeActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
