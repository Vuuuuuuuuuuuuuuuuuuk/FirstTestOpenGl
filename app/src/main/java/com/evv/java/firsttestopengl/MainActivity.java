package com.evv.java.firsttestopengl;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
  GLSurfaceView glSurfaceView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    glSurfaceView = new GLSurfaceView(this);
    glSurfaceView.setEGLContextClientVersion(2);
    glSurfaceView.setRenderer(new MyRenderer(this));

    glSurfaceView.setOnTouchListener(this);
    setContentView(glSurfaceView);
  }

  @Override
  protected void onResume() {
    super.onResume();
    glSurfaceView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    glSurfaceView.onPause();
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    

    return true;
  }
}