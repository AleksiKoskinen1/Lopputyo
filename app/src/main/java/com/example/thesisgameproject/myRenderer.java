package com.example.thesisgameproject;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Aleksi on 22.3.2018.
 */

public class myRenderer implements GLSurfaceView.Renderer{

    private Cube2 mCube;
    private Pyramid mPyramid;
    private backGround bg;
    private plusShape pShape;
    private longTriangle lTriangle;
    private hourGlass hGlass;
    private Rubin rubin;
    private float mTransY=0;
    private float mTransX=0;
    private Context context;
    private boolean gameOver = false;
    private boolean [] isRotating = new boolean[12];
    private float [] fadeAway = new float[12];
    private boolean [] conditionMet = new boolean[12];

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    public myRenderer(Context Gcontext) {
        context = Gcontext;
    }

    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {

        GLES30.glClearColor(0.9f, .9f, 0.9f, 0.9f);

        mCube = new Cube2();
        mCube.loadTexture(R.drawable.cube, context);

        mPyramid = new Pyramid();
        mPyramid.loadTexture(R.drawable.pyramid, context);

        hGlass = new hourGlass();
        hGlass.loadTexture(R.drawable.hourglass, context);

        pShape = new plusShape();
        pShape.loadTexture(R.drawable.brickwall, context);

        rubin = new Rubin();
        rubin.loadTexture(R.drawable.ruby, context);

        lTriangle = new longTriangle();
        lTriangle.loadTexture(R.drawable.fire, context);

        bg = new backGround();
        bg.loadTexture(R.drawable.bgtest, context);

    }
    public static int LoadShader(int type, String shaderSrc) {
        int shader;

        // Create the shader object
        shader = GLES30.glCreateShader(type);

        if (shader == 0) {
            return 0;
        }

        // Load the shader source
        GLES30.glShaderSource(shader, shaderSrc);

        // Compile the shader
        GLES30.glCompileShader(shader);

        return shader;
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {

        // Laitetaan näkymä
        GLES30.glViewport(0, 0, width, height);
        float aspectRatio = (float) width / height;

        //Käytetään perspektiivistä projisointia
        Matrix.perspectiveM(mProjectionMatrix, 0, 53.00f, aspectRatio, 1, 10);
        //  Matrix.frustumM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1.0f, 1.0f, 1.0f, 1000.f);
    }

    public void onDrawFrame(GL10 glUnused) {

        //Lasketaan väliä 0-360 (10 sekunnin välein alkaa nollasta)
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        //Enabloidaan syvyys, jotta 3D-grafiikka saadaan näkymään oikein
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);

        // Laitetaan kameran positio, eli mistä kohtaa se katsoo näkymään
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 0.5f, 0f, 0f, -5.0f, 0f, 1.0f, 0.0f);
                                                     //kuvakulma, //kaukosuus oli -0.5 its good

        ////////////////////////////////////////////////////////////////////
        // FIRST OBJECT
        ////////////////////////////////////////////////////////////////////
        //Nollataan matriisi
        Matrix.setIdentityM(mRotationMatrix, 0);
        //Sijainti ruudulla
        Matrix.translateM(mRotationMatrix, 0, -0.8f, 1.0f, -3.5f+fadeAway[0]);
        //Laitetaan aloituskierto 60 asteen verran
        Matrix.rotateM(mRotationMatrix, 0, 60f, 0.4f, 1.0f, 0.6f);

        //Katsotaan jos kuviota on painettu, laitetaan sille jatkuvaa rotaatiota akselinsa ympäri
        if(isRotating[0]) {
            Matrix.rotateM(mRotationMatrix, 0, angleInDegrees, 0.4f, 1.0f, 0.6f);
            //Jos kaksi samaa kuviota on painettu, poistetaan ne ruudulta
            if(isRotating[4] && !isRotating[1] && !isRotating[2] && !isRotating[3] && !isRotating[5] && !isRotating[6] && !isRotating[7] && !isRotating[8] && !isRotating[9] && !isRotating[10] && !isRotating[11]){
                conditionMet[0] = true;
            }
        }
        //Pienennetään syvyytta kuvioissa, jos molempia on painettu
        if(conditionMet[0]){
            setFadeAway(0, 4);
        }
        //Ynnätään matriisit yhteen, ja piirretään kuvio lopullisen matriisin avulla
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        mCube.draw(mMVPMatrix);

        ////////////////////////////////////////////////////////////////////
        // SECOND OBJECT
        ////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0.0f, 1.0f, -3.5f+fadeAway[1]);
        Matrix.rotateM(mRotationMatrix, 0, 320f, 0.4f, 1.0f, 0.6f);
        if(isRotating[1]) {
            Matrix.rotateM(mRotationMatrix, 0, angleInDegrees, 1.0f, 1.0f, 1.0f);
            if(isRotating[9] && !isRotating[0] && !isRotating[2] && !isRotating[3] && !isRotating[4] && !isRotating[5] && !isRotating[6] && !isRotating[7] && !isRotating[8] && !isRotating[10] && !isRotating[11]){
                conditionMet[1] = true;
            }
        }
        if(conditionMet[1]){
            setFadeAway(1, 9);
        }
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        mPyramid.draw(mMVPMatrix);

        ////////////////////////////////////////////////////////////////////
        // THIRD OBJECT
        ////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0.8f, 1.0f, -3.5f+fadeAway[2]);
        Matrix.rotateM(mRotationMatrix, 0, 80f, 0.4f, 1.0f, 0.6f);
        if(isRotating[2]) {
            Matrix.rotateM(mRotationMatrix, 0, angleInDegrees, 1.0f, 1.0f, 1.0f);
            if(isRotating[8] && !isRotating[0] && !isRotating[1] && !isRotating[3] && !isRotating[4] && !isRotating[5] && !isRotating[6] && !isRotating[7] && !isRotating[9] && !isRotating[10] && !isRotating[11]){
                conditionMet[2] = true;
            }
        }
        if(conditionMet[2]){
            setFadeAway(2, 8);
        }
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        pShape.draw(mMVPMatrix);

        ////////////////////////////////////////////////////////////////////
        //  FOURTH OBJECT
        ////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, -0.8f, 0.25f, -3.5f+fadeAway[3]);
        Matrix.rotateM(mRotationMatrix, 0, 90f, 0.4f, 1.0f, 0.6f);
        if(isRotating[3]) {
            Matrix.rotateM(mRotationMatrix, 0, angleInDegrees, 1.0f, 1.0f, 1.0f);
            if(isRotating[11] && !isRotating[0] && !isRotating[1] && !isRotating[2] && !isRotating[4] && !isRotating[5] && !isRotating[6] && !isRotating[7] && !isRotating[8] && !isRotating[9] && !isRotating[10]){
                conditionMet[3] = true;
            }
        }
        if(conditionMet[3]){
            setFadeAway(3, 11);
        }
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        hGlass.draw(mMVPMatrix);

        ////////////////////////////////////////////////////////////////////
        //  FIFTH OBJECT
        ////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0.0f, 0.25f, -3.5f+fadeAway[4]);
        Matrix.rotateM(mRotationMatrix, 0, 80f, 0.4f, 1.0f, 0.6f);
        if(isRotating[4]) {
            Matrix.rotateM(mRotationMatrix, 0, angleInDegrees, 0.4f, 1.0f, 0.6f);
            if(isRotating[0] && !isRotating[1] && !isRotating[2] && !isRotating[3] && !isRotating[5] && !isRotating[6] && !isRotating[7] && !isRotating[8] && !isRotating[9] && !isRotating[10] && !isRotating[11]){
                conditionMet[4] = true;
            }
        }
        if(conditionMet[4]){
            setFadeAway(4, 0);
        }
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        mCube.draw(mMVPMatrix);

        ////////////////////////////////////////////////////////////////////
        // SIXTH OBJECT
        ////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0.8f, 0.25f, -3.5f+fadeAway[5]);
        Matrix.rotateM(mRotationMatrix, 0, 200f, 0.4f, 1.0f, 0.6f);
        if(isRotating[5]) {
            Matrix.rotateM(mRotationMatrix, 0, angleInDegrees, 1.0f, 1.0f, 1.0f);
            if(isRotating[7] && !isRotating[0] && !isRotating[1] && !isRotating[2] && !isRotating[3] && !isRotating[4] && !isRotating[6] && !isRotating[8] && !isRotating[9] && !isRotating[10] && !isRotating[11]){
                conditionMet[5] = true;
            }
        }
        if(conditionMet[5]){
            setFadeAway(5, 7);
        }
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        lTriangle.draw(mMVPMatrix);

        ////////////////////////////////////////////////////////////////////
        // SEVENTH OBJECT
        ////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, -0.8f, -0.5f, -3.5f+fadeAway[6]);
        Matrix.rotateM(mRotationMatrix, 0, 240f, 0.4f, 1.0f, 0.6f);
        if(isRotating[6]) {
            Matrix.rotateM(mRotationMatrix, 0, angleInDegrees, 1.0f, 1.0f, 1.0f);
            if(isRotating[10] && !isRotating[0] && !isRotating[1] && !isRotating[2] && !isRotating[3] && !isRotating[4] && !isRotating[5] && !isRotating[7] && !isRotating[8] && !isRotating[9] && !isRotating[11]){
                conditionMet[6] = true;
            }
        }
        if(conditionMet[6]){
            setFadeAway(6, 10);
        }
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        rubin.draw(mMVPMatrix);

        ////////////////////////////////////////////////////////////////////
        // EIGHT OBJECT
        ////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0.0f, -0.5f, -3.5f+fadeAway[7]);
        Matrix.rotateM(mRotationMatrix, 0, 190f, 0.4f, 1.0f, 0.6f);
        if(isRotating[7]) {
            Matrix.rotateM(mRotationMatrix, 0, angleInDegrees, 1.0f, 1.0f, 1.0f);
            if(isRotating[5] && !isRotating[0] && !isRotating[1] && !isRotating[2] && !isRotating[3] && !isRotating[4] && !isRotating[6] && !isRotating[8] && !isRotating[9] && !isRotating[10] && !isRotating[11]){
                conditionMet[7] = true;
            }
        }
        if(conditionMet[7]){
            setFadeAway(7, 5);
        }
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        lTriangle.draw(mMVPMatrix);

        ////////////////////////////////////////////////////////////////////
        // NINTH OBJECT
        ////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0.8f, -0.5f, -3.5f+fadeAway[8]);
        if(isRotating[8]) {
            Matrix.rotateM(mRotationMatrix, 0, angleInDegrees, 1.0f, 1.0f, 1.0f);
            if(isRotating[2] && !isRotating[0] && !isRotating[1] && !isRotating[3] && !isRotating[4] && !isRotating[5] && !isRotating[6] && !isRotating[7] && !isRotating[9] && !isRotating[10] && !isRotating[11]){
                conditionMet[8] = true;
            }
        }
        if(conditionMet[8]){
            setFadeAway(8, 2);
        }
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        pShape.draw(mMVPMatrix);

        ////////////////////////////////////////////////////////////////////
        // TENTH OBJECT
        ////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, -0.8f, -1.3f, -3.5f+fadeAway[9]);
        Matrix.rotateM(mRotationMatrix, 0, 320f, 0.4f, 1.0f, 0.6f);
        if(isRotating[9]) {
            Matrix.rotateM(mRotationMatrix, 0, angleInDegrees, 1.0f, 1.0f, 1.0f);
            if(isRotating[1] && !isRotating[0] && !isRotating[2] && !isRotating[3] && !isRotating[4] && !isRotating[5] && !isRotating[6] && !isRotating[7] && !isRotating[8] && !isRotating[10] && !isRotating[11]){
                conditionMet[9] = true;
            }
        }
        if(conditionMet[9]){
            setFadeAway(9, 1);
        }
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        mPyramid.draw(mMVPMatrix);

        ////////////////////////////////////////////////////////////////////
        // ELEVENTH OBJECT
        ////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(mRotationMatrix, 0);

        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0.0f, -1.3f, -3.5f+fadeAway[10]);
        Matrix.rotateM(mRotationMatrix, 0, 160f, 0.4f, 1.0f, 0.6f);
        if(isRotating[10]) {
            Matrix.rotateM(mRotationMatrix, 0, angleInDegrees, 1.0f, 1.0f, 1.0f);
            if(isRotating[6] && !isRotating[0] && !isRotating[1] && !isRotating[2] && !isRotating[3] && !isRotating[4] && !isRotating[5] && !isRotating[7] && !isRotating[8] && !isRotating[9] && !isRotating[11]){
                conditionMet[10] = true;
            }
        }
        if(conditionMet[10]){
            setFadeAway(10, 6);
        }
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        rubin.draw(mMVPMatrix);

        ////////////////////////////////////////////////////////////////////
        // TWELVTH OBJECT
        ////////////////////////////////////////////////////////////////////
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0.8f, -1.3f, -3.5f+fadeAway[11]);
        if(isRotating[11]) {
            Matrix.rotateM(mRotationMatrix, 0, angleInDegrees, 1.0f, 1.0f, 1.0f);
            if(isRotating[3] && !isRotating[0] && !isRotating[1] && !isRotating[2] && !isRotating[4] && !isRotating[5] && !isRotating[6] && !isRotating[7] && !isRotating[8] && !isRotating[9] && !isRotating[10]){
                conditionMet[11] = true;
            }
        }
        if(conditionMet[11]){
            setFadeAway(11, 3);
        }
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        hGlass.draw(mMVPMatrix);

        Matrix.setIdentityM(mRotationMatrix, 0);

        Matrix.translateM(mRotationMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.scaleM(mRotationMatrix, 0, 1.75f, 2.8f, 0.0f);

        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);
        // combine the model-view with the projection matrix
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);


        //Jos kaikki kuviot saatu pois ruudulta
        if(conditionMet[0] && conditionMet[1] && conditionMet[2] && conditionMet[3] && conditionMet[4] && conditionMet[5] &&
                conditionMet[6] && conditionMet[7] && conditionMet[8] && conditionMet[9] && conditionMet[10] && conditionMet[11]) {

            bg.loadTexture(R.drawable.backgroundfinish, context);

            gameOver = true;
            if(mTransX < 0.3f && mTransX > -0.2f && mTransY < 0.65f && mTransY > 0.44f) {
                conditionMet = new boolean[12];
                fadeAway = new float[12];
                isRotating = new boolean[12];
                gameOver = false;
                bg.loadTexture(R.drawable.bgtest, context);
            }
        }
            bg.draw(mMVPMatrix);

    }

    public void setFadeAway(int objNumber, int objNumber2){

        fadeAway[objNumber] += 0.005f;
        if(fadeAway[objNumber] > 3f){
            isRotating[objNumber] = false;
            isRotating[objNumber2] = false;
        }
    }

    public void setCoords(float x, float y){

        mTransX = x;
        mTransY = y;

        if(!gameOver){

            //one
            if (mTransX < -0.50f && mTransX > -0.8f && mTransY < -0.35f && mTransY > -0.6f) {
                if (!isRotating[0]) {
                    isRotating[0] = true;
                } else if (isRotating[0]) {
                    isRotating[0] = false;
                }
            }
            //two
            if (mTransX < 0.2f && mTransX > -0.16f && mTransY < -0.35f && mTransY > -0.6f) {
                if (!isRotating[1]) {
                    isRotating[1] = true;
                } else if (isRotating[1]) {
                    isRotating[1] = false;
                }
            }
            //third
            if (mTransX < 0.82f && mTransX > 0.5f && mTransY < -0.35f && mTransY > -0.6f) {
                if (!isRotating[2]) {
                    isRotating[2] = true;
                } else if (isRotating[2]) {
                    isRotating[2] = false;
                }
            }
            //fourth
            if (mTransX < -0.50f && mTransX > -0.8f && mTransY < 0f && mTransY > -0.2f) {
                if (!isRotating[3]) {
                    isRotating[3] = true;
                } else if (isRotating[3]) {
                    isRotating[3] = false;
                }
            }
            //fifth
            if (mTransX < 0.2f && mTransX > -0.16f && mTransY < 0f && mTransY > -0.2f) {
                if (!isRotating[4]) {
                    isRotating[4] = true;
                } else if (isRotating[4]) {
                    isRotating[4] = false;
                }
            }
            //sixth
            if (mTransX < 0.82f && mTransX > 0.5f && mTransY < 0f && mTransY > -0.2f) {
                if (!isRotating[5]) {
                    isRotating[5] = true;
                } else if (isRotating[5]) {
                    isRotating[5] = false;
                }
            }
            //seven
            if (mTransX < -0.50f && mTransX > -0.8f && mTransY < 0.35f && mTransY > 0.18f) {
                if (!isRotating[6]) {
                    isRotating[6] = true;
                } else if (isRotating[6]) {
                    isRotating[6] = false;
                }
            }
            //eight
            if (mTransX < 0.2f && mTransX > -0.16f && mTransY < 0.35f && mTransY > 0.18f) {
                if (!isRotating[7]) {
                    isRotating[7] = true;
                } else if (isRotating[7]) {
                    isRotating[7] = false;
                }
            }
            //nine
            if (mTransX < 0.82f && mTransX > 0.5f && mTransY < 0.35f && mTransY > 0.18f) {
                if (!isRotating[8]) {
                    isRotating[8] = true;
                } else if (isRotating[8]) {
                    isRotating[8] = false;
                }
            }
            //ten
            if (mTransX < -0.50f && mTransX > -0.8f && mTransY < 0.80f && mTransY > 0.60f) {
                if (!isRotating[9]) {
                    isRotating[9] = true;
                } else if (isRotating[9]) {
                    isRotating[9] = false;
                }
            }
            //11
            if (mTransX < 0.2f && mTransX > -0.16f && mTransY < 0.80f && mTransY > 0.60f) {
                if (!isRotating[10]) {
                    isRotating[10] = true;
                } else if (isRotating[10]) {
                    isRotating[10] = false;
                }
            }
            //12
            if (mTransX < 0.82f && mTransX > 0.5f && mTransY < 0.80f && mTransY > 0.60f) {
                if (!isRotating[11]) {
                    isRotating[11] = true;
                } else if (isRotating[11]) {
                    isRotating[11] = false;
                }
            }
        }
    }
}
