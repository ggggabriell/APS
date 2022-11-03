package com.example.apstalvez.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.apstalvez.databinding.ActivityRegisterBinding;
import com.example.apstalvez.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.btnContinue.setOnClickListener(view -> {
            verifyData();
        });
    }

    private void verifyData(){
        String fullName = binding.etName.getText().toString();
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();
        String passwordConfirm = binding.etPasswordConfirm.getText().toString();

        if(!fullName.isEmpty()){
            if(!email.isEmpty()){
                if(!password.isEmpty()){
                    if(!passwordConfirm.isEmpty()){
                        if(password.equals(passwordConfirm)){

                            User user = new User();
                            user.setFullName(fullName);
                            user.setEmail(email);
                            user.setPassword(password);
                            registerUser(user);

                        }else{
                            binding.edtPassword.setError("Senhas diferentes");
                            binding.edtPasswordConfirm.setError("Senhas diferentes");
                        }

                    }else{
                        binding.edtPasswordConfirm.requestFocus();
                        binding.edtPasswordConfirm.setError("Confirme a sua senha.");
                    }

                }else{
                    binding.edtPassword.requestFocus();
                    binding.edtPassword.setError("Informe a sua senha.");
                }

            }else{
                binding.edtEmail.requestFocus();
                binding.edtEmail.setError("Informe o seu email.");
            }

        }else{
            binding.edtName.requestFocus();
            binding.edtName.setError("Informe o seu nome.");
        }
    }

    private void registerUser(User user){
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser userFirebase = mAuth.getCurrentUser();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this, "Auth failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}