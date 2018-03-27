package com.shumidub.todoapprealm.sync;

import android.os.Environment;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by A.shumidub on 19.03.18.
 */

public class FileWritter {

    private static final String FILE_NAME = "REALM_BD_JSON.txt";

    public static void saveFile(String json){


        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), FILE_NAME);

        if(file.exists())  file.delete();


        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        FileOutputStream stream = null;
        try {

            stream = new FileOutputStream(file);
            stream.write(json.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e2){
                e2.printStackTrace();
            }
        }

    }



    public static String readFile(){

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), FILE_NAME);


        int length = (int) file.length();

        byte[] bytes = new byte[length];

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                in.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String json = new String(bytes);

        return json;
    }


    public static boolean isBackupExist(){
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), FILE_NAME);
        return file.exists();
    }

}
