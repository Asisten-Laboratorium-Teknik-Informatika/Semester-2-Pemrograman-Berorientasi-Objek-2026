package entity;

public class Organizer extends User {

    private String organizationName;

    public Organizer(
            int userId,
            String nama,
            String email,
            String password,
            String phone,
            String organizationName
    ) {

        super(
                userId,
                nama,
                email,
                password,
                phone
        );

        this.organizationName =
                organizationName;
    }

    public String getOrganizationName() {

        return organizationName;
    }

    @Override
    public void dashboard() {

        System.out.println(
                "Dashboard Organizer"
        );
    }
}