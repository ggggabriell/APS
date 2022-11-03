package com.example.apstalvez.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.apstalvez.R;
import com.example.apstalvez.databinding.ActivityLoginBinding;
import com.example.apstalvez.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.tvCreateAccount.setOnClickListener(view -> {startActivity(new Intent(this, RegisterActivity.class)); finish();});

        binding.btnLogin.setOnClickListener(view -> {verifyData();});
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void verifyData(){
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        if(!email.isEmpty()){
            if(!password.isEmpty()){

                User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                loginUser(user);

            }else{
                binding.edtPassword.requestFocus();
                binding.edtPassword.setError("Informe a sua senha.");
            }
        }else{
            binding.edtEmail.requestFocus();
            binding.edtEmail.setError("Informe o seu email");
        }
    }

    private void loginUser(User user){
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Logado com sucesso.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "Erro ao logar.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}