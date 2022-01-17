package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;

public class salesStore {
    private SimpleStringProperty category;
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty quantity;
    private SimpleIntegerProperty unitPrice;
    private SimpleStringProperty time;
    private SimpleStringProperty totalPrice;


    public salesStore(String category, String id, int unitPrice, String name, String quantity, String time, String totalPrice){
        this.category = new SimpleStringProperty(category);
        this.id = new SimpleStringProperty(id);
        this.unitPrice = new SimpleIntegerProperty(unitPrice);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleStringProperty(quantity);
        this.time     = new SimpleStringProperty(time);
        this.totalPrice= new SimpleStringProperty(totalPrice);
    }

    public salesStore() {

    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category = new SimpleStringProperty(category);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id = new SimpleStringProperty(id);
    }

    public int getUnitPrice() {
        return unitPrice.get();
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = new SimpleIntegerProperty(unitPrice);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getQuantity(){
        return quantity.get();}

    public void setQuantity(String quantity){this.quantity = new SimpleStringProperty(quantity);}

   /* public SimpleStringProperty quantity(){
        return quantity;
    }*/

    public String getTime(){return time.get();}

    public void setTime(String time){this.time = new SimpleStringProperty(time);}

    public String getTotalPrice(){

        Float price = new Float(unitPrice.get());
        Float qty = new Float(quantity.get());
        Float total = price * qty;
        String totalPrice = String.format("%.2f", total);

        return totalPrice;
    }

    public void setTotalPrice(String totalPrice){this.totalPrice = new SimpleStringProperty(totalPrice);}
}
