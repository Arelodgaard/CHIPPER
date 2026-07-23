package no.lodgaard.chipper.IO;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class KeyCodeListener {



    private boolean[] interrupted = new boolean[1];



    private boolean[] iterate = new boolean[1];

    private Scene scene;

    public KeyCodeListener (Scene scene, boolean interrupted, boolean iterate){

        this.scene = scene;

        this.interrupted[0] = interrupted;
        this.iterate[0] = iterate;
    }

    public void setUpListeners() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P) interrupted[0] = !interrupted[0];
            if (event.getCode() == KeyCode.I) iterate[0] = true;
            if (event.getCode() == KeyCode.ESCAPE) System.exit(0);
        });
    }

    public boolean getInterrupted(int index) {
        return interrupted[index];
    }

    public void setInterrupted(int index, boolean bool) {
        this.interrupted[index] = bool;
    }

    public boolean getIterate(int index) {
        return iterate[index];
    }

    public void setIterate(int index, boolean bool) {
        this.iterate[index] = bool;
    }

}
