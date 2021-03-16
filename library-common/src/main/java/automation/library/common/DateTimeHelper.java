package automation.library.common;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * @author KatulaS
 */

public class DateTimeHelper {

    private DateTimeHelper(){}

    /**
     * Get current timeStamp in yyyy-MM-dd'T'HH:mm:sZ
     * @param time time in Milisec
     * @return String format
     */
    public static String getISO08061TimeFormat(long time){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sZ");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(time);
    }

    /**
     * Get current timeStamp in yyyyMMddHHmmssSSS
     * @return String format
     */
    public static String getCurrentTimeStamp(){
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return df.format(new Date());
    }
}