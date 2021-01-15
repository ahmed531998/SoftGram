package table_chooser;

import java.util.Date;

public class Userdata {
    private String username;
    private String country;
    private String password;
    private String email;
    private String role;
    private String website;

    public Userdata(String username,  String country, String password, String email, String role, String website) {
        this.username = username;
        this.country = country;
        this.password = password;
        this.email = email;
        this.role = role;
        this.website = website;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
