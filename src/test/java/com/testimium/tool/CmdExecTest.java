package com.testimium.tool;

import com.testimium.tool.utility.FileUtility;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmdExecTest {
    public static void main(String argv[]) {
        try {
            System.out.println(FileUtility.getSeparatedFileName("SetModulesForCumulusTier.sql")[2]);
            String line;
           /* Process p = Runtime.getRuntime().exec
                    ("psql -U username -d dbname -h serverhost -f scripfile.sql");*/
            Process p = Runtime.getRuntime().exec
                    ("sqlcmd -E -S localhost -v database=testimium -e -i SetModulesForCumulusTier.sql");
            /*ProcessBuilder processBuilder = new ProcessBuilder("sqlcmd -E -S localhost", " -e -i SetModulesForCumulusTier.sql");
            Process p = processBuilder.start();*/
            int exitCode = p.waitFor();
            if(exitCode == 0) {
                BufferedReader input =
                        new BufferedReader
                                (new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                input.close();
            } else {
                BufferedReader input =
                        new BufferedReader
                                (new InputStreamReader(p.getErrorStream()));
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                input.close();
            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }
}
