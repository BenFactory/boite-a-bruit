package thefactory.boiteabruit;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Erwan on 27/04/2016.
 */
public class Utils {

    private static boolean hasPermissions(Context context, String... permissions) {
        //Verifie que l'application a les permissions (API 23 mini)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void requestPermissionsIfNot(Context context, Activity activity, String... permissions)
    {
        if(!Utils.hasPermissions(context, permissions)) {
            ActivityCompat.requestPermissions(activity, permissions, 123);
        }
    }
}
