package com.example.scripters_society;

import static com.example.scripters_society.Login.usuarioLogeado;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    TextView tvNombreUsuario, tvEmailUsuario, tvEstadoUsuario;
    EditText etEstado;
    Button btnCambiar, btnEditarEstado;
    ImageButton imgBtnClose;

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
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
        dialog.setView(editProfileDialog);

        imgBtnClose = (ImageButton) editProfileDialog.findViewById(R.id.imgBtnClose);
        btnCambiar = (Button) editProfileDialog.findViewById(R.id.btnCambiar);
        etEstado = (EditText) editProfileDialog.findViewById(R.id.etNuevoEstado);
        final AlertDialog alertDialog = dialog.create();

        btnEditarEstado = view.findViewById(R.id.btnEditarEstado);

        tvNombreUsuario = view.findViewById(R.id.tvNameUser);
        tvEmailUsuario = view.findViewById(R.id.tvEmailUser);
        tvEstadoUsuario = view.findViewById(R.id.tvStatusUser);

        tvNombreUsuario.setText(usuarioLogeado.getName());
        tvEmailUsuario.setText(usuarioLogeado.getEmail());
        tvEstadoUsuario.setText('"'+usuarioLogeado.getStatus()+'"');
        etEstado.setText(usuarioLogeado.getStatus());

        btnEditarEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarEstado(etEstado.getText().toString());
                alertDialog.cancel();
                Toast.makeText(getContext(), "Estado cambiado", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void cambiarEstado(String nuevoEstado) {
        usuarioLogeado.setStatus(nuevoEstado);
        tvEstadoUsuario.setText('"'+usuarioLogeado.getStatus()+'"');
    }
}