package com.example.thesisgameproject;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Aleksi on 22.3.2018.
 */

public class myGLSurfaceView extends GLSurfaceView{

    myRenderer myRender;

    public myGLSurfaceView(Context context) {
        super(context);
        // Tehdään OpenGL ES 3.0 konteksti
        setEGLContextClientVersion(3);

        super.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        // Lisätään rasterointi luokka käsittelemään piirtoja
        myRender = new myRenderer(context);
        setRenderer(myRender);

        // Rasteroidaan näkymä vain silloin kun muutoksia tapahtuu
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
   //     setRenderer(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = getWidth() / 2;
        float y = getHeight() / 2;


        Log.i("JOO", x + " on e.getX = " + e.getX());
        switch (e.getAction()) {

            case MotionEvent.ACTION_DOWN:

                float realX = e.getX() / x - 1;
                float realY = e.getY() / y - 1;

                myRender.setCoords(realX, realY);

        }

        return true;
    }

}
