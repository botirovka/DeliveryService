import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class Order {
    LocalDate orderDate;
    LocalTime orderTime;

    ZoneId deliveryZone;
    DeliveryMethod deliveryMethod;

    public Order(LocalDate orderDate, LocalTime orderTime, ZoneId deliveryZone, DeliveryMethod deliveryMethod) {
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.deliveryZone = deliveryZone;
        this.deliveryMethod = deliveryMethod;
    }

    @Override
    public String toString() {
        return "Замовлення{" +
                "\n Дата замовлення=" + orderDate +
                "\nЧас замовлення=" + orderTime +
                "\nРегіон Доставки=" + deliveryZone +
                "\nТип Доставки=" + deliveryMethod +
                "\n}";
    }
}
