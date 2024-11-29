import java.time.DayOfWeek;
import java.time.Duration;
import java.time.ZoneId;
import java.util.Set;

public class Seller {
    String name;
    Set<DayOfWeek> workingDays;
    Duration averageDeliveryTime;
    ZoneId sellerTimeZone;

    public Seller(String name, Set<DayOfWeek> workingDays, Duration averageDeliveryTime, ZoneId sellerTimeZone) {
        this.name = name;
        this.workingDays = workingDays;
        this.averageDeliveryTime = averageDeliveryTime;
        this.sellerTimeZone = sellerTimeZone;
    }
}
