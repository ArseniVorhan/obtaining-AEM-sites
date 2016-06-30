package com.axamit.core.main;

import com.axamit.core.service.AnalyzeSiteService;
import com.axamit.core.service.DatabaseUpdateService;
import org.apache.log4j.Logger;

import java.io.IOException;

public class App {

	private static final Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) throws IOException {

        boolean isAEMPowered = AnalyzeSiteService.isAEMPowered("http://www.4point.com/");

//        String filePath = "D:\\!111\\httparchive_Jun_15_2016_pages.csv.gz";
        String filePath = DatabaseUpdateService.downloadSitesDump(
                "http://s3.amazonaws.com/alexa-static/top-1m.csv.zip", 131072);
        DatabaseUpdateService.unzipFile(filePath ,"D:\\!111", 131072);


        System.out.println(isAEMPowered);

    }
}
