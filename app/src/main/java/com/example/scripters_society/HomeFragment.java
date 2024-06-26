package com.example.scripters_society;

import static com.example.scripters_society.Login.usuarioLogeado;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scripters_society.controllers.interfaces.PublicationCallBack;
import com.example.scripters_society.models.AdapterPublications;
import com.example.scripters_society.models.Publication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class HomeFragment extends Fragment {

    private View viewFragment;
    private ListView lvPublicaciones;
    private AdapterPublications adapterPublications;
    ProgressBar progressBar;
    TextView tvNoPublications;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listarPublicaciones(new PublicationCallBack() {
            @Override
            public void onPublicationLoaded(ArrayList<Publication> listaPublicaciones) {
                progressBar.setVisibility(View.INVISIBLE);
                adapterPublications = new AdapterPublications(viewFragment.getContext(), listaPublicaciones);
                lvPublicaciones.setVisibility(View.VISIBLE);
                lvPublicaciones.setAdapter(adapterPublications);
                View viewEmpty = LayoutInflater.from(getContext()).inflate(R.layout.item_public_empty, null);
                lvPublicaciones.addFooterView(viewEmpty);
            }

            @Override
            public void onError(String mensajeError) {
                Toast.makeText(viewFragment.getContext(), mensajeError, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewFragment = view;
        progressBar = view.findViewById(R.id.progressBarPub);
        tvNoPublications = view.findViewById(R.id.tvNoPublications);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        lvPublicaciones = view.findViewById(R.id.lvPublicaciones);
        lvPublicaciones.setVisibility(View.INVISIBLE);
        tvNoPublications.setVisibility(View.INVISIBLE);
        return view;
    }

    private void listarPublicaciones(PublicationCallBack callBack) {
        ArrayList<Publication> listPublications = new ArrayList<>();
        String url = "https://redsocial.balinsa.com/api/post";
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("data")) {
                        JSONArray publicaciones = jsonResponse.getJSONArray("data");
                        if (publicaciones.length() > 0) {
                            progressBar.setVisibility(View.INVISIBLE);
                            for (int i = 0; i < publicaciones.length(); i++) {
                                try {
                                    JSONObject jsonPublications = publicaciones.getJSONObject(i);
                                    int id = jsonPublications.getInt("id");
                                    int userId = jsonPublications.getInt("user_id");
                                    String name = jsonPublications.getString("name");
                                    String description = jsonPublications.getString("description");
                                    String image = jsonPublications.getString("image");
                                    String updated_at = jsonPublications.getString("updated_at");
                                    Publication publication = new Publication(id, userId, name, description, image, updated_at);

                                    listPublications.add(publication);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            callBack.onPublicationLoaded(listPublications);
                            System.out.println(publicaciones);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            tvNoPublications.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception ex) {
                    //Toast.makeText(viewFragment.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callBack.onError(volleyError.getMessage().trim());
                Toast.makeText(viewFragment.getContext(), "error en el response", Toast.LENGTH_LONG).show();
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
}