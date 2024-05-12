package com.example.scripters_society;

import static com.example.scripters_society.Login.usuarioLogeado;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scripters_society.models.DataPart;
import com.example.scripters_society.requests_personalizados.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class publicar extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;
    EditText etDescripcionPublication;
    private Button btnPublicar;
    ProgressBar progressBarPublicando;
    Bitmap imgBitmap;
    Uri path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_publicar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView imageButton = findViewById(R.id.btncamera);

        btnPublicar = findViewById(R.id.btnPublicar);
        progressBarPublicando = findViewById(R.id.progressBarPublicando);
        progressBarPublicando.setVisibility(View.INVISIBLE);
        etDescripcionPublication = findViewById(R.id.etDescripcionPublication);
        // Establece un OnClickListener para el botón de la imagen
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama al método para iniciar la captura de imagen
                dispatchTakePictureIntent();
            }
        });

        this.btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPublicar.setVisibility(View.INVISIBLE);
                progressBarPublicando.setVisibility(View.VISIBLE);
                publicar();
            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Verifica si hay una aplicación de cámara disponible para manejar el intent
        startActivityIfNeeded(intent, 1);

    }

    public void onclick(View view) {
        cargarImagen();
    }
    private void  cargarImagen(){
        Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"seleccione la aplicacion"),10);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK) {
            path = data.getData();
            try {
                imgBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                ImageView imageView = findViewById(R.id.imgpublic);
                imageView.setImageURI(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Imprime un mensaje en la consola

        }else if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imgBitmap = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(R.id.imgpublic);
            imageView.setImageBitmap(imgBitmap);
        }
    }

    private void publicar(){
        String url = "https://redsocial.balinsa.com/api/post";
        Map<String, String> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        Map<String, DataPart> dataPartParam = new HashMap<>();
        headers.put("Authorization", "Bearer" + usuarioLogeado.getJwtToken());
        params.put("description",String.valueOf(etDescripcionPublication.getText()));
        dataPartParam.put("image", new DataPart("publicacion.jpg", convertirImagenABytes(imgBitmap), "image/jpeg"));
        VolleyMultipartRequest multiReq = new VolleyMultipartRequest(Request.Method.POST, url, headers, params, dataPartParam, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has("status")) {
                    Toast.makeText(getApplicationContext(), "¡Publicado con éxito!", Toast.LENGTH_LONG).show();
                    showHomeView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                btnPublicar.setVisibility(View.VISIBLE);
                progressBarPublicando.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(multiReq);
    }

    public byte[] convertirImagenABytes(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public String convertirUriABitmap(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byte_arr = stream.toByteArray();
            String imagenString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            return imagenString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showHomeView() {
        Intent intent = new Intent(getApplicationContext(), activity_principal.class);
        startActivity(intent);
        finish();
    }
}