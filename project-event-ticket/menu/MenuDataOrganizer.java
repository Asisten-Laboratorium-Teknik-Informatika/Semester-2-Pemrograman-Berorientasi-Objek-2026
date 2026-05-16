package menu;

import service.AdminService;

public class MenuDataOrganizer {

    AdminService service =
            new AdminService();

    public void tampilMenu() {

        service.tampilOrganizer();
    }
}