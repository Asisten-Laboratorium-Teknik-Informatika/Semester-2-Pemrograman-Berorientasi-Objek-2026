package entity;

public class Admin extends User {

    public Admin(
            int userId,
            String nama,
            String email,
            String password,
            String phone
    ) {

        super(
                userId,
                nama,
                email,
                password,
                phone
        );
    }

    @Override
    public void dashboard() {

        System.out.println(
                "Dashboard Admin"
        );
    }
}