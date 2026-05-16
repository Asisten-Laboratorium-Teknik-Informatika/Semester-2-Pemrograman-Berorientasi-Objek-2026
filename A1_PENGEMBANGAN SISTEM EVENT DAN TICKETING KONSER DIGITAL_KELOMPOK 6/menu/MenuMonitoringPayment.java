package menu;

import service.AdminService;

public class MenuMonitoringPayment {

    AdminService service =
            new AdminService();

    public void tampilMenu() {

        service.tampilMonitoringPayment();
    }
}