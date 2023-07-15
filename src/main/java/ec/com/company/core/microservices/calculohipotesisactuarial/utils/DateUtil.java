package ec.com.company.core.microservices.calculohipotesiscompanyl.utils;

import ec.com.company.core.microservices.calculohipotesiscompanyl.contants.AppConstants;

import java.time.LocalDate;

public class DateUtil {
    public static String formatDate(LocalDate date) {
        return date.format(AppConstants.DATE_FORMATTER);
    }

}
