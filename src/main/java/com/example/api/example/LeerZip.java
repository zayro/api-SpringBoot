package com.example.api.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LeerZip {

    // Relative Path ./ From Folder of Project
    // Absolute Path / From Disk of Project

    private static String ARCHIVO_ZIP = "./src/main/resources/files/archivos.zip";
    private static String RUTA_SALIDA = "./src/main/resources/files/unzip";

    List<String> ListaArchivos = new ArrayList<>();
    List<String> ListaArchivosAbsoluta = new ArrayList<>();

    public static void main(String[] args) {
        LeerZip leerZip = new LeerZip();
        // leerZip.unZip(ARCHIVO_ZIP, RUTA_SALIDA);
        // leerZip.deleteListFiles();
        // leerZip.listFilesForFolder(RUTA_SALIDA);
        leerZip.stringZip("C19680292_0830099400041_1087.zip.gato.perro");
    }

    public void unZip(String archivoZip, String rutaSalida) {

        byte[] buffer = new byte[1024];

        try {

            File pathLoad = new File(ARCHIVO_ZIP);

            if (pathLoad.exists()) {
                System.out.println("--- File Exist() ---");
            } else {
                throw new Exception("Error al encontrar rura del archivo");
            }

            File folder = new File(RUTA_SALIDA);

            if (!folder.exists()) {
                folder.mkdir();
            }

            ZipInputStream zis = new ZipInputStream(new FileInputStream(archivoZip));
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String nombreArchivo = ze.getName();
                ListaArchivos.add(nombreArchivo);
                File archivoNuevo = new File(rutaSalida + File.separator + nombreArchivo);
                ListaArchivosAbsoluta.add(archivoNuevo.getAbsoluteFile().toString());
                System.out.println("archivo descomprimido : " + archivoNuevo.getAbsoluteFile());
                new File(archivoNuevo.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(archivoNuevo);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            System.out.println("Listo");
            System.out.println("######################");
            System.out.println("ListaArchivos: " + ListaArchivos);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void listFilesForFolder(String folderFiles) {
        File folder = new File(folderFiles);
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(String.valueOf(fileEntry));
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }

    public void deleteListFiles() {
        for (int i = 0; i < ListaArchivosAbsoluta.size(); i++) {

            // Print all elements of List
            System.out.println(ListaArchivosAbsoluta.get(i));

            File f = new File(ListaArchivosAbsoluta.get(i)); // file to be delete
            if (f.delete()) // returns Boolean value
            {
                System.out.println(f.getName() + " deleted"); // getting and printing the file name
            } else {
                System.out.println("failed");
            }
        }

    }

    public void stringZip(String name) {

        System.out.println(name);

        String[] resultSplit = name.split("[.]");

        System.out.println(resultSplit[0]);

        String[] resultData = resultSplit[0].split("_");

        for (String data : resultData){
            System.out.println(data);
        }
    }
}
