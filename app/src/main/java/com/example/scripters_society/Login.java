package com.example.scripters_society;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    public static UserLoged usuarioLogeado;
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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logearUsuario();
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
                            getProfileUserLoged(new UserCallBack() {
                                @Override
                                public void onUserLoaded(Object[] data) {
                                    try {
                                        usuarioLogeado = new UserLoged(
                                                jsonResponse.getString("token"),
                                                Integer.parseInt(String.valueOf(data[0])),
                                                String.valueOf(data[1]),
                                                String.valueOf(data[2]),
                                                String.valueOf(data[3]),
                                                String.valueOf(data[4]),
                                                String.valueOf(data[5]));
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                @Override
                                public void onError(String mensajeError) {
                                    Toast.makeText(Login.this, mensajeError, Toast.LENGTH_LONG).show();
                                }
                            });

                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                            showHomeView();
                        } else {
                            String message = jsonResponse.getString("message");
                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(Login.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
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
                            dataUser[0] = jsonResponse.getInt("id");
                            dataUser[1] = jsonResponse.getString("name");
                            dataUser[2] = jsonResponse.getString("email");
                            dataUser[3] = jsonResponse.getString("email_verified_at");
                            dataUser[4] = jsonResponse.getString("created_at");
                            dataUser[5] = jsonResponse.getString("updated_at");
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
                headers.put("Bearer Token", usuarioLogeado.getJwtToken());
                return headers;
            }
        };
//        return dataUser;
    }

    private void showHomeView() {
        Intent intent = new Intent(Login.this, activity_principal.class);
        startActivity(intent);
    }
}