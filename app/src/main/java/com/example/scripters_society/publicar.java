package com.example.scripters_society;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class publicar extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;


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



        // Establece un OnClickListener para el botón de la imagen
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama al método para iniciar la captura de imagen
                dispatchTakePictureIntent();
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
            Uri path = data.getData();
            ImageView imageView = findViewById(R.id.imgpublic);
            imageView.setImageURI(path);
            // Imprime un mensaje en la consola

        }else if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imgBitmap = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(R.id.imgpublic);
            imageView.setImageBitmap(imgBitmap);
        }
    }
}