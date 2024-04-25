package com.example.scripters_society.controllers.interfaces;

import com.example.scripters_society.models.Publication;

import java.util.ArrayList;

public interface PublicationCallBack {
    void onPublicationLoaded(ArrayList<Publication> listaPublicaciones);
    void onError(String mensajeError);
}
