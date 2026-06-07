package Fitur;

public class Session {

    private static String username;
    private static String role;
    private static String nama;
    private static String nim;
    private static String nidn;

    public static String getUsername() { return username; }
    public static String getRole()     { return role; }
    public static String getNama()     { return nama; }
    public static String getNim()      { return nim; }
    public static String getNidn()     { return nidn; }

    public static void setUsername(String username) { Session.username = username; }
    public static void setRole(String role)         { Session.role = role; }
    public static void setNama(String nama)         { Session.nama = nama; }
    public static void setNim(String nim)           { Session.nim = nim; }
    public static void setNidn(String nidn)         { Session.nidn = nidn; }

    public static boolean isLoggedIn() {
        return username != null && !username.isEmpty();
    }

    public static void clear() {
        username = null;
        role     = null;
        nama     = null;
        nim      = null;
        nidn     = null;
    }
}