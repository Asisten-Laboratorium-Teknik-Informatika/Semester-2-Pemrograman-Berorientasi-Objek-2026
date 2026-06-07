package modell;
import java.util.Objects;

public class distribution {
    private int idDistribution;
    private String distributionCode;
    private int idSourceWarehouse;
    private int idDestinationWarehouse;
    private int idProduct;
    private int quantity;


    public distribution() {
    }

    public distribution(int idDistribution, String distributionCode, int idSourceWarehouse, int idDestinationWarehouse, int idProduct, int quantity) {
        this.idDistribution = idDistribution;
        this.distributionCode = distributionCode;
        this.idSourceWarehouse = idSourceWarehouse;
        this.idDestinationWarehouse = idDestinationWarehouse;
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public int getIdDistribution() {
        return this.idDistribution;
    }

    public void setIdDistribution(int idDistribution) {
        this.idDistribution = idDistribution;
    }

    public String getDistributionCode() {
        return this.distributionCode;
    }

    public void setDistributionCode(String distributionCode) {
        this.distributionCode = distributionCode;
    }

    public int getIdSourceWarehouse() {
        return this.idSourceWarehouse;
    }

    public void setIdSourceWarehouse(int idSourceWarehouse) {
        this.idSourceWarehouse = idSourceWarehouse;
    }

    public int getIdDestinationWarehouse() {
        return this.idDestinationWarehouse;
    }

    public void setIdDestinationWarehouse(int idDestinationWarehouse) {
        this.idDestinationWarehouse = idDestinationWarehouse;
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

    public distribution idDistribution(int idDistribution) {
        setIdDistribution(idDistribution);
        return this;
    }

    public distribution distributionCode(String distributionCode) {
        setDistributionCode(distributionCode);
        return this;
    }

    public distribution idSourceWarehouse(int idSourceWarehouse) {
        setIdSourceWarehouse(idSourceWarehouse);
        return this;
    }

    public distribution idDestinationWarehouse(int idDestinationWarehouse) {
        setIdDestinationWarehouse(idDestinationWarehouse);
        return this;
    }

    public distribution idProduct(int idProduct) {
        setIdProduct(idProduct);
        return this;
    }

    public distribution quantity(int quantity) {
        setQuantity(quantity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof distribution)) {
            return false;
        }
        distribution distribution = (distribution) o;
        return idDistribution == distribution.idDistribution && Objects.equals(distributionCode, distribution.distributionCode) && idSourceWarehouse == distribution.idSourceWarehouse && idDestinationWarehouse == distribution.idDestinationWarehouse && idProduct == distribution.idProduct && quantity == distribution.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDistribution, distributionCode, idSourceWarehouse, idDestinationWarehouse, idProduct, quantity);
    }

    @Override
    public String toString() {
        return "{" +
            " idDistribution='" + getIdDistribution() + "'" +
            ", distributionCode='" + getDistributionCode() + "'" +
            ", idSourceWarehouse='" + getIdSourceWarehouse() + "'" +
            ", idDestinationWarehouse='" + getIdDestinationWarehouse() + "'" +
            ", idProduct='" + getIdProduct() + "'" +
            ", quantity='" + getQuantity() + "'" +
            "}";
    }
    
}
