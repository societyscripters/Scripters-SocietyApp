package com.example.scripters_society;

import static com.example.scripters_society.Login.usuarioLogeado;

import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scripters_society.models.AdapterPublications;
import com.example.scripters_society.models.Publication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    TextView tvNombreUsuario, tvEmailUsuario, tvEstadoUsuario, tvNoHayPublicaciones;
    EditText etEstado;
    Button btnCambiar, btnEditarEstado, btnCerrarSesion, btnSiCloseSession, btnNoCloseSession;
    ImageButton imgBtnClose_EditProfileDialog, imgBtnClose_CloseSessionDialog;
    private ListView lvPublicaciones;
    private AdapterPublications adapterPublications;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        View editProfileDialog = LayoutInflater.from(this.getContext()).inflate(R.layout.edit_profile_dialog,null);
        View closeSesionDialog = LayoutInflater.from(this.getContext()).inflate(R.layout.confirm_closesession_dialog, null);

        AlertDialog.Builder dialog_close_sesion = new AlertDialog.Builder(this.getContext());
        AlertDialog.Builder dialog_change_status = new AlertDialog.Builder(this.getContext());
        dialog_change_status.setView(editProfileDialog);
        dialog_close_sesion.setView(closeSesionDialog);

        lvPublicaciones = (ListView) view.findViewById(R.id.lvPublicacionesUserLoged);
        tvNoHayPublicaciones = (TextView) view.findViewById(R.id.tvNoPublicationsUserLoged);

        imgBtnClose_EditProfileDialog = (ImageButton) editProfileDialog.findViewById(R.id.imgBtnClose_EditProfileDialog);
        btnCambiar = (Button) editProfileDialog.findViewById(R.id.btnCambiar);
        etEstado = (EditText) editProfileDialog.findViewById(R.id.etNuevoEstado);

        imgBtnClose_CloseSessionDialog = (ImageButton) closeSesionDialog.findViewById(R.id.imgBtnClose_CloseSessionDialog);
        btnSiCloseSession = (Button) closeSesionDialog.findViewById(R.id.btnSiCloseSession);
        btnNoCloseSession = (Button) closeSesionDialog.findViewById(R.id.btnNoCloseSession);

        final AlertDialog alertDialog_change_status = dialog_change_status.create();
        final AlertDialog alertDialog_close_sesion = dialog_close_sesion.create();

        btnEditarEstado = view.findViewById(R.id.btnEditarEstado);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        tvNombreUsuario = view.findViewById(R.id.tvNameUser);
        tvEmailUsuario = view.findViewById(R.id.tvEmailUser);
        tvEstadoUsuario = view.findViewById(R.id.tvStatusUser);

        tvNombreUsuario.setText(usuarioLogeado.getName());
        tvEmailUsuario.setText(usuarioLogeado.getEmail());
        tvEstadoUsuario.setText('"'+usuarioLogeado.getStatus()+'"');
        etEstado.setText(usuarioLogeado.getStatus());

        listarPublicacionesUsuario(view);
        btnEditarEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_change_status.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog_change_status.show();
            }
        });
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_close_sesion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog_close_sesion.show();
            }
        });

        imgBtnClose_EditProfileDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_change_status.cancel();
            }
        });
        imgBtnClose_CloseSessionDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_close_sesion.cancel();
            }
        });

        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarEstadoPerfil(alertDialog_change_status);
            }
        });

        btnSiCloseSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion(alertDialog_close_sesion);
            }
        });
        btnNoCloseSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_close_sesion.cancel();
            }
        });

        return view;
    }

    private void listarPublicacionesUsuario(View viewFragment) {
        try {
            ArrayList<Publication> publications = new ArrayList<>();
            JSONArray jsonPublications = usuarioLogeado.getPosts();
            for (int i = 0; i < jsonPublications.length(); i++) {
                JSONObject pub = jsonPublications.getJSONObject(i);
                Publication publication = new Publication(pub.getInt("id"),
                        pub.getInt("user_id"), pub.getString("name"),
                        pub.getString("description"), pub.getString("image"),
                        pub.getString("updated_at"));
                publications.add(publication);
            }
            if (publications.isEmpty()) {
                tvNoHayPublicaciones.setVisibility(View.VISIBLE);
            } else {
                tvNoHayPublicaciones.setVisibility(View.INVISIBLE);
                adapterPublications = new AdapterPublications(viewFragment.getContext(), publications);
                lvPublicaciones.setAdapter(adapterPublications);
            }
        } catch (JSONException ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void actualizarEstadoPerfil(AlertDialog alertDialog) {
        String url = "https://redsocial.balinsa.com/api/profile/edit";
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("status")) {
                        cambiarEstado(etEstado.getText().toString());
                        alertDialog.cancel();
                        Toast.makeText(getContext(), jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getContext(), "error en el response", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("estado", String.valueOf(etEstado.getText()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer" + usuarioLogeado.getJwtToken());
                return headers;
            }
        };

        RequestQueue reqQueue = Volley.newRequestQueue(this.requireContext());
        reqQueue.add(req);
    }

    private void cerrarSesion(AlertDialog alertDialog) {
        String url = "https://redsocial.balinsa.com/api/logout";
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("status")) {
                        showMainActivity();
                        Toast.makeText(getContext(), jsonResponse.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    alertDialog.cancel();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer" + usuarioLogeado.getJwtToken());
                return headers;
            }
        };

        RequestQueue reqQueue = Volley.newRequestQueue(this.requireContext());
        reqQueue.add(req);
    }

    private void showMainActivity() {
        Intent intent = new Intent(this.getContext(), activity_principal.class);
        startActivity(intent);
        this.requireActivity().finish();
    }

    private void cambiarEstado(String nuevoEstado) {
        usuarioLogeado.setStatus(nuevoEstado);
        tvEstadoUsuario.setText('"'+usuarioLogeado.getStatus()+'"');
    }
}