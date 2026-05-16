package service;

import repository.AdminRepository;

public class AdminService {

    AdminRepository repo =
            new AdminRepository();

    /*
     * =========================
     * TAMPIL REFUND
     * =========================
     */
    public void tampilRefund() {

        repo.tampilRefund();
    }

    /*
     * =========================
     * APPROVE REFUND
     * =========================
     */
    public boolean approveRefund(
            int refundId,
            int adminId
    ) {

        return repo.approveRefund(
                refundId,
                adminId
        );
    }

    /*
     * =========================
     * REJECT REFUND
     * =========================
     */
    public boolean rejectRefund(
            int refundId,
            String alasan,
            int adminId
    ) {

        return repo.rejectRefund(
                refundId,
                alasan,
                adminId
        );
    }

    /*
     * =========================
     * DATA USER
     * =========================
     */
    public void tampilUser() {

        repo.tampilUser();
    }

    /*
     * =========================
     * DATA ORGANIZER
     * =========================
     */
    public void tampilOrganizer() {

        repo.tampilOrganizer();
    }

    /*
     * =========================
     * MONITORING PAYMENT
     * =========================
     */
    public void tampilMonitoringPayment() {

        repo.tampilMonitoringPayment();
    }
}