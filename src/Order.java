
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;

public class Order{
    private SimpleIntegerProperty id;
    private SimpleObjectProperty<LocalDate> orderDate;
    private SimpleObjectProperty<LocalDate> orderLastUpdateDate;

    Order(int id,LocalDate orderDate, LocalDate orderLastUpdateDate){
        this.id = new SimpleIntegerProperty(id);
        this.orderDate = new SimpleObjectProperty<LocalDate>(orderDate);
        this.orderLastUpdateDate = new SimpleObjectProperty<LocalDate>(orderLastUpdateDate);
    }
    public int getId(){return id.get();}
    public void setId(int value){id.set(value);}

    public LocalDate getOrderDate() {
        return orderDate.get();
    }
    public void setOrderDate(LocalDate date){orderDate.set(date);}

    public LocalDate getOrderLastUpdateDate(){
        return orderLastUpdateDate.get();
    }
    public void setOrderLastUpdateDate(LocalDate date){orderLastUpdateDate.set(date);}
}