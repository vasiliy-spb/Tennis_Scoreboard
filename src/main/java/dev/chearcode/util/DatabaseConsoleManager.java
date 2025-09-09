package dev.chearcode.util;

import org.h2.tools.Server;

import java.sql.SQLException;

public final class DatabaseConsoleManager {
    private DatabaseConsoleManager() {
    }

    public static void runConsoleServer(int port) {
        try {
            Server h2ConsoleServer = Server.createWebServer(
                    "-web",
                    "-webAllowOthers",
                    "-webPort", String.valueOf(port)
            );
            h2ConsoleServer.start();
            System.out.println("H2 Console is available at: http://localhost:" + port);
        } catch (SQLException e) {
            System.err.println("Failed to start H2 Console: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
