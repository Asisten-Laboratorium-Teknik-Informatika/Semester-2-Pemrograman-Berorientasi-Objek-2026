package entity;

public abstract class User {

    protected int userId;
    protected String nama;
    protected String email;
    protected String password;
    protected String phone;

    public User(
            int userId,
            String nama,
            String email,
            String password,
            String phone
    ) {

        this.userId = userId;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public int getUserId() {
        return userId;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public abstract void dashboard();
}