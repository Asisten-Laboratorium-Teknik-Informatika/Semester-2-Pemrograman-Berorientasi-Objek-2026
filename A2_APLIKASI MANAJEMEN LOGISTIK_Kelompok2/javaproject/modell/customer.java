package modell;
import java.util.Objects;

public class customer {
    private int idCustomer;
    private String name;
    private String phone;
    private String address;


    public customer() {
    }

    public customer(int idCustomer, String name, String phone, String address) {
        this.idCustomer = idCustomer;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public int getIdCustomer() {
        return this.idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
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

    public customer idCustomer(int idCustomer) {
        setIdCustomer(idCustomer);
        return this;
    }

    public customer name(String name) {
        setName(name);
        return this;
    }

    public customer phone(String phone) {
        setPhone(phone);
        return this;
    }

    public customer address(String address) {
        setAddress(address);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof customer)) {
            return false;
        }
        customer customer = (customer) o;
        return idCustomer == customer.idCustomer && Objects.equals(name, customer.name) && Objects.equals(phone, customer.phone) && Objects.equals(address, customer.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCustomer, name, phone, address);
    }

    @Override
    public String toString() {
        return "{" +
            " idCustomer='" + getIdCustomer() + "'" +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
    
}
