package thefactory.boiteadose;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Erwan on 27/04/2016.
 */
public class Utils {

    public static final String pathFiles = Environment.getExternalStorageDirectory().getAbsolutePath() + "/boite-a-dose/";

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

    public static void moveFileFromRaw(Context context, int id, String destination, String filename) throws IOException {
        InputStream in = context.getResources().openRawResource(id);
        File newFile = new File(destination + (destination.endsWith("/") ? "" : "/") + filename);
        if (!newFile.exists())
        {
            newFile.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(newFile);
        byte[] buff = new byte[1024];
        int read = 0;

        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            in.close();
            out.close();
        }

    }
}
