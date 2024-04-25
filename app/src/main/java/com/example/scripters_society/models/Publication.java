package com.example.scripters_society.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Publication implements Serializable {
    private int id;
    private int user_id;
    private String name;
    private String description;
    private String pathImage;
    private String updated_at;
    private ArrayList<Object> comments;

    public Publication(
            int id,
            int user_id,
            String name,
            String description,
            String pathImage,
            String updated_at
    ) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.description = description;
        this.pathImage = pathImage;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public ArrayList<Object> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Object> comments) {
        this.comments = comments;
    }
}
