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

import com.turquoise.hotelbookrecomendation.model.User;

import static com.turquoise.hotelbookrecomendation.Utils.Utilities.newActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login;
    private TextInputLayout username;
    private TextInputLayout password;

    private User user;


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
        String enteredUsername = getUsername();
        String enteredPassword = getPassword();

        User user = getUserFromJson(enteredUsername, enteredPassword);

        if (user == null) {
            setError(username, "Невалидно потребителско име или парола");
        } else {
            // Потребителското име и паролата са валидни
            newActivity(LoginActivity.this, MainActivity.class);
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

    private User getUserFromJson(String username, String password) {
        User user = null;

        try {
            InputStream is = getAssets().open("users.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray usersArray = jsonObject.getJSONArray("users");

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                String jsonUsername = userObject.getString("username");
                String jsonPassword = userObject.getString("password");

                if (jsonUsername.equals(username) && jsonPassword.equals(password)) {
                    user = new User(jsonUsername, jsonPassword);
                    break;
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

}
