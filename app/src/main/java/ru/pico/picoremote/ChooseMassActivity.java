package ru.pico.picoremote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

import ru.pico.picoremote.databinding.ActivityChooseMassBinding;

public class ChooseMassActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    List<DevicesModel> models = new ArrayList<>();

    ActivityChooseMassBinding binding;

    RecyclerViewAdapter2 adapter2;


    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        binding = ActivityChooseMassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new RecyclerViewAdapter2(this, models, new RecyclerViewAdapter2.ItemClickListener() {
            @Override
            public void checkPos(View view, int position, int id) {

            }
        });
        recyclerView.setAdapter(adapter2);

        database = FirebaseDatabase.getInstance().getReference();
        database.child("devices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    DevicesModel model = postSnapshot.getValue(DevicesModel.class);
                    models.add(model);
                }
                if(models.size()==0){
                    binding.text.setVisibility(View.VISIBLE);
                }
                else{
                    binding.text.setVisibility(View.GONE);
                }
                Log.d("planeta", "onCancelled: "+models.size());
                recyclerView.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("planeta", "onCancelled: "+error.getMessage());
            }
        });


    }
}