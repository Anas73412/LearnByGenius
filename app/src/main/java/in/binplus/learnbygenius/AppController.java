package in.binplus.learnbygenius;

import android.app.Application;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        List<Locale> locales = new ArrayList<>();
        locales.add(Locale.ENGLISH);
        locales.add(new Locale("en","ENGLISH"));
//        LocaleHelper.setLocale(getApplicationContext(),"en");
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }





    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}


