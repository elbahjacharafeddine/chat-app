package com.javapapers.android.elbahjaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.javapapers.android.elbahjaapplication.databinding.ActivityMainBinding;
import com.javapapers.android.elbahjaapplication.model.User;

public class SignUpActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Button signup;
    EditText inputName,inputEmail,inputPassword,inputMobile;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_sign_up);

       progressDialog = new ProgressDialog(SignUpActivity.this);

                // findViewById
        signup = findViewById(R.id.signUpButton);


                // setOnClickListener
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"good",Toast.LENGTH_SHORT).show();
                 inputName = findViewById(R.id.username);
                 inputEmail = findViewById(R.id.email);
                 inputMobile = findViewById(R.id.phone);
                 inputPassword = findViewById(R.id.password);

                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Wait while loading...");
                progressDialog.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progressDialog.show();
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseFirestore = FirebaseFirestore.getInstance();

                firebaseAuth.createUserWithEmailAndPassword(inputEmail.getText().toString().trim(),inputPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.cancel();
                                firebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getUid())
                                        .set(new User(inputName.getText().toString(),inputMobile.getText().toString(),inputPassword.getText().toString(),inputEmail.getText().toString().trim()));

                                FirebaseDatabase.getInstance().getReference("users")
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .setValue(new User(inputName.getText().toString(),inputMobile.getText().toString(),inputPassword.getText().toString(),inputEmail.getText().toString().trim()))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(),"good",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                                finish();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                  progressDialog.cancel();
                            }
                        });
            }
        });
    }
}