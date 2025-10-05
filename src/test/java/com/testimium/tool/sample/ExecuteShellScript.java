package com.testimium.tool.sample;

import java.io.IOException;

public class ExecuteShellScript {
    public static void main(String[] args) throws IOException {
        Process exeProcess  = Runtime.getRuntime().exec("sh -c sudo chmod -R 777 ");
        exeProcess.info().toString();
    }
}
