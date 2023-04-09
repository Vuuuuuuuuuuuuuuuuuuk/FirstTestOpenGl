package com.evv.java.firsttestopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

public class MyRenderer implements GLSurfaceView.Renderer {
  Context context;

  private final float[] modelMatrix = new float[16];
  private final float[] projMatrix = new float[16];
  private final float[] viewMatrix = new float[16];

  private static final String U_MODEL_MATRIX = "u_ModelMatrix";
  private int uModelMatrixLocation;
  private static final String U_PROJECTION_MATRIX = "u_ProjMatrix";
  private int uProjMatrixLocation;
  private static final String U_VIEW_MATRIX = "u_ViewMatrix";
  private int uViewMatrixLocation;

  private FloatBuffer vertexesData;
  private static final int BYTES_PER_FLOAT = 4;
  private static final int POSITION_COMPONENT_COUNT = 3;
  private static final int TEXTURE_COMPONENT_COUNT = 2;
  private static final int COLOR_COMPONENT_COUNT = 3;
  private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

  private int program;
  private static final String A_POSITION = "a_Position";
  private int aPositionLocation;
  private static final String U_TEXTURE_UNIT = "u_TextureUnit";            //униформ для тукстуры в фрагментном шейдере
  private int uTextureUnitLocation;
  private static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";     //атрибут для координат текстуры в вершинном шейдере
  private int aTextureCoordinatesLocation;
  private static final String A_COLOR = "a_Color";
  private int aColorLocation;


  public MyRenderer(Context context) {
    this.context = context;

    float vertexes[] = new float[]{
      //triangle_fan
      0.5f,  0.5f,  0.0f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f,    //center
      0.25f, 0.25f, 0.0f, 0.0f, 0.0f, 0.3f, 0.0f, 0.0f,    //left-bottom
      0.75f, 0.25f, 0.0f, 1.0f, 0.0f, 0.3f, 0.0f, 0.0f,    //right-bottom
      0.75f, 0.75f, 0.0f, 1.0f, 1.0f, 0.3f, 0.0f, 0.0f,    //right-up
      0.25f, 0.75f, 0.0f, 0.0f, 1.0f, 0.3f, 0.0f, 0.0f,    //left-up
      0.25f, 0.25f, 0.0f, 0.0f, 0.0f, 0.3f, 0.0f, 0.0f,    //left-bottom

      0.5f,  0.5f,  0.2f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f,     //center
      0.25f, 0.25f, 0.2f, 0.0f, 0.0f, 0.3f, 1.0f, 0.0f,     //left-bottom
      0.75f, 0.25f, 0.2f, 1.0f, 0.0f, 0.3f, 1.0f, 0.0f,     //right-bottom
      0.75f, 0.75f, 0.2f, 1.0f, 1.0f, 0.3f, 1.0f, 0.0f,     //right-up
      0.25f, 0.75f, 0.2f, 0.0f, 1.0f, 0.3f, 1.0f, 0.0f,     //left-up
      0.25f, 0.25f, 0.2f, 0.0f, 0.0f, 0.3f, 1.0f, 0.0f,     //left-bottom
    };

    vertexesData = ByteBuffer.allocateDirect(vertexes.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
    vertexesData.put(vertexes);
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
       //НОВЫЕ ШЕЙДЕРЫ !!!
    int vertexShader = ShaderHelper.compileShader(RawReader.textFromRaw(context, R.raw.vertex_texture_orto), GL_VERTEX_SHADER);
    int fragmentShader = ShaderHelper.compileShader(RawReader.textFromRaw(context, R.raw.fragment_shader_with_matrix), GL_FRAGMENT_SHADER);

    program = ShaderHelper.LinkProgram(vertexShader, fragmentShader);
    glUseProgram(program);

        //получение размещения текстуры и указание читать из текстуры НОМЕР 0
    uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
    glUniform1i(uTextureUnitLocation, 0);

        //получение идентификатора текстуры, активизация ТЕКСТУРЫ В КОНВЕЕРЕ С НОМЕРОМ 0, и связка их
    int texture = TextureHelper.loadTexture(context, R.drawable.sample1);
    glBindTexture(GL_TEXTURE_2D, texture);
    glActiveTexture(GL_TEXTURE0);

        //получение координат текстуры
    aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    vertexesData.position(POSITION_COMPONENT_COUNT);
    glVertexAttribPointer(aTextureCoordinatesLocation, TEXTURE_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexesData );
    glEnableVertexAttribArray(aTextureCoordinatesLocation);

    //
    aPositionLocation = glGetAttribLocation(program, A_POSITION);
    vertexesData.position(0);
    glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexesData );
    glEnableVertexAttribArray(aPositionLocation);

    //
    aColorLocation = glGetAttribLocation(program, A_COLOR);
    vertexesData.position(5);
    glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexesData );
    glEnableVertexAttribArray(aColorLocation);

    //
    uModelMatrixLocation  = glGetUniformLocation(program, U_MODEL_MATRIX);
    uProjMatrixLocation   = glGetUniformLocation(program, U_PROJECTION_MATRIX);
    uViewMatrixLocation   = glGetUniformLocation(program, U_VIEW_MATRIX);
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    setIdentityM(modelMatrix, 0);

    scaleM(modelMatrix,0,2.0f,2.0f,1.0f);
    translateM(modelMatrix,0,-0.5f,-0.5f,0f);

    //orthoM(projMatrix,0,-1,1,-1,1,-3,4);
    perspectiveM(projMatrix,0,45,(float) width / (float) height,0,100);

    glViewport(0,0,width,height);
  }


  private float angle = 0.0f, radius = 10f;
  @Override
  public void onDrawFrame(GL10 gl) {


    glUniformMatrix4fv(uModelMatrixLocation, 1, false, modelMatrix, 0);
    glUniformMatrix4fv(uProjMatrixLocation,  1, false, projMatrix, 0);

    angle += 0.01f;
    Matrix.setLookAtM(viewMatrix, 0, (float)Math.cos(angle) * radius, 0, (float)Math.sin(angle) * radius,
      0, 0, 0, 0, 1, 0);
    glUniformMatrix4fv(uViewMatrixLocation, 1, false, viewMatrix, 0);

    glClear(GL_COLOR_BUFFER_BIT);
    glDrawArrays(GL_TRIANGLE_FAN, 0, 12);

  }
}