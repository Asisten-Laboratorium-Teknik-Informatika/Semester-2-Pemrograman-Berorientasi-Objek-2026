package modell;
import java.util.Objects;

public class warehouse {
    private int idWarehouse;
    private String name;
    private String address;
    private int capacity;


    public warehouse() {
    }

    public warehouse(int idWarehouse, String name, String address, int capacity) {
        this.idWarehouse = idWarehouse;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    public int getIdWarehouse() {
        return this.idWarehouse;
    }

    public void setIdWarehouse(int idWarehouse) {
        this.idWarehouse = idWarehouse;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public warehouse idWarehouse(int idWarehouse) {
        setIdWarehouse(idWarehouse);
        return this;
    }

    public warehouse name(String name) {
        setName(name);
        return this;
    }

    public warehouse address(String address) {
        setAddress(address);
        return this;
    }

    public warehouse capacity(int capacity) {
        setCapacity(capacity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof warehouse)) {
            return false;
        }
        warehouse warehouse = (warehouse) o;
        return idWarehouse == warehouse.idWarehouse && Objects.equals(name, warehouse.name) && Objects.equals(address, warehouse.address) && capacity == warehouse.capacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idWarehouse, name, address, capacity);
    }

    @Override
    public String toString() {
        return "{" +
            " idWarehouse='" + getIdWarehouse() + "'" +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", capacity='" + getCapacity() + "'" +
            "}";
    }
    
}
