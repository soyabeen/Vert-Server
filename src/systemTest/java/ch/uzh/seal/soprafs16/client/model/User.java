package ch.uzh.seal.soprafs16.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oracle.webservices.internal.api.databinding.DatabindingMode;

/**
 * Created by soyabeen on 31.03.16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private Long id;
    private String name;
    private String username;
    private String token;
    private String status;

    public User() {
    }

    public User(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public User(Long id, String name, String username, String token, String status) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.token = token;
        this.status = status;
    }

//    @Override
//    public String toString() {
//        return "User{" +
//                "name='" + name + '\'' +
//                ", username='" + username +
//                "'}";
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
