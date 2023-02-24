package com.evv.java.firsttestopengl;

import static android.opengl.GLES20.*;

public class ShaderHelper {
  public static int compileShader(String code, int typeShader){
    int shader = glCreateShader(typeShader);

    if(shader == 0) return 0;

    int compiled[] = new int[1];
    glShaderSource(shader, code);
    glCompileShader(shader);
    glGetShaderiv(shader, GL_COMPILE_STATUS, compiled, 0);

    if(compiled[0] == 0){
      glDeleteShader(shader);
      return 0;
    }

    return shader;
  }

  public static int LinkProgram(int vShader, int fShader){
    int program = glCreateProgram();

    if(program == 0) return 0;

    int linked[] = new int[1];
    glAttachShader(program, vShader);
    glAttachShader(program, fShader);
    glLinkProgram(program);
    glGetProgramiv(program, GL_LINK_STATUS, linked, 0);

    if(linked[0] == 0){
      glDeleteProgram(program);
      return 0;
    }

    return program;
  }
}
