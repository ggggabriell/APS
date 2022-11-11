package com.example.apstalvez.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.apstalvez.databinding.ActivityAccountBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    private ActivityAccountBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String userEmail = getIntent().getStringExtra("email");
        if(userEmail != null){
            binding.tvUserEmail.setText(userEmail);
        }

        binding.btnLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        binding.tvContact.setOnClickListener(view -> startActivity(new Intent(this, InfoActivity.class)));

        binding.cvContact.setOnClickListener(view -> startActivity(new Intent(this, InfoActivity.class)));

    }
}