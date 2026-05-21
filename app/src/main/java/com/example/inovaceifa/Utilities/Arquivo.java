package com.example.inovaceifa.Utilities;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Classe para manipulação dos arquivos utilizados na aplicação. Atualmente cuida da Escrita, Leitura
 * e Exclusão do Arquivo.
 *
 * @author Gustavo Henrique Tostes
 * @version 1.0 - mar. 2023
 */
public class Arquivo {

    /**
     * Método de escrita no arquivo destino. Concatena dados, ou seja, não sobrescreve os dados
     * já inseridos.
     * @param data Dado a ser inserido no arquivo, em formato de String.
     * @param context Contexto da aplicação, para "resgatar" o diretório.
     * @param fileName Nome do arquivo manipulado (***.txt).
     */
    public static void writeFile(String data, Context context, String fileName) {
        File path = context.getFilesDir();
        File file = new File(path, fileName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("Exception", "Arquivo nao encontrado: ");
        } catch (IOException e) {
            Log.e("Exception", "Escrita falhou: ");
        }
    }

    /**
     * Método de escrita no arquivo destino. Se houver dados no arquivo, o método os sobrescreve.
     * @param data {@link String} contendo os dados a serem inseridos no arquivo.
     * @param context {@link Context} contexto da aplicação
     * @param fileName {@link String} contendo o nome do arquivo utilizado.
     */
    public static void writeFileOver(String data, Context context, String fileName) {
        File path = context.getFilesDir();
        File file = new File(path, fileName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("Exception", "Arquivo nao encontrado: ");
        } catch (IOException e) {
            Log.e("Exception", "Escrita falhou: ");
        }
    }

    /**
     * Método de leitura do arquivo manipulado. Retorna todos os dados contidos no arquivo em questão.
     * @param context Contexto da aplicação para "resgatar" o diretório em que o arquivo se encontra.
     * @param fileName Nome do arquivo manipulado (***.txt).
     * @return Retorna uma String contendo todos os dados contidos no arquivo manipulado. PODE SER
     * TROCADA PARA UMA LISTA // PILHA // ARRAY.
     */
    public static String readFile(Context context, String fileName) {
        File path = context.getFilesDir();
        File file = new File(path, fileName);

        int length = (int) file.length();

        byte[] bytes = new byte[length];

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
        } catch (FileNotFoundException e) {
            Log.e("Exception", "Arquivo nao encontrado: " + e);
        } catch (IOException e) {
            Log.e("Exception", "Leitura falhou: " + e);
        }

        String a = new String(bytes);

        return a;
    }

    /**
     * Método de exclusão de um arquivo específico do sistema, sendo que o mesmo encontra-se em um
     * diretório interno da aplicação (inacessível ao usuário).
     * @param context Contexto da aplicação para "resgatar" o diretório em que o arquivo se encontra.
     * @param fileName Nome do arquivo manipulado (***.txt).
     */
    public static void deleteFile(Context context, String fileName) {
        File path = context.getFilesDir();
        File file = new File(path, fileName);
        file.delete();
    }
}
