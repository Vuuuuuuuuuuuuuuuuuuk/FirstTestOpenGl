package com.evv.java.firsttestopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;

public class MyRenderer implements GLSurfaceView.Renderer {
  Context context;

  private FloatBuffer vertexesData;
  private static final int BYTES_PER_FLOAT = 4;
  private static final int POSITION_COMPONENT_COUNT = 2;
  private static final int COLOR_COMPONENT_COUNT = 3;
  private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

  private int program;
  //private static final String U_COLOR = "u_Color";
  //private int uColorLocation;
  private static final String A_POSITION = "a_Position";
  private int aPositionLocation;
  private static final String A_COLOR = "a_Color";
  private int aColorLocation;


  public MyRenderer(Context context) {
    this.context = context;

    float vertexes[] = new float[]{
     /* //triangle
      -0.5f, 0.5f,
      -0.25f, 0.75f,
      -0.75f, 0.75f,*/

      //triangle_fan
      0.5f, 0.5f, 1.0f, 1.0f, 1.0f,       //white
      0.25f, 0.25f, 0.5f, 0.0f, 0.0f,     //red
      0.75f, 0.25f, 0.0f, 0.5f, 0.0f,     //green
      0.75f, 0.75f, 0.0f, 0.0f, 0.5f,     //blue
      0.25f, 0.75f, 0.0f, 0.0f, 0.0f,     //black
      0.25f, 0.25f, 0.5f, 0.0f, 0.0f,     //red
/*
      //triangle_strip
      0.2f, -0.6f,
      0.2f, -0.2f,
      0.4f, -0.6f,
      0.6f, -0.2f,
      0.8f, -0.6f,

      //line
      -0.9f, -0.1f,
      -0.7f, -0.1f,
      -0.6f, -0.1f,
      -0.4f, -0.1f,

      //Line_strip
      -0.9f, -0.3f,
      -0.7f, -0.2f,
      -0.6f, -0.3f,
      -0.4f, -0.2f,

      //Line_loop
      -0.9f, -0.6f,
      -0.7f, -0.5f,
      -0.6f, -0.6f,
      -0.4f, -0.5f,

      //points
      0.4f, -0.4f,
      0.5f, -0.4f,*/
    };

    vertexesData = ByteBuffer.allocateDirect(vertexes.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
    vertexesData.put(vertexes);
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    //int vertexShader = ShaderHelper.compileShader(RawReader.textFromRaw(context, R.raw.vertex_shader_program), GL_VERTEX_SHADER);
    //int fragmentShader = ShaderHelper.compileShader(RawReader.textFromRaw(context, R.raw.fragment_shader_program), GL_FRAGMENT_SHADER);
    int vertexShader = ShaderHelper.compileShader(RawReader.textFromRaw(context, R.raw.vertex_shader_test2), GL_VERTEX_SHADER);
    int fragmentShader = ShaderHelper.compileShader(RawReader.textFromRaw(context, R.raw.fragment_shader_test2), GL_FRAGMENT_SHADER);

    program = ShaderHelper.LinkProgram(vertexShader, fragmentShader);

    glUseProgram(program);

    //uColorLocation = glGetUniformLocation(program, U_COLOR);
    aColorLocation = glGetAttribLocation(program, A_COLOR);
    vertexesData.position(POSITION_COMPONENT_COUNT);
    glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexesData );
    glEnableVertexAttribArray(aColorLocation);

    aPositionLocation = glGetAttribLocation(program, A_POSITION);
    vertexesData.position(0);
    //glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexesData );
    glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexesData );
    glEnableVertexAttribArray(aPositionLocation);
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    glViewport(0,0,width,height);
  }

  @Override
  public void onDrawFrame(GL10 gl) {
    glClear(GL_COLOR_BUFFER_BIT);

    glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    /*
    glUniform4f(uColorLocation,0.0f, 0.3f, 0.0f, 0.0f);
    glDrawArrays(GL_TRIANGLES, 0, 3);

    glUniform4f(uColorLocation, 0.5f, 0.6f, 0.0f, 0.0f);
    glDrawArrays(GL_TRIANGLE_FAN, 3, 6);

    glUniform4f(uColorLocation, 0.5f, 0.6f, 0.8f, 0.0f);
    glDrawArrays(GL_TRIANGLE_STRIP, 9, 5);

    glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 0.0f);
    glDrawArrays(GL_LINES, 14, 4);

    glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 0.0f);
    glDrawArrays(GL_LINE_STRIP, 18, 4);

    glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 0.0f);
    glDrawArrays(GL_LINE_LOOP, 22, 4);

    glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
    glDrawArrays(GL_POINTS, 26, 2);
    */
  }
}