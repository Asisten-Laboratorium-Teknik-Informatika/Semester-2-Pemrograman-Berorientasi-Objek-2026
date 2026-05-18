package model;
import java.util.Scanner;

public abstract class User {
    protected int idAkun;
    protected String nama, email;
    protected Scanner scanner = new Scanner(System.in);

    public User(int idAkun, String nama, String email) {
        this.idAkun = idAkun;
        this.nama = nama;
        this.email = email;
    }

    public abstract boolean tampilkanMenu();

}