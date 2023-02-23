package com.evv.java.firsttestopengl;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RawReader {
  public static String textFromRaw(Context context, int rawID){
    StringBuilder text = new StringBuilder();

    try {
      InputStream inputStream = context.getResources().openRawResource(rawID);
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      BufferedReader reader = new BufferedReader(inputStreamReader);

      for(String line; (line = reader.readLine()) != null; ) text.append(line);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return text.toString();
  }

}
