package alidoran.ir.dreamrecorder;

import java.util.Calendar;

public class TimeActivity {
    int thisYear;
    int thismonth;
    int thisday;
    int hour;
    int min;
    int sec;

    public String timeSystem ( ) {
        Calendar calendar = Calendar.getInstance ( );

        thisYear = calendar.get ( Calendar.YEAR );
        thismonth = calendar.get ( Calendar.MONTH ) + 1;
        thisday = calendar.get ( Calendar.DAY_OF_MONTH );
        hour = calendar.get ( Calendar.HOUR_OF_DAY );
        min = calendar.get ( Calendar.MINUTE );
        sec = calendar.get ( Calendar.SECOND );

        return (Integer.toString ( thisYear ) + "-" + String.format ("%02d" , thismonth ) + "-" + String.format ("%02d", thisday ) + "T" + String.format ("%02d" ,hour ) + String.format ("%02d" , min ) + String.format ("%02d" , sec ));
    }
    public static String timeNow ( ) {
        TimeActivity timeActivity=new TimeActivity ();
        return timeActivity.timeSystem ();
    }

}
