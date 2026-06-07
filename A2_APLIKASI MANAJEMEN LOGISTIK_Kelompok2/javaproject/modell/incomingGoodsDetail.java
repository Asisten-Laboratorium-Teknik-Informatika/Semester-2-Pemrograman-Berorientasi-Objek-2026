package modell;
import java.util.Objects;

public class incomingGoodsDetail {
    private int idIngoodsDetail;
    private int idIngoods;
    private int idProduct;
    private int quantity;


    public incomingGoodsDetail() {
    }

    public incomingGoodsDetail(int idIngoodsDetail, int idIngoods, int idProduct, int quantity) {
        this.idIngoodsDetail = idIngoodsDetail;
        this.idIngoods = idIngoods;
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public int getIdIngoodsDetail() {
        return this.idIngoodsDetail;
    }

    public void setIdIngoodsDetail(int idIngoodsDetail) {
        this.idIngoodsDetail = idIngoodsDetail;
    }

    public int getIdIngoods() {
        return this.idIngoods;
    }

    public void setIdIngoods(int idIngoods) {
        this.idIngoods = idIngoods;
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

    public incomingGoodsDetail idIngoodsDetail(int idIngoodsDetail) {
        setIdIngoodsDetail(idIngoodsDetail);
        return this;
    }

    public incomingGoodsDetail idIngoods(int idIngoods) {
        setIdIngoods(idIngoods);
        return this;
    }

    public incomingGoodsDetail idProduct(int idProduct) {
        setIdProduct(idProduct);
        return this;
    }

    public incomingGoodsDetail quantity(int quantity) {
        setQuantity(quantity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof incomingGoodsDetail)) {
            return false;
        }
        incomingGoodsDetail incomingGoodsDetail = (incomingGoodsDetail) o;
        return idIngoodsDetail == incomingGoodsDetail.idIngoodsDetail && idIngoods == incomingGoodsDetail.idIngoods && idProduct == incomingGoodsDetail.idProduct && quantity == incomingGoodsDetail.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIngoodsDetail, idIngoods, idProduct, quantity);
    }

    @Override
    public String toString() {
        return "{" +
            " idIngoodsDetail='" + getIdIngoodsDetail() + "'" +
            ", idIngoods='" + getIdIngoods() + "'" +
            ", idProduct='" + getIdProduct() + "'" +
            ", quantity='" + getQuantity() + "'" +
            "}";
    }
    
}
