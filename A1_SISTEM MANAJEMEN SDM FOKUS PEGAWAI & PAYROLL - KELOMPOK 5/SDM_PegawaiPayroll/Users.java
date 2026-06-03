package SDM_PegawaiPayroll;

public class Users {

    private String idUser;
    private int idPegawai;
    private String username;
    private String userPassword;
    private String role;

    public Users(String idUser, int idPegawai,
            String username,
            String userPassword,
            String role) {

        this.idUser = idUser;
        this.idPegawai = idPegawai;
        this.username = username;
        this.userPassword = userPassword;
        this.role = role;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(int idPegawai) {
        this.idPegawai = idPegawai;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
