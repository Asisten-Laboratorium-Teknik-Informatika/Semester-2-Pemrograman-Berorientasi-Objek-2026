package modell;
import java.util.Objects;

public class warehouseStocks {
    private int idStock;
    private int idProduct;
    private int idWarehouse;
    private int quantity;


    public warehouseStocks() {
    }

    public warehouseStocks(int idStock, int idProduct, int idWarehouse, int quantity) {
        this.idStock = idStock;
        this.idProduct = idProduct;
        this.idWarehouse = idWarehouse;
        this.quantity = quantity;
    }

    public int getIdStock() {
        return this.idStock;
    }

    public void setIdStock(int idStock) {
        this.idStock = idStock;
    }

    public int getIdProduct() {
        return this.idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdWarehouse() {
        return this.idWarehouse;
    }

    public void setIdWarehouse(int idWarehouse) {
        this.idWarehouse = idWarehouse;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public warehouseStocks idStock(int idStock) {
        setIdStock(idStock);
        return this;
    }

    public warehouseStocks idProduct(int idProduct) {
        setIdProduct(idProduct);
        return this;
    }

    public warehouseStocks idWarehouse(int idWarehouse) {
        setIdWarehouse(idWarehouse);
        return this;
    }

    public warehouseStocks quantity(int quantity) {
        setQuantity(quantity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof warehouseStocks)) {
            return false;
        }
        warehouseStocks warehouse_stocks = (warehouseStocks) o;
        return idStock == warehouse_stocks.idStock && idProduct == warehouse_stocks.idProduct && idWarehouse == warehouse_stocks.idWarehouse && quantity == warehouse_stocks.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idStock, idProduct, idWarehouse, quantity);
    }

    @Override
    public String toString() {
        return "{" +
            " idStock='" + getIdStock() + "'" +
            ", idProduct='" + getIdProduct() + "'" +
            ", idWarehouse='" + getIdWarehouse() + "'" +
            ", quantity='" + getQuantity() + "'" +
            "}";
    }
    
}
