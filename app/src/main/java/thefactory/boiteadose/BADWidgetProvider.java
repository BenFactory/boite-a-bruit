package thefactory.boiteadose;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.RemoteViews;

/**
 * Created by Erwan on 27/04/2016.
 */
public class BADWidgetProvider extends AppWidgetProvider {

    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
    private static MediaPlayer mp;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.main_widget);
            Intent intent = new Intent(context, BADWidgetProvider.class);
            intent.setAction(ACTION_WIDGET_RECEIVER);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_ib_dose, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (mp == null)
            mp = MediaPlayer.create(context.getApplicationContext(), R.raw.dose);

        final String action = intent.getAction();

        if (ACTION_WIDGET_RECEIVER.equals(action))
        {
            if (mp.isPlaying())
            {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(context.getApplicationContext(), R.raw.dose);
            }
            else
            {
                mp.start();
            }
        }
        super.onReceive(context, intent);
    }
}
