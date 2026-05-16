package menu;

import service.AdminService;

public class MenuDataUser {

    AdminService service =
            new AdminService();

    public void tampilMenu() {

        service.tampilUser();
    }
}