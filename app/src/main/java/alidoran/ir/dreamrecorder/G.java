package alidoran.ir.dreamrecorder;

import android.app.Application;
import ir.tapsell.sdk.Tapsell;


public class G extends Application {
    @Override
    public void onCreate ( ) {
        super.onCreate ( );
        String appKey ="ahscmrmnkojckfkrgctcfgsqndqjrspecjshkogrekbhcpriapmdtndkcfcncqfmrsfrnb";
        Tapsell.initialize(MainActivity.context, appKey);



    }
}
