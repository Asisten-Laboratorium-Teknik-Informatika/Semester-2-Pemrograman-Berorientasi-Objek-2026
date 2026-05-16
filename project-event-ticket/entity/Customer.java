package entity;

public class Customer extends User {

    public Customer(
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
                "Dashboard Customer"
        );
    }
}