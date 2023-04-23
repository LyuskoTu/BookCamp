package com.turquoise.hotelbookrecomendation.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.turquoise.hotelbookrecomendation.R;

import static com.turquoise.hotelbookrecomendation.Utils.Utilities.newActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login;
    private TextInputLayout username;
    private TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.usernameLayout);
        username.getEditText().addTextChangedListener(usernameTextWatcher);
        password = findViewById(R.id.passwordLayout);
        password.getEditText().addTextChangedListener(passwordTextWatcher);
        login = findViewById(R.id.loginBtn);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginBtn) {
            if (getUsername().length() < 6) {
                setError(username, "Потребителското име трябва да е с поне 6 символа");
            } else if (getPassword().length() < 6) {
                setError(password, "Паролата трябва да е с поне 6 символа");
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences(getUsername(), MODE_PRIVATE);
                SharedPreferences.Editor editor = getSharedPreferences(getUsername(), MODE_PRIVATE).edit();
                if (sharedPreferences.getString("active", "in").equals("in")) {
                    SharedPreferences.Editor edit = getSharedPreferences("cur", MODE_PRIVATE).edit();
                    edit.putString("user", getUsername());
                }
                newActivity(LoginActivity.this, MainActivity.class);
            }
        }
    }

    private void setError(@NonNull TextInputLayout inputLayout, String message) {
        inputLayout.setError(message);
    }

    private String getUsername() {
        return username.getEditText().getText().toString().trim();
    }

    private String getPassword() {
        return password.getEditText().getText().toString().trim();
    }

    private TextWatcher usernameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() < 6) {
                setError(username, "Потребителското име трябва да е с поне 6 символа");
            } else {
                setError(username, null);
            }
        }
    };

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() < 6) {
                setError(password, "Паролата трябва да е с поне 6 символа");
            } else {
                setError(password, null);
            }
        }
    };
}
