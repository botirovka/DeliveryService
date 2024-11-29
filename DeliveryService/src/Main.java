import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    static DataService dataService = new DataService();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("If you want to fill in the data manually," +
                " press m, or any other key to fill in the data automatically");

        if(scanner.next().equals("m")){
            Order order = manualDataFilling();
            System.out.println(order);
            dataService.getSellers().forEach(seller -> {
                LocalDate nextWorkingDay = calculateNextWorkingDay(order, seller);
                System.out.println("Найближчий робочий день доставки: " + nextWorkingDay);
            });
        }
        else {
            LocalDate now = LocalDate.now();
            LocalTime orderTime = LocalTime.of(22, 0);

            Order order = new Order(now, orderTime, ZoneId.systemDefault(), DeliveryMethod.REGULAR);
            System.out.println(order);

            dataService.getSellers().forEach(seller -> {
                LocalDate nextWorkingDay = calculateNextWorkingDay(order, seller);
                System.out.println("Найближчий робочий день доставки: " + nextWorkingDay);
            });
        }

    }
    private static boolean isHoliday(LocalDate date, ZoneId zoneId) {
        List<LocalDate> holidays = dataService.getHolidaysForRegion(zoneId);
        return holidays.contains(date);
    }
    public static LocalDate calculateNextWorkingDay(Order order, Seller seller) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");

        System.out.println("\nПродавець " + seller.name + " Регіон " + seller.sellerTimeZone);
        System.out.println("Обрахунок:");

       ZonedDateTime orderZonedDateTime = ZonedDateTime.of(order.orderDate, order.orderTime, order.deliveryZone);
       ZonedDateTime orderTimeInSellerZone = orderZonedDateTime.withZoneSameInstant(seller.sellerTimeZone);

        System.out.println("Замовлення отримано продавцем " + orderTimeInSellerZone.format(dateFormatter));
        System.out.println("Місцевий час покупця " + orderZonedDateTime.format(dateFormatter));
        LocalDate deliveryDate = order.orderDate;

        System.out.println("\nНаступні обрахунки за часом продавця");
        if (orderTimeInSellerZone.toLocalTime().isAfter(LocalTime.of(17, 0))) {
            System.out.println("Час у продавця " + orderTimeInSellerZone.format(dateFormatter));
            System.out.println("Сьогодні вже закінчився робочий день продавця, +1 день");

            deliveryDate = deliveryDate.plusDays(1);
            orderTimeInSellerZone = orderTimeInSellerZone.plusDays(1);
        }

        while (!seller.workingDays.contains(orderTimeInSellerZone.getDayOfWeek()) || isHoliday(LocalDate.from(orderTimeInSellerZone), order.deliveryZone)) {
            if (!seller.workingDays.contains(orderTimeInSellerZone.getDayOfWeek())) {
                System.out.println("Сьогодні " + orderTimeInSellerZone.format(dateFormatter) + " " + orderZonedDateTime.getDayOfWeek() + " Це не його робочий день");
            }
            if (isHoliday(deliveryDate, order.deliveryZone)) {
                System.out.println("Сьогодні " + orderTimeInSellerZone.format(dateFormatter) + " " + orderZonedDateTime.getDayOfWeek() + " Це святковий день");
            }
            System.out.println("+ 1 День");
            deliveryDate = deliveryDate.plusDays(1);
            orderTimeInSellerZone = orderTimeInSellerZone.plusDays(1);
        }

        deliveryDate = deliveryDate.plusDays(seller.averageDeliveryTime.toDays());
        System.out.println("Середня тривалість доставки від цього продавця: " + seller.averageDeliveryTime.toDays() + " днів");

        System.out.println("\nНаступні обрахунки за часом покупця");
        if (order.deliveryMethod.equals(DeliveryMethod.REGULAR)) {
            System.out.println("Затримка через звичайний тип доставки +2 дні");
            deliveryDate = deliveryDate.plusDays(2);
            System.out.println("Очікувана дата " + deliveryDate);
        } else {
            System.out.println("Очікувана дата прибуття посилки " + deliveryDate);
        }

        deliveryDate = deliveryDate.plusDays(RandomSituation());
        System.out.println("Замовлення прибуло " + deliveryDate);

        return deliveryDate;
    }

    private static int RandomSituation() {
        Random random = new Random();
        int chance = random.nextInt(10) + 1;
        if(chance == 1){
            int situation = random.nextInt(4);
            switch (situation){
                case 0:
                    System.out.println("У водія пошти з вашою посилкою закінчилося паливо - затримався на один день");
                    return 1;
                case 1:
                    System.out.println("У водія пошти з вашою посилкою пробило колесо - затримався на один день");
                    return 1;
                case 2:
                    System.out.println("Через помилку співробітника " +
                            "при оформленні документів, посилка прийшла на інший склад - переадресація зайняла 4 дні");
                    return 4;
                case 3:
                    System.out.println("Посилку загубили на складі - шукали пять днів");
                    return 5;
                default:
                    return 0;
            }
        }
        return 0;
    }

    private static Order manualDataFilling() {
        System.out.println("Введіть дату замовлення (dd-MM-yyyy):");

        LocalDate orderDate = validateDateInput();
        System.out.println("Введіть час замовлення (HH:mm):"); 
        LocalTime orderTime = validateTimeInput();

        ZoneId deliveryZone = chooseTimeZone();

        DeliveryMethod deliveryMethod = chooseDeliveryMethod();
        return new Order(orderDate, orderTime , deliveryZone, deliveryMethod);
    }


    private static LocalTime validateTimeInput() {
        scanner.nextLine();
        String inputTime = scanner.nextLine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = null;
        while (time == null) {
            try {
                time = LocalTime.parse(inputTime, timeFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Неправильний формат часу. Спробуйте знову (HH:mm):");
                inputTime = scanner.nextLine();
            }
        }
        return time;
    }

    private static DeliveryMethod chooseDeliveryMethod() {
        List<DeliveryMethod> deliveryMethods = Arrays.stream(DeliveryMethod.values()).toList();
        System.out.println("Оберіть Метод Доставки:");

        for (int i = 0; i < deliveryMethods.size(); i++) {
            System.out.println((i + 1) + ". " + deliveryMethods.get(i));
        }

        int choice = -1;
        while (choice < 1 || choice > deliveryMethods.size()) {
            System.out.print("Введіть номер вибраного методу доставки: ");
            choice = scanner.nextInt();
        }

        return deliveryMethods.get(choice - 1);
    }

    private static ZoneId chooseTimeZone() {
        List<String> timeZones = dataService.getTimezones();

        System.out.println("Оберіть часову зону для доставки:");
        for (int i = 0; i < timeZones.size(); i++) {
            System.out.println((i + 1) + ". " + timeZones.get(i));
        }

        int choice = -1;
        while (choice < 1 || choice > timeZones.size()) {
            System.out.print("Введіть номер вибраної часової зони: ");
            choice = scanner.nextInt();
        }

        return ZoneId.of(timeZones.get(choice - 1));
    }

    private static LocalDate validateDateInput() {
        scanner.nextLine();
        String inputDate = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = null;
        while (date == null) {
            try {
                date = LocalDate.parse(inputDate, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Неправильний формат дати. Спробуйте знову (dd-MM-yyyy):");
                inputDate = scanner.nextLine();
            }
        }
        return date;
    }
}