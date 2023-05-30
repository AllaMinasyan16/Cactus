package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        progressDialog=new ProgressDialog(this);

        binding.SignUp.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {
               String name = binding.name.getText().toString();
               String email = binding.editTextTextEmailAddress.getText().toString();
               String password = binding.editTextNumberPasswordSignUp.getText().toString();
               String repeatPassword = binding.editTextNumberPasswordRepeat.getText().toString();

               progressDialog.show();
               firebaseAuth.createUserWithEmailAndPassword(email, password)
                       .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                           @Override
                           public void onSuccess(AuthResult authResult) {
                               startActivity(new Intent(SignUp.this, login.class));
                               progressDialog.cancel();

                               firebaseFirestore.collection("User")
                                       .document(FirebaseAuth.getInstance().getUid())
                                       .set(new UserModel(name, email));

                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                               progressDialog.cancel();
                           }
                       });
               FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
               FirebaseUser user = firebaseAuth.getCurrentUser();
               View main = findViewById(R.id.SignUp);

               if (user != null) {
                   user.sendEmailVerification()
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {
                                       Snackbar.make(main, "Письмо для подтверждения отправлено на " + user.getEmail(), Snackbar.LENGTH_SHORT).show();
                                   } else {
                                       Snackbar.make(main, "Ошибка отправки письма для подтверждения " + task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                   }
                               }
                           });

               }
           }
        });

    }
    }