package by.kuchinsky.alexandr.komilfoserver.Model;

public class Order {

    private String ProductId;
    private String ProductName;
    private String Qantity;
    private String Price;
    private String Discount;

    public Order() {

    }

    public Order(String productId, String productName, String qantity, String price, String discount) {
        ProductId = productId;
        ProductName = productName;
        Qantity = qantity;
        Price = price;
        Discount = discount;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQantity() {
        return Qantity;
    }

    public void setQantity(String qantity) {
        Qantity = qantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}