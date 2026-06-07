package modell;
import java.util.Objects;

public class product {

    private int idProduct;
    private int idCategories;
    private int idSupplier;
    private String name;
    private double price;
    private String unit;

    public product() {
    }

    public product(
            int idProduct,
            int idCategories,
            int idSupplier,
            String name,
            double price,
            String unit) {

        this.idProduct = idProduct;
        this.idCategories = idCategories;
        this.idSupplier = idSupplier;
        this.name = name;
        this.price = price;
        this.unit = unit;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdCategories() {
        return idCategories;
    }

    public void setIdCategories(int idCategories) {
        this.idCategories = idCategories;
    }

    public int getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(int idSupplier) {
        this.idSupplier = idSupplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public product idProduct(int idProduct) {
        setIdProduct(idProduct);
        return this;
    }

    public product idCategories(int idCategories) {
        setIdCategories(idCategories);
        return this;
    }

    public product idSupplier(int idSupplier) {
        setIdSupplier(idSupplier);
        return this;
    }

    public product name(String name) {
        setName(name);
        return this;
    }

    public product price(double price) {
        setPrice(price);
        return this;
    }

    public product unit(String unit) {
        setUnit(unit);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof product)) {
            return false;
        }
        product product = (product) o;
        return idProduct == product.idProduct && idCategories == product.idCategories && idSupplier == product.idSupplier && Objects.equals(name, product.name) && price == product.price && Objects.equals(unit, product.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduct, idCategories, idSupplier, name, price, unit);
    }

    @Override
    public String toString() {
        return "{" +
            " idProduct='" + getIdProduct() + "'" +
            ", idCategories='" + getIdCategories() + "'" +
            ", idSupplier='" + getIdSupplier() + "'" +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", unit='" + getUnit() + "'" +
            "}";
    }
    
}