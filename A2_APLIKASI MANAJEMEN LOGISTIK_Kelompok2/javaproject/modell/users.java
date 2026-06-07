package modell;
import java.util.Objects;

public class users {
    private int idUser;
    private int idRoles;
    private String username;
    private String fullname;
    private String password;
    private String email;


    public users() {
    }

    public users(int idUser, int idRoles, String username, String fullname, String password, String email) {
        this.idUser = idUser;
        this.idRoles = idRoles;
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.email = email;
    }

    public int getIdUser() {
        return this.idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdRoles() {
        return this.idRoles;
    }

    public void setIdRoles(int idRoles) {
        this.idRoles = idRoles;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public users idUser(int idUser) {
        setIdUser(idUser);
        return this;
    }

    public users idRoles(int idRoles) {
        setIdRoles(idRoles);
        return this;
    }

    public users username(String username) {
        setUsername(username);
        return this;
    }

    public users fullname(String fullname) {
        setFullname(fullname);
        return this;
    }

    public users password(String password) {
        setPassword(password);
        return this;
    }

    public users email(String email) {
        setEmail(email);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof users)) {
            return false;
        }
        users users = (users) o;
        return idUser == users.idUser && idRoles == users.idRoles && Objects.equals(username, users.username) && Objects.equals(fullname, users.fullname) && Objects.equals(password, users.password) && Objects.equals(email, users.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idRoles, username, fullname, password, email);
    }

    @Override
    public String toString() {
        return "{" +
            " idUser='" + getIdUser() + "'" +
            ", idRoles='" + getIdRoles() + "'" +
            ", username='" + getUsername() + "'" +
            ", fullname='" + getFullname() + "'" +
            ", password='" + getPassword() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
    
}
