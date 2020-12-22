package com.example.customtooldataapp.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.ui.MainActivity;


public class LoginActivity extends AppCompatActivity {
    private String employeeId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("Employee Identification", MODE_PRIVATE);
        final String empID = sharedPreferences.getString("ID", "");
        if(!empID.equals("")){
            startMainActivity();
        }else {
            getLogin();
        }
    }

    private void getLogin(){
        setContentView(R.layout.activity_login);
        TextView loginText = findViewById(R.id.login_text_view);
        EditText editText = findViewById(R.id.employee_id_login);
        Button signInButton = findViewById(R.id.login_button);

        loginText.setText("Please enter your employee number.");

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employeeId = String.valueOf(editText.getText());
                if (employeeId.length() != 4) {
                    loginText.setText("Incorrect code...");
                }else {
                    SharedPreferences sharedPreferences = getSharedPreferences("Employee Identification", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ID", employeeId);
                    editor.apply();
                    startMainActivity();
                }
            }
        });
        }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}