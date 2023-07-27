package com.rest.api.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileZip {


    // Relative Path ./ From Folder of Project
    // Absolute Path / From Disk of Project

    private static String ARCHIVO_ZIP = "./src/main/resources/files/documents.zip";
    private static String RUTA_SALIDA = "./build/tmp/files";

    List<String> ListaNombreArchivos = new ArrayList<>();
    List<String> ListaArchivos = new ArrayList<>();
    List<String> ListaArchivosAbsoluta = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        FileZip fileZip = new FileZip();
        //fileZip.unZip(ARCHIVO_ZIP, RUTA_SALIDA);
        fileZip.unZipTmp(ARCHIVO_ZIP);

        fileZip.listFilesForFolder(RUTA_SALIDA);
        fileZip.stringZip("C19680292_0830099400041_1087.zip.gato.perro");

        Thread.sleep(20000);
        fileZip.deleteListFiles();

    }

    public void unZip(String archivoZip, String rutaSalida) {

        System.out.println("######################");
        System.out.println("FileZip.unZip");
        System.out.println("######################");

        try {

        byte[] buffer = new byte[1024];

            File pathLoad = new File(ARCHIVO_ZIP);

            if (!pathLoad.exists()) {
                if(pathLoad.mkdir()){
                    System.out.println("--- INTENTA CREAR EL DIRECTORIO ---");
                } else {
                    throw new Exception(" NO SE PUEDE CREAR EL DIRECTORIO");
                }
            }


            if (pathLoad.exists()) {
                System.out.println("--- File Exist() ---");
            } else {
                throw new Exception("Error al encontrar rura del archivo");
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
        } finally {

        }
    }

    public void unZipTmp(String archivoZip )   {

        System.out.println("######################");
        System.out.println("FileZip.unZipTmp");
        System.out.println("######################");

        try {

            byte[] buffer = new byte[1024];

            File pathLoad = new File(ARCHIVO_ZIP);

            if (!pathLoad.exists()) {
                if(pathLoad.mkdir()){
                    System.out.println("--- INTENTA CREAR EL DIRECTORIO ---");
                } else {
                    throw new Exception(" NO SE PUEDE CREAR EL DIRECTORIO");
                }
            }


            if (pathLoad.exists()) {
                System.out.println("--- File Exist() ---");
            } else {
                throw new Exception("Error al encontrar rura del archivo");
            }


            // obtiene el zip
            ZipInputStream zis = new ZipInputStream(new FileInputStream(archivoZip));
            // obtiene el files del zip
            ZipEntry ze = zis.getNextEntry();
            // recorre el zip
            while (ze != null) {
                String nombreArchivo = ze.getName();
                ListaNombreArchivos.add(nombreArchivo);
                File archivoNuevo = new File("./build/tmp/files"+ File.separator + nombreArchivo);
                ListaArchivos.add(archivoNuevo.getPath().toString());
                ListaArchivosAbsoluta.add(archivoNuevo.getAbsoluteFile().toString());


                System.out.println("archivo descomprimido : " + archivoNuevo.getAbsoluteFile());



                if(new File(archivoNuevo.getParent()).mkdirs()){

                    throw new IOException("Failed to create directory " + archivoNuevo);

                }

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
            System.out.println("######################");
            System.out.println("ListaArchivosAbsoluta: " + ListaArchivosAbsoluta);

            List<String> file = ListaArchivosAbsoluta;

            String fileSendOcr = file.get(0);

            File load = new File(fileSendOcr);

            if (load.isFile()) {
                System.out.println("--- File Exist() ---" + fileSendOcr);

            }



        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

    public void listFilesForFolder(String folderFiles) {

        System.out.println("######################");
        System.out.println("FileZip.listFilesForFolder");
        System.out.println("######################");

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

        System.out.println("######################");
        System.out.println("FileZip.deleteListFiles");
        System.out.println("######################");
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

        System.out.println("######################");
        System.out.println("FileZip.stringZip");
        System.out.println("######################");

        System.out.println(name);

        String[] resultSplit = name.split("[.]");

        System.out.println(resultSplit[0]);

        String[] resultData = resultSplit[0].split("_");

        for (String data : resultData){
            System.out.println(data);
        }
    }
}
