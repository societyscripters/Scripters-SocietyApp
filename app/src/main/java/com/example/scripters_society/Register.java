package com.example.scripters_society;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    ProgressBar progressBar;
    EditText etNombre, etEmail, etPassword, etPasswordConfirm;
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        progressBar = findViewById(R.id.progressBarReg);
        progressBar.setVisibility(View.INVISIBLE);

        btnRegister = findViewById(R.id.btn_register);
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(etNombre.getText()).isEmpty()
                        || String.valueOf(etEmail.getText()).isEmpty()
                        || String.valueOf(etPassword.getText()).isEmpty()
                        || String.valueOf(etPasswordConfirm.getText()).isEmpty()) {
                    Toast.makeText(Register.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    if (isValidEmail(String.valueOf(etEmail.getText()))) {
                        if (String.valueOf(etPassword.getText()).equals(String.valueOf(etPasswordConfirm.getText()))) {
                            btnRegister.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            registrarUsuario();
                        } else {
                            Toast.makeText(Register.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, "Ingrese un email valido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void registrarUsuario() {
        final String url = "https://redsocial.balinsa.com/api/register";
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("message")){
                        String message = jsonResponse.getString("message");
                        Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception ex) {
                    btnRegister.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Register.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                btnRegister.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Register.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", String.valueOf(etNombre.getText()));
                params.put("email", String.valueOf(etEmail.getText()));
                params.put("password", String.valueOf(etPassword.getText()));
                params.put("password_confirmation", String.valueOf(etPasswordConfirm.getText()));
                return params;
            }
        };

        RequestQueue reqQueue = Volley.newRequestQueue(this);
        reqQueue.add(req);
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}