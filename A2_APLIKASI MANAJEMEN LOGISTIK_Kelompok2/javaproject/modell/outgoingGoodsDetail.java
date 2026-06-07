package modell;
import java.util.Objects;

public class outgoingGoodsDetail {
    private int idOutgoodsDetail;
    private int idOutgoods;
    private int idProduct;
    private int quantity;


    public outgoingGoodsDetail() {
    }

    public outgoingGoodsDetail(int idOutgoodsDetail, int idOutgoods, int idProduct, int quantity) {
        this.idOutgoodsDetail = idOutgoodsDetail;
        this.idOutgoods = idOutgoods;
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public int getIdOutgoodsDetail() {
        return this.idOutgoodsDetail;
    }

    public void setIdOutgoodsDetail(int idOutgoodsDetail) {
        this.idOutgoodsDetail = idOutgoodsDetail;
    }

    public int getIdOutgoods() {
        return this.idOutgoods;
    }

    public void setIdOutgoods(int idOutgoods) {
        this.idOutgoods = idOutgoods;
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

    public outgoingGoodsDetail idOutgoodsDetail(int idOutgoodsDetail) {
        setIdOutgoodsDetail(idOutgoodsDetail);
        return this;
    }

    public outgoingGoodsDetail idOutgoods(int idOutgoods) {
        setIdOutgoods(idOutgoods);
        return this;
    }

    public outgoingGoodsDetail idProduct(int idProduct) {
        setIdProduct(idProduct);
        return this;
    }

    public outgoingGoodsDetail quantity(int quantity) {
        setQuantity(quantity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof outgoingGoodsDetail)) {
            return false;
        }
        outgoingGoodsDetail outgoingGoodsDetail = (outgoingGoodsDetail) o;
        return idOutgoodsDetail == outgoingGoodsDetail.idOutgoodsDetail && idOutgoods == outgoingGoodsDetail.idOutgoods && idProduct == outgoingGoodsDetail.idProduct && quantity == outgoingGoodsDetail.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOutgoodsDetail, idOutgoods, idProduct, quantity);
    }

    @Override
    public String toString() {
        return "{" +
            " idOutgoodsDetail='" + getIdOutgoodsDetail() + "'" +
            ", idOutgoods='" + getIdOutgoods() + "'" +
            ", idProduct='" + getIdProduct() + "'" +
            ", quantity='" + getQuantity() + "'" +
            "}";
    }
    
}
