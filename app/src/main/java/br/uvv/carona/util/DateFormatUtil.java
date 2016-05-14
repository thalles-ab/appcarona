package br.uvv.carona.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by CB1772 on 14/05/2016.
 */
public class DateFormatUtil {
    public static DateFormat formatDateView = SimpleDateFormat.getDateInstance();
    public static SimpleDateFormat formatHourView = new SimpleDateFormat("HH:mm");
}
