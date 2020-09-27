package com.dazzhub.skywars.MySQL;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Console;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;

public class MySQL {

    public static String username;
    public static String password;
    public static String database;
    public static String host;
    public static int port;
    public static Connection con;
    public static boolean isEnabled;

    static {
        MySQL.isEnabled = false;
        MySQL.username = "";
        MySQL.password = "";
        MySQL.database = "";
        MySQL.host = "";
        MySQL.port = 3306;
    }

    public static void connect(Main plugin) {
        if (!isConnected()) {
            try {
                if (MySQL.isEnabled) {
                    Properties properties = new Properties();
                    properties.setProperty("user", MySQL.username);
                    properties.setProperty("password", MySQL.password);
                    properties.setProperty("autoReconnect", "true");
                    properties.setProperty("verifyServerCertificate", "false");
                    properties.setProperty("useSSL", "false");
                    properties.setProperty("requireSSL", "false");
                    MySQL.con = DriverManager.getConnection("jdbc:mysql://" + MySQL.host + ":" + MySQL.port + "/" + MySQL.database, properties);
                    Console.info("[MySQL] The connection to MySQL has been established!");
                } else {
                    File db = new File(plugin.getDataFolder(), MySQL.database + ".db");
                    if (!db.exists()) {
                        try {
                            db.createNewFile();
                        } catch (IOException e) {
                            Console.error("[SQL] File Write Error: " + MySQL.database + ".db");
                        }
                    }
                    Class.forName("org.sqlite.JDBC");
                    MySQL.con = DriverManager.getConnection("jdbc:sqlite:" + db);
                    Console.info("[SQLite] The connection to SQLite has been established!");

                }
            } catch (SQLException e) {
                Console.error("[MySQL] " + e.getMessage());
            } catch (ClassNotFoundException e) {
                Console.error("[SQLite] " + e.getMessage());
            }
        }
    }

    public static void disconnect() {
        if (isConnected()) {
            try {
                MySQL.con.close();
            } catch (SQLException e) {
                Console.error(e.getMessage());
            }
        }
    }

    public static boolean isConnected() {
        return MySQL.con != null;
    }

    public static void update(String qry) {
        if (isConnected()) {
            try {
                MySQL.con.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                Console.error(e.getMessage());
            }
        }
    }

    public static ResultSet getResult(String qry) {
        if (isConnected()) {
            try {
                return MySQL.con.createStatement().executeQuery(qry);
            } catch (SQLException e) {
                Console.error(e.getMessage());
            }
        }
        return null;
    }

    public static String getFirstString(ResultSet rs, int l, String re, int t) {
        try {
            while (rs.next()) {
                if (rs.getString(l).equalsIgnoreCase(re)) {
                    return rs.getString(t);
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public static Connection getConnection() {
        return MySQL.con;
    }

    public static ResultSet query(String query) throws SQLException {
        Statement stmt = MySQL.con.createStatement();
        try {
            return stmt.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
