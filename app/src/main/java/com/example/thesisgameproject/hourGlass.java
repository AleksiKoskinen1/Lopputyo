package com.example.thesisgameproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Aleksi on 22.3.2018.
 */

public class hourGlass {
    private int mProgramObject;
    private FloatBuffer vertexBuffer;
    private final FloatBuffer textureBuffer;
    private final ShortBuffer drawListBuffer;
    private final int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    private final int COORDS_PER_TEXTURE = 2;
    private int textureStride = COORDS_PER_TEXTURE * 4;
    private int[] textures = new int[1];

    //Kuvion koko, jotta sitä on helppo muuttaa tarvittaessa
    private float size = 0.2f;

    private float[] mVerticesData = new float[]{

            //keskus verteksi
            0.0f, 0.0f, 0.0f,
            //ylaneliö
            -size, -size, -size,
            -size, -size, size,
            size, -size, size,
            size, -size, -size,
            //alaneliö
            size, size, -size,
            size, size, size,
            -size, size, size,
            -size, size, -size,

            //tekstuureja varten molemmista neliöstä 2 verteksiä

            size, -size, size,
            size, -size, -size,

            -size, size, size,
            -size, size, -size,


    };

    private short[] indices = {
            //Yläpuolen kolmiot
            1, 0, 2, 2, 0, 3, 3, 0, 4, 4, 0, 1,
            //alapuolen kolmiot
            5, 0, 6, 6, 0, 7, 7, 0, 8, 8, 0, 5,
            //neliöt päihin
            1, 2, 9, 9, 10, 1,
            5, 6, 11, 11, 12, 5,

    };

    private float texture[] = {
            0.5f , 0,
            0, 1,
            1, 1,
            0, 1,
            1, 1,

            0, 1,
            1, 1,
            0, 1,
            1, 1,

            1, 0,
            0, 0,

            1, 0,
            0, 0,

    };


    //vertex shader code

    private String vShaderStr =
            "#version 300 es 			  \n"
                    + "uniform mat4 uMVPMatrix;     \n"
                    + "in vec4 vPosition;           \n"
                    + "in vec2 TexCoordIn;          \n"
                    + "out vec2 TexCoordOut;    \n"
                    + "void main()                  \n"
                    + "{                            \n"
                    + "   gl_Position = uMVPMatrix * vPosition;  \n"
                    + "  TexCoordOut = TexCoordIn;"
                    + "}                            \n";

    //fragment shader code.
    private String fShaderStr =
            "#version 300 es		 			          	\n"
                    + "precision mediump float;					  	\n"
                    + "uniform sampler2D TexCoordIn; \n"
                    + "out vec4 fragColor;	 			 		  	\n"
                    + "in vec2 TexCoordOut;                    \n"
                    + "void main()                                  \n"
                    + "{                                            \n"
                    + " fragColor = texture(TexCoordIn, TexCoordOut); \n"  //
                    + "}                                            \n";

    public void loadTexture(int texture, Context context) {
        InputStream imagestream = context.getResources().openRawResource(texture);
        Bitmap bitmap = null;

        android.graphics.Matrix flip = new android.graphics.Matrix();
        flip.postScale(-1f, -1f);


        try {

            bitmap = BitmapFactory.decodeStream(imagestream);

        }catch(Exception e){

        }finally {
            try {
                imagestream.close();
                imagestream = null;
            } catch (IOException e) {
            }
        }

        GLES30.glGenTextures(1, textures, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]);

        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);

        //   GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        //   GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT);

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
    }

    public hourGlass() {

        vertexBuffer = ByteBuffer
                .allocateDirect(mVerticesData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(mVerticesData);
        vertexBuffer.position(0);

        textureBuffer = ByteBuffer.allocateDirect(texture.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(texture);
        textureBuffer.position(0);

        drawListBuffer = ByteBuffer.allocateDirect(indices.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(indices);
        drawListBuffer.position(0);

        //setup the shaders
        int vertexShader;
        int fragmentShader;
        int programObject;

        // Load the vertex/fragment shaders
        vertexShader = myRenderer.LoadShader(GLES30.GL_VERTEX_SHADER, vShaderStr);
        fragmentShader = myRenderer.LoadShader(GLES30.GL_FRAGMENT_SHADER, fShaderStr);

        // Create the program object
        programObject = GLES30.glCreateProgram();

        if (programObject == 0) {
            Log.e("hourGlass", "So some kind of error, but what?");
            return;
        }

        GLES30.glAttachShader(programObject, vertexShader);
        GLES30.glAttachShader(programObject, fragmentShader);

        // Bind vPosition to attribute 0
        GLES30.glBindAttribLocation(programObject, 0, "vPosition");

        // Link the program
        GLES30.glLinkProgram(programObject);

        // Store the program object
        mProgramObject = programObject;

    }

    public void draw(float[] mvpMatrix) {

        GLES30.glUseProgram(mProgramObject);

        int mMVPMatrixHandle = GLES30.glGetUniformLocation(mProgramObject, "uMVPMatrix");
        int mPositionHandle = GLES30.glGetAttribLocation(mProgramObject, "vPosition");
        int vsTextureCoord = GLES30.glGetAttribLocation(mProgramObject, "TexCoordIn");

        //Käytetään tehtyä projisointi ja näkymä matriisia
        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES30.glEnableVertexAttribArray(vsTextureCoord);
        GLES30.glEnableVertexAttribArray(mPositionHandle);

        GLES30.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        GLES30.glVertexAttribPointer(vsTextureCoord, COORDS_PER_TEXTURE,
                GLES30.GL_FLOAT, false,
                textureStride, textureBuffer);


        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]);

        GLES30.glDrawElements(GLES30.GL_TRIANGLES, indices.length, GLES30.GL_UNSIGNED_SHORT, drawListBuffer);

    }

}
