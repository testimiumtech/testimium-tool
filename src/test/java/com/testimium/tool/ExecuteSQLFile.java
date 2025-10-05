package com.testimium.tool;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.ibatis.jdbc.ScriptRunner;
public class ExecuteSQLFile {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        //Registering the Driver
        DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
        //Getting the connection
        String mysqlUrl = "jdbc:jtds:sqlserver://localhost:1433/testimium";
        Connection con = DriverManager.getConnection(mysqlUrl, "abcd", "abcd");
        System.out.println("Connection established......");
        //Initialize the script runner
        ScriptRunner sr = new ScriptRunner(con);
        sr.setSendFullScript(false);
        //sr.setRemoveCRs(true);
        //sr.setStopOnError(true);
        //sr.setEscapeProcessing(true);
        //Creating a reader object
        Reader reader = new BufferedReader(new FileReader("C:\\Sandeep\\Projects\\testimium\\POC\\Selenium\\codebase\\SmartTestingToolFramework\\sttf\\SmartTestingFramework\\testfiles\\Sample\\SampleSQL.sql"));
        //Running the script
        sr.runScript(reader);
    }
}
