package com.example.api.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class UnZip {

    public List<String> unZipFile(String archivoZip, String rutaSalida) {

        byte[] buffer = new byte[1024];
        List<String> listaArchivos = new ArrayList<String>();

        try {
            File folder = new File(rutaSalida);
            if (!folder.exists()) {
                folder.mkdir();
                throw new Exception("archivo no existe");
            }
            ZipInputStream zis = new ZipInputStream(new FileInputStream(archivoZip));
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String nombreArchivo = ze.getName();
                File archivoNuevo = new File(rutaSalida + File.separator + nombreArchivo);
                listaArchivos.add(archivoNuevo.getAbsoluteFile().toString());
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
            return listaArchivos;
        } catch (IOException ex) {
            System.out.println("IOException" + ex);
            ex.printStackTrace();
            listaArchivos.add("error");
            return listaArchivos;
        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
        return listaArchivos;
    }
}
