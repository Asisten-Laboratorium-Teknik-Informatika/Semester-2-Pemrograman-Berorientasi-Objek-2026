package modell;
import java.util.Objects;

public class supplier {
    private int idSupplier;
    private String name;
    private String phone;
    private String address;


    public supplier() {
    }

    public supplier(int idSupplier, String name, String phone, String address) {
        this.idSupplier = idSupplier;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public int getIdSupplier() {
        return this.idSupplier;
    }

    public void setIdSupplier(int idSupplier) {
        this.idSupplier = idSupplier;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public supplier idSupplier(int idSupplier) {
        setIdSupplier(idSupplier);
        return this;
    }

    public supplier name(String name) {
        setName(name);
        return this;
    }

    public supplier phone(String phone) {
        setPhone(phone);
        return this;
    }

    public supplier address(String address) {
        setAddress(address);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof supplier)) {
            return false;
        }
        supplier supplier = (supplier) o;
        return idSupplier == supplier.idSupplier && Objects.equals(name, supplier.name) && Objects.equals(phone, supplier.phone) && Objects.equals(address, supplier.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSupplier, name, phone, address);
    }

    @Override
    public String toString() {
        return "{" +
            " idSupplier='" + getIdSupplier() + "'" +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
    
}
