package com.s23010222.mohomad;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText usernameEditText, passwordEditText;
    private MaterialButton loginButton;
    private RelativeLayout loadingLayout;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        loadingLayout = findViewById(R.id.loadingLayout);
        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText() != null ? usernameEditText.getText().toString().trim() : "";
            String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "❗Invalid User Inputs", Toast.LENGTH_SHORT).show();
            } else {
                boolean inserted = dbHelper.insertUser(username, password);
                if (inserted) {
                    Toast.makeText(LoginActivity.this, "✅ User Login Saved to Database!", Toast.LENGTH_SHORT).show();
                    loadingLayout.setVisibility(View.VISIBLE);
                    loginButton.setEnabled(false);
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                        startActivity(intent);
                        finish();
                    }, 4000);
                } else {
                    Toast.makeText(LoginActivity.this, "❌ Incorrect User Login Please Try Again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
