package com.example.scripters_society;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.text.TextUtils;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scripters_society.models.UserLoged;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    public static UserLoged usuarioLogeado;
    ProgressBar progressBar;
    EditText email, password;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.etEmailLog);
        password = findViewById(R.id.etPasswordLog);
        btnLogin = findViewById(R.id.btnLogin);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCredenciales()) {
                    if (email.getText().toString().isEmpty()){
                        Toast.makeText(Login.this, "Ingrese un email", Toast.LENGTH_SHORT).show();
                    } else if (!isValidEmail(email.getText().toString())) {
                        Toast.makeText(Login.this, "Ingrese un email valido", Toast.LENGTH_SHORT).show();
                    } else if (password.getText().toString().isEmpty()) {
                        Toast.makeText(Login.this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        btnLogin.setVisibility(View.INVISIBLE);
                        logearUsuario();
                    }
                } else {
                    Toast.makeText(Login.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void logearUsuario() {
        String url = "https://redsocial.balinsa.com/api/login";
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("status")){
                        if (jsonResponse.getBoolean("status")){
                            String message = jsonResponse.getString("message");
                            usuarioLogeado = new UserLoged(jsonResponse.getString("token"));
                            getProfileUserLoged(new UserCallBack() {
                                @Override
                                public void onUserLoaded(Object[] data) {
                                    usuarioLogeado.setId(Integer.parseInt(String.valueOf(data[0])));
                                    usuarioLogeado.setName(String.valueOf(data[1]));
                                    usuarioLogeado.setEmail(String.valueOf(data[2]));
                                    usuarioLogeado.setEmail_verified_at(String.valueOf(data[3]));
                                    usuarioLogeado.setCreated_at(String.valueOf(data[4]));
                                    usuarioLogeado.setUpdated_at(String.valueOf(data[5]));

                                    Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                                    showHomeView();
                                }

                                @Override
                                public void onError(String mensajeError) {
                                    btnLogin.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(Login.this, mensajeError, Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            btnLogin.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            String message = jsonResponse.getString("message");
                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception ex) {
                    btnLogin.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                btnLogin.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Login.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email", String.valueOf(email.getText()));
                params.put("password", String.valueOf(password.getText()));

                return params;
            }
        };

        RequestQueue reqQueue = Volley.newRequestQueue(this);
        reqQueue.add(req);
    }

    private void getProfileUserLoged(UserCallBack callBack) {
        Object[] dataUser = new Object[6];
        String url = "https://redsocial.balinsa.com/api/profile";
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("status")){
                        if (jsonResponse.getBoolean("status")){
                            JSONObject data = jsonResponse.getJSONObject("data");
                            dataUser[0] = data.getInt("id");
                            dataUser[1] = data.getString("name");
                            dataUser[2] = data.getString("email");
                            dataUser[3] = data.getString("email_verified_at");
                            dataUser[4] = data.getString("created_at");
                            dataUser[5] = data.getString("updated_at");
                        } else {
                            String message = jsonResponse.getString("message");
                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(Login.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                callBack.onUserLoaded(dataUser);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Login.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer" + usuarioLogeado.getJwtToken());
                return headers;
            }
        };

        RequestQueue reqQueue = Volley.newRequestQueue(this);
        reqQueue.add(req);
//        return dataUser;
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean validarCredenciales() {
        String textEmail = email.getText().toString();
        String pass = email.getText().toString();
        return !textEmail.equals("") && !pass.equals("");
    }

    private void showHomeView() {
        Intent intent = new Intent(Login.this, activity_principal.class);
        startActivity(intent);
        finish();
    }
}