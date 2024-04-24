package com.example.scripters_society;

public class UserLoged {
    private String jwtToken;
    private String name;
    private String email;
    private String pathPhotoProfile;

    public UserLoged(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
