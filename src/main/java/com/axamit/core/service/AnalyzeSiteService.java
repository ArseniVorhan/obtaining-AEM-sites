package com.axamit.core.service;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by arseni.vorhan on 29.06.2016.
 */
public class AnalyzeSiteService {


    private static String[] AEM_INDICATES = {
    "/etc/clientlibs",
    "/etc/designs",
    "/content",
    "/content/dam",
    "assets.adobedtm.com",
    "parsys",
    "granite",
    "/apps",
    "jcr:content",
    "<cq",
    "cq-",
    "colctrl",
    "/libs/wcm",
    "/foundation/components"};

    private static final Logger logger = Logger.getLogger(AnalyzeSiteService.class);


    public static boolean isAEMPowered(String siteUrl) throws IOException {

        URL obj = new URL(siteUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();

        logger.info("Sending 'GET' request to URL : " + siteUrl);
        logger.info("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            if(isContainsAEMIndicates(inputLine)){
                return true;
            }

        }
        in.close();

        return false;
    }

    private static boolean isContainsAEMIndicates(String inputLine) {

        for (String aemIndicate : AEM_INDICATES) {
            if (inputLine.contains(aemIndicate)){
                return true;
            }
        }
        return false;
    }
}
