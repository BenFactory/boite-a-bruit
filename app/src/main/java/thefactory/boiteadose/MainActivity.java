package thefactory.boiteadose;

import android.Manifest;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MusicManager musicManager;
    private TextView tv_musicName;
    private File app_folder;
    private MediaPlayer soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Demander les permissions si elles ne sont pas accordées
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Utils.requestPermissionsIfNot(this, this, permissions);

        //Récupération du dossier de l'application - création si il ne l'a pas encore été
        app_folder = new File(Utils.pathFiles);
        if (!app_folder.exists()) {
            app_folder.mkdir();
        }
        //Parce que si la dose n'est pas là, on crée le fichier
        if (!new File(Utils.pathFiles + "Dosé.wav").exists())
        {
            try {
                Utils.moveFileFromRaw(this, R.raw.dose, Utils.pathFiles, "Dosé.wav");
            } catch (IOException e) {
                Toast.makeText(this, "La dose n'arrive pas à être présente.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        musicManager = new MusicManager(app_folder);
        soundManager = null;

        tv_musicName = (TextView)findViewById(R.id.tv_name_file);

        tv_musicName.setText(musicManager.getCurMusicName());

        findViewById(R.id.b_make_sound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = musicManager.getCurMusic();
                if (file.exists()) {
                    if (soundManager != null)
                    {
                        soundManager.stop();
                        soundManager.release();
                    }
                    soundManager = MediaPlayer.create(v.getContext(), Uri.parse(file.getAbsolutePath()));
                    soundManager.start();
                } else {
                    Toast.makeText(v.getContext(), "Aucun fichier audio " + musicManager.getCurMusic() + " trouvé !", Toast.LENGTH_SHORT).show();
                    musicManager.refresh();
                }
            }
        });

        findViewById(R.id.b_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.next();
                tv_musicName.setText(musicManager.getCurMusicName());
            }
        });

        findViewById(R.id.b_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.previous();
                tv_musicName.setText(musicManager.getCurMusicName());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        musicManager.refresh();
    }
}
