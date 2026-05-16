package session;

public class LoginSession {

    private static int userId;

    private static String nama;

    private static String role;

    /*
     * =========================
     * LOGIN SESSION
     * =========================
     */
    public static void login(

            int id,
            String namaUser,
            String roleUser

    ) {

        userId = id;

        nama = namaUser;

        role = roleUser;
    }

    /*
     * =========================
     * GET USER ID
     * =========================
     */
    public static int getUserId() {

        return userId;
    }

    /*
     * =========================
     * GET NAMA
     * =========================
     */
    public static String getNama() {

        return nama;
    }

    /*
     * =========================
     * GET ROLE
     * =========================
     */
    public static String getRole() {

        return role;
    }

    /*
     * =========================
     * LOGOUT
     * =========================
     */
    public static void logout() {

        userId = 0;

        nama = null;

        role = null;
    }
}