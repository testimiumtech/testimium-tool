package com.testimium.tool.mavenlibs;

import java.io.IOException;

public interface IMaven {

    void execute(String cmdStatement) throws IOException, InterruptedException;
}
