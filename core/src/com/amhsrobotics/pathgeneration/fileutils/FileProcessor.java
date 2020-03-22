package com.amhsrobotics.pathgeneration.fileutils;

import com.amhsrobotics.pathgeneration.Overlay;
import com.amhsrobotics.pathgeneration.parametrics.abstractions.SplineController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class FileProcessor {

    private Json json;
    private FileHandle handle;
    private pgData data;

    public FileProcessor() {
        this.json = new Json();
        data = new pgData();
    }

/*    public void read(String filename) {
        this.handle = Gdx.files.absolute(filename);
        data = json.fromJson(pgData.class, handle.readString());

        Overlay.splineManager.setSplines(data.splines);
    }*/

    public void export(String filename) {

        this.handle = Gdx.files.absolute(filename);

        Overlay.splineManager.writeAll(handle);

        Gdx.app.log("FileUtils", "Exported to file!");
    }

    public pgData getData() {
        return data;
    }
}
