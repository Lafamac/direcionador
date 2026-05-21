package com.example.inovaceifa.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Classe para lidar com todas as questões relativas à imagens no asplicativo.
 *
 * @author Gustavo Henrique Tostes
 * @version 1.0 - feb. 2023
 */
public class Imagem {

    /**
     * Conversão de uma String (contendo o BitMap de uma Imagem) para o tipo BitMap.
     * @param string String a ser convertida para BitMap.
     * @return String convertida para BitMap.
     */
    public Bitmap StringToBitMap(String string) {
        try {
            byte[] encodeByte = Base64.decode(string, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    /**
     * Conversão de um BitMap para String.
     * @param bitmap BitMap a ser convertido.
     * @return BitMap convertido para String.
     */
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * Conversão de Bitmap para um vetor de bytes (byte[]).
     * @param bitmap Objeto a ser convertido.
     * @return Bitmap convertido para byte[].
     */
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    /**
     * Conversão de vetor de bytes (byte[]) para Bitmap.
     * @param image Objeto a ser convertido.
     * @return Vetor de bytes convertido para Bitmap.
     */
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    /**
     * "Retira" uma imagem, em formato Bitmap, de um arquivo.
     * @param path Caminho para "encontrar" o arquivo.
     * @return Imagem em Bitmap.
     */
    public Bitmap pathToImage(String path) {
        if ((path == null) || path.length() == 0) {
            return null;
        }

        return BitmapFactory.decodeFile(path);
    }
}
