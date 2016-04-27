package thefactory.boiteabruit;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import thefactory.boiteabruit.R;

public class MainActivity extends AppCompatActivity {

    private final String pathFiles = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BoiteABruit/";
    private MusicManager musicManager;
    private TextView tv;
    private File folder;
    private MediaPlayer sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Demander les permissions si elles ne sont pas accordées
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Utils.requestPermissionsIfNot(this, this, permissions);

        //Récupération du dossier de l'application - création si il ne l'a pas encore été
        folder = new File(pathFiles);
        if (!folder.exists()) {
            folder.mkdir();
        }

        musicManager = new MusicManager(folder);
        sound = null;

        tv = (TextView)findViewById(R.id.tv_name_file);

        tv.setText(musicManager.getCurMusicName());
        //musicManager.getAllMusicFile(this);

        findViewById(R.id.b_make_sound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = musicManager.getCurMusic();
                if (file.exists()) {
                    if (sound != null)
                    {
                        sound.stop();
                        sound.release();
                    }
                    sound = MediaPlayer.create(v.getContext(), Uri.parse(file.getAbsolutePath()));
                    sound.start();
                } else {
                    Toast.makeText(v.getContext(), "Aucun fichier audio \"" + musicManager.getCurMusic() + "\" trouvé !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.b_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.next();
                tv.setText(musicManager.getCurMusicName());
            }
        });

        findViewById(R.id.b_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.previous();
                tv.setText(musicManager.getCurMusicName());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        musicManager.refresh();
    }
}
