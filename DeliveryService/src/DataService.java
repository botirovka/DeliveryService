import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataService {
    private List<Seller> sellers = new ArrayList<>();
    private List<String> timezones = new ArrayList<>();
    private Map<ZoneId, List<LocalDate>> regionalHolidays = new HashMap<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public List<LocalDate> getHolidaysForRegion(ZoneId zoneId) {
        return regionalHolidays.getOrDefault(zoneId, Collections.emptyList());
    }

    public DataService() {
        sellersSetInfo();
        timezonesSetInfo();
        regionalHolidaysSetInfo();
    }

    private void regionalHolidaysSetInfo() {
        // Європа
        regionalHolidays.put(ZoneId.of("Europe/Kiev"), Arrays.asList(
                LocalDate.parse("01-01-2024", DATE_FORMATTER), // Новий Рік
                LocalDate.parse("07-01-2024", DATE_FORMATTER), // Різдво (православне)
                LocalDate.parse("08-03-2024", DATE_FORMATTER), // Міжнародний жіночий день
                LocalDate.parse("01-05-2024", DATE_FORMATTER), // День Праці
                LocalDate.parse("09-05-2024", DATE_FORMATTER), // День Перемоги
                LocalDate.parse("28-06-2024", DATE_FORMATTER), // День Конституції
                LocalDate.parse("24-08-2024", DATE_FORMATTER), // День Незалежності
                LocalDate.parse("25-12-2024", DATE_FORMATTER)  // Різдво (католицьке)
        ));

        // Європа
        regionalHolidays.put(ZoneId.of("Europe/London"), Arrays.asList(
                LocalDate.parse("01-01-2024", DATE_FORMATTER), // New Year's Day
                LocalDate.parse("29-03-2024", DATE_FORMATTER), // Good Friday
                LocalDate.parse("01-04-2024", DATE_FORMATTER), // Easter Monday
                LocalDate.parse("06-05-2024", DATE_FORMATTER), // Early May Bank Holiday
                LocalDate.parse("27-05-2024", DATE_FORMATTER), // Spring Bank Holiday
                LocalDate.parse("26-08-2024", DATE_FORMATTER), // Summer Bank Holiday
                LocalDate.parse("25-12-2024", DATE_FORMATTER), // Christmas Day
                LocalDate.parse("26-12-2024", DATE_FORMATTER)  // Boxing Day
        ));

        // США
        regionalHolidays.put(ZoneId.of("America/New_York"), Arrays.asList(
                LocalDate.parse("01-01-2024", DATE_FORMATTER), // New Year's Day
                LocalDate.parse("15-01-2024", DATE_FORMATTER), // Martin Luther King Jr. Day
                LocalDate.parse("19-02-2024", DATE_FORMATTER), // Presidents' Day
                LocalDate.parse("27-05-2024", DATE_FORMATTER), // Memorial Day
                LocalDate.parse("04-07-2024", DATE_FORMATTER), // Independence Day
                LocalDate.parse("02-09-2024", DATE_FORMATTER), // Labor Day
                LocalDate.parse("28-11-2024", DATE_FORMATTER), // Thanksgiving Day
                LocalDate.parse("25-12-2024", DATE_FORMATTER)  // Christmas Day
        ));

        // Японія
        regionalHolidays.put(ZoneId.of("Asia/Tokyo"), Arrays.asList(
                LocalDate.parse("01-01-2024", DATE_FORMATTER), // New Year's Day
                LocalDate.parse("08-01-2024", DATE_FORMATTER), // Coming of Age Day
                LocalDate.parse("11-02-2024", DATE_FORMATTER), // National Foundation Day
                LocalDate.parse("29-04-2024", DATE_FORMATTER), // Showa Day
                LocalDate.parse("03-05-2024", DATE_FORMATTER), // Constitution Memorial Day
                LocalDate.parse("04-05-2024", DATE_FORMATTER), // Greenery Day
                LocalDate.parse("05-05-2024", DATE_FORMATTER), // Children's Day
                LocalDate.parse("23-12-2024", DATE_FORMATTER)  // Emperor's Birthday
        ));
    }


    private void timezonesSetInfo() {
        timezones = Arrays.asList(
                "Europe/Kiev",
                "Europe/London",
                "America/New_York",
                "Asia/Tokyo");

    }

    private void sellersSetInfo() {
        sellers.add(new Seller(
                "Seller1",
                EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                Duration.ofDays(3),
                ZoneId.of("Europe/Kiev")
        ));

        sellers.add(new Seller(
                "Seller2",
                EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                Duration.ofDays(5),
                ZoneId.of("Europe/London")
        ));

        sellers.add(new Seller(
                "Seller3",
                EnumSet.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY),
                Duration.ofDays(2),
                ZoneId.of("America/New_York")
        ));

        sellers.add(new Seller(
                "Seller4",
                EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY),
                Duration.ofDays(7),
                ZoneId.of("Asia/Tokyo")
        ));
    }

    public List<Seller> getSellers() {
        return sellers;
    }
    public List<String> getTimezones() {
        return timezones;
    }
}
