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

  private int program;
  private static final String U_COLOR = "u_Color";
  private int uColorLocation;
  private static final String A_POSITION = "a_Position";
  private int aPositionLocation;

  public MyRenderer(Context context) {
    this.context = context;

    float vertexes[] = new float[]{};

    vertexesData = ByteBuffer.allocateDirect(vertexes.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
    vertexesData.put(vertexes);
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    int vertexShader = ShaderHelper.compileShader(RawReader.textFromRaw(context, R.raw.vertex_shader_program), GL_VERTEX_SHADER);
    int fragmentShader = ShaderHelper.compileShader(RawReader.textFromRaw(context, R.raw.fragment_shader_program), GL_FRAGMENT_SHADER);

    program = ShaderHelper.LinkProgram(vertexShader, fragmentShader);

    glUseProgram(program);

    uColorLocation = glGetUniformLocation(program, U_COLOR);

    aPositionLocation = glGetAttribLocation(program, A_POSITION);
    vertexesData.position(0);
    glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexesData );
    glEnableVertexAttribArray(aPositionLocation);
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    glViewport(0,0,width,height);
  }

  @Override
  public void onDrawFrame(GL10 gl) {

  }
}
