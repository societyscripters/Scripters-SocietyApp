package com.example.scripters_society;

public class UserLoged {
    private String jwtToken;
    private int id;
    private String name;
    private String email;
    private String email_verified_at;
    private String created_at;
    private String updated_at;
    private String pathPhotoProfile;

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

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
