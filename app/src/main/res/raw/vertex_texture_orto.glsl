uniform mat4 u_ProjMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ModelMatrix;

attribute vec4 a_Position;
attribute vec4 a_Color;
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinates;
varying vec4 v_Color;

void main(){
    v_TextureCoordinates = a_TextureCoordinates;
    v_Color = a_Color;
    gl_Position = u_ProjMatrix * u_ViewMatrix * u_ModelMatrix * a_Position;
}