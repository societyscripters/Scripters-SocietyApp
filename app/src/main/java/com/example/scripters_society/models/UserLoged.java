package com.example.scripters_society.models;

public class UserLoged {
    private String jwtToken;
    private int id;
    private String name;
    private String email;
    private String email_verified_at;
    private String created_at;
    private String updated_at;
    private String pathPhotoProfile;
    private String status;

    public UserLoged(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public UserLoged(String jwtToken, int id, String name, String email, String email_verified_at, String created_at, String updated_at) {
        this.jwtToken = jwtToken;
        this.id = id;
        this.name = name;
        this.email = email;
        this.email_verified_at = email_verified_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.pathPhotoProfile = pathPhotoProfile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPathPhotoProfile() {
        return pathPhotoProfile;
    }

    public void setPathPhotoProfile(String pathPhotoProfile) {
        this.pathPhotoProfile = pathPhotoProfile;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
