package com.axamit.core.service;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by arseni.vorhan on 29.06.2016.
 */
public class DatabaseUpdateService {

    private static final Logger logger = Logger.getLogger(DatabaseUpdateService.class);

    public static String downloadSitesDump(String fileURL, int bufferSize ) throws IOException {

        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        String saveFilePath = "";

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            int index = disposition != null ? disposition.indexOf("filename=") : 0;
            if (disposition != null && index > 0) {
                // extracts file name from header field
                fileName = disposition.substring(index + 10,
                        disposition.length() - 1);
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }


            logger.info("Content-Type = " + contentType);
            logger.info("Content-Disposition = " + disposition);
            logger.info("Content-Length = " + contentLength);
            logger.info("fileName = " + fileName);

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            saveFilePath = "D:\\!111" + File.separator + fileName;

            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[bufferSize];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            logger.info("File downloaded");
        } else {
            logger.debug("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();

        return saveFilePath;
    }


    public static void gunzipFile(String inputGzipFile, String outputFile, int bufferSize){


        byte[] buffer = new byte[bufferSize];

        try(GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(inputGzipFile));
            FileOutputStream out = new FileOutputStream( outputFile)){

            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static void unzipFile(String inputZipFile, String outputFile, int bufferSize){


        byte[] buffer = new byte[bufferSize];

        try(ZipInputStream zis = new ZipInputStream(new FileInputStream(inputZipFile));
            ){

//            ZipInputStream zis =
//                    new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while(ze!=null){

                String fileName = ze.getName();
                File newFile = new File(outputFile + File.separator + fileName);

                System.out.println("file unzip : "+ newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

//            int len;
//            while ((len = gzis.read(buffer)) > 0) {
//                out.write(buffer, 0, len);
//            }

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }


}

