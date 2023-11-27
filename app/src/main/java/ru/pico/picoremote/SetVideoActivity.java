package ru.pico.picoremote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.pico.picoremote.databinding.ActivitySetVideoBinding;

public class SetVideoActivity extends AppCompatActivity {

    ActivitySetVideoBinding binding;
    int video_type = 2;
    int my_id;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        binding = ActivitySetVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance().getReference();


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            my_id = extras.getInt("device_id");
            // and get whatever type user account id is
        }

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
                    database.child("devices").child("" + my_id).child("url").setValue(binding.editText.getText().toString());
                    database.child("devices").child("" + my_id).child("videoType").setValue(video_type).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Запрос на запуск видео!", Toast.LENGTH_SHORT).show();
                                SetVideoActivity.this.finish();
                            }
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