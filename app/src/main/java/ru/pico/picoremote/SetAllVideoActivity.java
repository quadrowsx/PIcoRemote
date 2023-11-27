package ru.pico.picoremote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.pico.picoremote.databinding.ActivitySetAllVideoBinding;
import ru.pico.picoremote.databinding.ActivitySetVideoBinding;

public class SetAllVideoActivity extends AppCompatActivity {

    ActivitySetAllVideoBinding binding;

    int video_type = 2;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        binding = ActivitySetAllVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance().getReference();


        Bundle extras = getIntent().getExtras();


        binding.radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radio2.setChecked(false);
                video_type = 2;
            }
        });
        binding.radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radio1.setChecked(false);
                video_type = 1;
            }
        });

        binding.radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radio4.setChecked(false);
                binding.editText.setHint("Название файла: пример RS.mp4");
            }
        });

        binding.radio4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radio3.setChecked(false);
                binding.editText.setHint("URL-адрес: http://google.com/RS.mp4");
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.continues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.editText.getText().toString().equals("")) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/devices/");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                DatabaseReference childRef = child.getRef();
                                childRef.child("url").setValue(binding.editText.getText().toString());
                                childRef.child("videoType").setValue(video_type);
                            }
                            Toast.makeText(getApplicationContext(), "Запрос на запуск видео на всех девайсах!", Toast.LENGTH_SHORT).show();
                            SetAllVideoActivity.this.finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Пустая ссылка!", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}