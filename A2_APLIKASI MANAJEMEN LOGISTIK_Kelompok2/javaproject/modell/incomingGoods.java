package modell;
import java.util.Objects;

public class incomingGoods {
    private int idIngoods;
    private int idSupplier;
    private int idWarehouse;
    private int idUser;
    private String invoiceCode;
    private String status;


    public incomingGoods() {
    }

    public incomingGoods(int idIngoods, int idSupplier, int idWarehouse, int idUser, String invoiceCode, String status) {
        this.idIngoods = idIngoods;
        this.idSupplier = idSupplier;
        this.idWarehouse = idWarehouse;
        this.idUser = idUser;
        this.invoiceCode = invoiceCode;
        this.status = status;
    }

    public int getIdIngoods() {
        return this.idIngoods;
    }

    public void setIdIngoods(int idIngoods) {
        this.idIngoods = idIngoods;
    }

    public int getIdSupplier() {
        return this.idSupplier;
    }

    public void setIdSupplier(int idSupplier) {
        this.idSupplier = idSupplier;
    }

    public int getIdWarehouse() {
        return this.idWarehouse;
    }

    public void setIdWarehouse(int idWarehouse) {
        this.idWarehouse = idWarehouse;
    }

    public int getIdUser() {
        return this.idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getInvoiceCode() {
        return this.invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public incomingGoods idIngoods(int idIngoods) {
        setIdIngoods(idIngoods);
        return this;
    }

    public incomingGoods idSupplier(int idSupplier) {
        setIdSupplier(idSupplier);
        return this;
    }

    public incomingGoods idWarehouse(int idWarehouse) {
        setIdWarehouse(idWarehouse);
        return this;
    }

    public incomingGoods idUser(int idUser) {
        setIdUser(idUser);
        return this;
    }

    public incomingGoods invoiceCode(String invoiceCode) {
        setInvoiceCode(invoiceCode);
        return this;
    }

    public incomingGoods status(String status) {
        setStatus(status);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof incomingGoods)) {
            return false;
        }
        incomingGoods incomingGoods = (incomingGoods) o;
        return idIngoods == incomingGoods.idIngoods && idSupplier == incomingGoods.idSupplier && idWarehouse == incomingGoods.idWarehouse && idUser == incomingGoods.idUser && Objects.equals(invoiceCode, incomingGoods.invoiceCode) && Objects.equals(status, incomingGoods.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIngoods, idSupplier, idWarehouse, idUser, invoiceCode, status);
    }

    @Override
    public String toString() {
        return "{" +
            " idIngoods='" + getIdIngoods() + "'" +
            ", idSupplier='" + getIdSupplier() + "'" +
            ", idWarehouse='" + getIdWarehouse() + "'" +
            ", idUser='" + getIdUser() + "'" +
            ", invoiceCode='" + getInvoiceCode() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
    
}
