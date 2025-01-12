package br.inatel.dm111promo.persistence.user;

//{
//    "id": "",
//    "name": "",
//    "email": "",
//    "password": "",
//    "role": "ADMIN|CLIENT"
//}
public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private String role;

    public User() {
    }

    public User(String id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

}
