package modell;
import java.util.Objects;

public class outgoingGoods {
    private int idOutgoods;
    private int idCustomer;
    private int idWarehouse;
    private int idUser;
    private String invoiceCode;
    private String destinationAddress;
    private String status;


    public outgoingGoods() {
    }

    public outgoingGoods(int idOutgoods, int idCustomer, int idWarehouse, int idUser, String invoiceCode, String destinationAddress, String status) {
        this.idOutgoods = idOutgoods;
        this.idCustomer = idCustomer;
        this.idWarehouse = idWarehouse;
        this.idUser = idUser;
        this.invoiceCode = invoiceCode;
        this.destinationAddress = destinationAddress;
        this.status = status;
    }

    public int getIdOutgoods() {
        return this.idOutgoods;
    }

    public void setIdOutgoods(int idOutgoods) {
        this.idOutgoods = idOutgoods;
    }

    public int getIdCustomer() {
        return this.idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
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

    public String getDestinationAddress() {
        return this.destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public outgoingGoods idOutgoods(int idOutgoods) {
        setIdOutgoods(idOutgoods);
        return this;
    }

    public outgoingGoods idCustomer(int idCustomer) {
        setIdCustomer(idCustomer);
        return this;
    }

    public outgoingGoods idWarehouse(int idWarehouse) {
        setIdWarehouse(idWarehouse);
        return this;
    }

    public outgoingGoods idUser(int idUser) {
        setIdUser(idUser);
        return this;
    }

    public outgoingGoods invoiceCode(String invoiceCode) {
        setInvoiceCode(invoiceCode);
        return this;
    }

    public outgoingGoods destinationAddress(String destinationAddress) {
        setDestinationAddress(destinationAddress);
        return this;
    }

    public outgoingGoods status(String status) {
        setStatus(status);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof outgoingGoods)) {
            return false;
        }
        outgoingGoods outgoingGoods = (outgoingGoods) o;
        return idOutgoods == outgoingGoods.idOutgoods && idCustomer == outgoingGoods.idCustomer && idWarehouse == outgoingGoods.idWarehouse && idUser == outgoingGoods.idUser && Objects.equals(invoiceCode, outgoingGoods.invoiceCode) && Objects.equals(destinationAddress, outgoingGoods.destinationAddress) && Objects.equals(status, outgoingGoods.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOutgoods, idCustomer, idWarehouse, idUser, invoiceCode, destinationAddress, status);
    }

    @Override
    public String toString() {
        return "{" +
            " idOutgoods='" + getIdOutgoods() + "'" +
            ", idCustomer='" + getIdCustomer() + "'" +
            ", idWarehouse='" + getIdWarehouse() + "'" +
            ", idUser='" + getIdUser() + "'" +
            ", invoiceCode='" + getInvoiceCode() + "'" +
            ", destinationAddress='" + getDestinationAddress() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
    
}
