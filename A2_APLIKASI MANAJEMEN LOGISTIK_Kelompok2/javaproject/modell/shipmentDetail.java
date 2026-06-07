package modell;
import java.util.Objects;

public class shipmentDetail {
    private int idShipmentDetails;
    private int idShipment;
    private int idProduct;
    private int quantity;


    public shipmentDetail() {
    }

    public shipmentDetail(int idShipmentDetails, int idShipment, int idProduct, int quantity) {
        this.idShipmentDetails = idShipmentDetails;
        this.idShipment = idShipment;
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public int getIdShipmentDetails() {
        return this.idShipmentDetails;
    }

    public void setIdShipmentDetails(int idShipmentDetails) {
        this.idShipmentDetails = idShipmentDetails;
    }

    public int getIdShipment() {
        return this.idShipment;
    }

    public void setIdShipment(int idShipment) {
        this.idShipment = idShipment;
    }

    public int getIdProduct() {
        return this.idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public shipmentDetail idShipmentDetails(int idShipmentDetails) {
        setIdShipmentDetails(idShipmentDetails);
        return this;
    }

    public shipmentDetail idShipment(int idShipment) {
        setIdShipment(idShipment);
        return this;
    }

    public shipmentDetail idProduct(int idProduct) {
        setIdProduct(idProduct);
        return this;
    }

    public shipmentDetail quantity(int quantity) {
        setQuantity(quantity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof shipmentDetail)) {
            return false;
        }
        shipmentDetail shipmentDetail = (shipmentDetail) o;
        return idShipmentDetails == shipmentDetail.idShipmentDetails && idShipment == shipmentDetail.idShipment && idProduct == shipmentDetail.idProduct && quantity == shipmentDetail.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idShipmentDetails, idShipment, idProduct, quantity);
    }

    @Override
    public String toString() {
        return "{" +
            " idShipmentDetails='" + getIdShipmentDetails() + "'" +
            ", idShipment='" + getIdShipment() + "'" +
            ", idProduct='" + getIdProduct() + "'" +
            ", quantity='" + getQuantity() + "'" +
            "}";
    }
    
}
