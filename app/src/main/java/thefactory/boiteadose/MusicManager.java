package thefactory.boiteadose;

import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Erwan on 20/04/2016.
 */
public class MusicManager {

    private File folder;
    private List<File> files;
    private int curMusic;

    private class FileNameFilterAudio implements FilenameFilter{

        @Override
        public boolean accept(File dir, String filename) {
            return filename.endsWith(".mp3") || filename.endsWith(".wav");
        }
    }

    public MusicManager(File folder)
    {
        Log.i("MusicManager - new", folder.getAbsolutePath());
        if (!folder.exists() || !folder.isDirectory())
        {
            folder.mkdir();
        }
        this.folder = folder;
        if (folder.listFiles(new FileNameFilterAudio()) != null)
        {
            this.files = Arrays.asList(folder.listFiles(new FileNameFilterAudio()));
        }
        else
        {
            this.files = new ArrayList<File>();
        }
        this.curMusic = (files.size() == 0 ? -1 : 0);
    }

    public File getCurMusic()
    {
        return this.curMusic == -1 ? new File("") : files.get(curMusic);
    }

    public File next()
    {
        if (files.size() == 0)
        {
            return new File("");
        }
        curMusic = (curMusic + 1) % files.size();
        return getCurMusic();
    }

    public File previous()
    {
        curMusic = --curMusic < 0 ? files.size() - 1 : curMusic;
        return getCurMusic();
    }

    public String getCurMusicName()
    {
        if (this.curMusic < 0)
        {
            return "";
        }
        return getCurMusic().getName().substring(0, getCurMusic().getName().lastIndexOf(".")).replace('_', ' ');
    }

    public void refresh()
    {
        if (folder.listFiles(new FileNameFilterAudio()) != null)
        {
            files = Arrays.asList(folder.listFiles(new FileNameFilterAudio()));
        }
        else
        {
            files = new ArrayList<File>();
        }
        if (curMusic >= files.size() || curMusic == -1)
        {
            curMusic = (files.size() == 0 ? -1 : 0);
        }
    }
}
