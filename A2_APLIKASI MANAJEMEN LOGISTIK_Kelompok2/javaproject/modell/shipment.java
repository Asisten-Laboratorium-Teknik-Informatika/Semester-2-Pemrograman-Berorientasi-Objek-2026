package modell;
import java.util.Objects;

public class shipment {
    private int idShipment;
    private int idWarehouse;
    private String destination;
    private String status;


    public shipment() {
    }

    public shipment(int idShipment, int idWarehouse, String destination, String status) {
        this.idShipment = idShipment;
        this.idWarehouse = idWarehouse;
        this.destination = destination;
        this.status = status;
    }

    public int getIdShipment() {
        return this.idShipment;
    }

    public void setIdShipment(int idShipment) {
        this.idShipment = idShipment;
    }

    public int getIdWarehouse() {
        return this.idWarehouse;
    }

    public void setIdWarehouse(int idWarehouse) {
        this.idWarehouse = idWarehouse;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public shipment idShipment(int idShipment) {
        setIdShipment(idShipment);
        return this;
    }

    public shipment idWarehouse(int idWarehouse) {
        setIdWarehouse(idWarehouse);
        return this;
    }

    public shipment destination(String destination) {
        setDestination(destination);
        return this;
    }

    public shipment status(String status) {
        setStatus(status);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof shipment)) {
            return false;
        }
        shipment shipment = (shipment) o;
        return idShipment == shipment.idShipment && idWarehouse == shipment.idWarehouse && Objects.equals(destination, shipment.destination) && Objects.equals(status, shipment.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idShipment, idWarehouse, destination, status);
    }

    @Override
    public String toString() {
        return "{" +
            " idShipment='" + getIdShipment() + "'" +
            ", idWarehouse='" + getIdWarehouse() + "'" +
            ", destination='" + getDestination() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
    
}
