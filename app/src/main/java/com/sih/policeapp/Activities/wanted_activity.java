package com.sih.policeapp.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sih.policeapp.R;

public class wanted_activity extends AppCompatActivity {
    RecyclerView mrecyclerview;
   private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wanted_activity);

        mrecyclerview=findViewById(R.id.rview);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        fetch();

    }

    public void searchhere(String text)
    {
        Query query = FirebaseDatabase.getInstance()
            .getReference()
            .child("criminal_ref").orderByChild("criminal_name").startAt(text).endAt(text+"\uf8ff");


        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(query, new SnapshotParser<Model>() {
                            @NonNull
                            @Override
                            public Model parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Model(snapshot.child("criminal_id").getValue().toString(),
                                        snapshot.child("criminal_name").getValue().toString(),
                                        snapshot.child("profile_pic_url").getValue().toString(),
                                        Float.parseFloat(snapshot.child("criminal_rating").getValue().toString()));
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.wanted_description,parent,false);
                ViewHolder viewHolder=new ViewHolder(view);
                return viewHolder;
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, Model model) {
                holder.setDetails(getApplicationContext(),model.getCriminal_id(),model.getCriminal_name(),model.getProfile_pic_url(),model.getRate());
            }

        };
        mrecyclerview.setAdapter(adapter);
    }


    @Override
    protected void onStop() {
        super.onStop();
    adapter.stopListening();
    finish();}

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("criminal_ref");

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(query, new SnapshotParser<Model>() {
                            @NonNull
                            @Override
                            public Model parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Model(snapshot.child("criminal_id").getValue().toString(),
                                        snapshot.child("criminal_name").getValue().toString(),
                                        snapshot.child("profile_pic_url").getValue().toString(),
                                        Float.parseFloat(snapshot.child("criminal_rating").getValue().toString()));
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.wanted_description,parent,false);
ViewHolder viewHolder=new ViewHolder(view);
                return viewHolder;
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, Model model) {
holder.setDetails(getApplicationContext(),model.getCriminal_id(),model.getCriminal_name(),model.getProfile_pic_url(),model.getRate());
            }

        };
        mrecyclerview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                searchhere(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
            searchhere(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //handle other action bar item clicks here
        if (id == R.id.settings) {
            //display alert dialog to choose sorting
            //showSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }}


