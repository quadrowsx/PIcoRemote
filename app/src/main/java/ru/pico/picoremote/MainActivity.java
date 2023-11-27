package ru.pico.picoremote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

import ru.pico.picoremote.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    DatabaseReference database;
    ActivityMainBinding binding;

    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    List<DevicesModel> devicesModels = new ArrayList<>();

    int delete_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dialogAdd.setVisibility(View.VISIBLE);
                database.child("is_adding").setValue(true);
            }
        });
        binding.allDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dialogStartAll.setVisibility(View.VISIBLE);
            }
        });

        binding.close10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dialogStartAll.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, SetAllVideoActivity.class);
                startActivity(intent);
            }
        });

        binding.close8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dialogStartAll.setVisibility(View.GONE);
            }
        });

        binding.close9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/devices/");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            DatabaseReference childRef = child.getRef();
                            childRef.child("url").setValue("empty");
                            childRef.child("videoType").setValue(2);
                        }
                        binding.dialogStartAll.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dialogAdd.setVisibility(View.GONE);
                database.child("is_adding").setValue(false);
            }
        });

        binding.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dialogDelete.setVisibility(View.GONE);
            }
        });

        binding.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child("devices").child(delete_pos+"").removeValue();
                binding.dialogDelete.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Успешно удалено!", Toast.LENGTH_SHORT).show();
            }
        });

        database = FirebaseDatabase.getInstance().getReference();


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean is_adding = snapshot.child("is_adding").getValue(Boolean.class);
                Log.d("planeta", "onDataChange: "+is_adding);
                if(!is_adding){
                    binding.dialogAdd.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("planeta", "onCancelled: "+error.getMessage());
            }
        });

        database.child("devices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                devicesModels.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    DevicesModel model = postSnapshot.getValue(DevicesModel.class);
                    devicesModels.add(model);
                }
                if(devicesModels.size()==0){
                    binding.text.setVisibility(View.VISIBLE);
                }
                else{
                    binding.text.setVisibility(View.GONE);
                }
                Log.d("planeta", "onCancelled: "+devicesModels.size());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("planeta", "onCancelled: "+error.getMessage());
            }
        });


        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, devicesModels, new RecyclerViewAdapter.ItemClickListener() {
            @Override
            public void play(View view, int position) {
                Intent intent = new Intent(MainActivity.this, SetVideoActivity.class);
                intent.putExtra("device_id", position);
                startActivity(intent);
            }

            @Override
            public void exit(View view, int position) {
                database.child("devices").child(position+"").child("exit").setValue(true);
                Toast.makeText(getApplicationContext(), "Запрос на остановку воспроизведения!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void delete(View view, int position) {
                delete_pos = position;
                binding.dialogDelete.setVisibility(View.VISIBLE);
                //database.child("devices").child(position+"").child("exit").setValue(true);
            }
        });
        recyclerView.setAdapter(adapter);


    }
}