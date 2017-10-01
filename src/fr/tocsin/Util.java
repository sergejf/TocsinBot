package fr.tocsin;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class Util {

    public static LocalDate parseDate(String dateString) {
        LocalDate date;
        try {
            date = LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            System.err.println("parseData failed. Date: " + dateString);
            System.err.println(e.getMessage());
            date = null;
        }
        return date;
    }

    // Dates are normalized to the New York time zone
    public static String todayDate() {
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("America/New_York"));
        return ft.format(today);
    }

    public static String lastWeekday() {
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("America/New_York"));
        ZonedDateTime lastWeekday;

        switch (today.getDayOfWeek()) {
            case SATURDAY:
                lastWeekday = today.minus(1, ChronoUnit.DAYS);
                break;
            case SUNDAY:
                lastWeekday = today.minus(2, ChronoUnit.DAYS);
                break;
            default:
                lastWeekday = today;
        }
        return ft.format(lastWeekday);
    }

    public static String stripQuotes(String string) {
        return string.replaceAll("^\"|\"$", "");
    }
}
