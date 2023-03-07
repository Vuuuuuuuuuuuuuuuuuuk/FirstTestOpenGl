package com.evv.java.firsttestopengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.texImage2D;

public class TextureHelper {
  public static int loadTexture(Context context, int resourceId) {
    final int[] textureObjectIds = new int[1];
    glGenTextures(1, textureObjectIds, 0);

    if (textureObjectIds[0] == 0) return 0;
        //оригинальный размер
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inScaled = false;
        //декодировка из png в битовую карту
    final Bitmap bitmap = BitmapFactory.decodeResource(
      context.getResources(), resourceId, options);

    if (bitmap == null) {
      glDeleteTextures(1, textureObjectIds, 0);
      return 0;
    }
        //связываем данный текстурный обьект с 2D текстурой
    glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
        //указываем как трансформировать текстуру при растяжении и сжатии
        //сжатие
        //GL_NEAREST
        //GL_NEAREST_MIPMAP_NEAREST
        //GL_NEAREST_MIPMAP_LINEAR
        //GL_LINEAR
        //GL_LINEAR_MIPMAP_NEAREST
        //GL_LINEAR_MIPMAP_LINEAR
        //растяжение
        //GL_NEAREST
        //GL_LINEAR
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        //загружаем битовую карту в конвеер
    texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        //освобождаем битовую карту
    bitmap.recycle();
        //генерируем MIPMAP
    glGenerateMipmap(GL_TEXTURE_2D);
        //отвязываем данный текстурный обьект от 2D текстуры
    glBindTexture(GL_TEXTURE_2D, 0);

    return textureObjectIds[0];
  }
}
