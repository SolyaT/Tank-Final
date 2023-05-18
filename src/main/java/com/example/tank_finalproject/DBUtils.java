package com.example.tank_finalproject;

import javafx.scene.control.Alert;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    public static void main(String[] args) {
        // Параметры подключения к базе данных
        String url = "jdbc:mysql://localhost:3306/tank-final";
        String username = "root";
        String password = "sae030721";

        showMessageAndRestart("Hello");

        // Подключение к базе данных
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Подключение успешно!");

            // Здесь можно выполнять операции с базой данных
            JOptionPane.showMessageDialog(null, "Hello");
            // Закрываем подключение
            connection.close();
            System.out.println("Подключение закрыто!");
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }

    private static void showMessageAndRestart(String message) {
        int result = JOptionPane.showOptionDialog(
                null,
                message,
                "Message",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"OK"},
                "OK");

        if (result == JOptionPane.OK_OPTION) {
            restartApplication();
        }
    }

    private static void restartApplication() {
        final String javaBin = System.getProperty("java.home") + "/bin/java";
        final String currentJar = new java.io.File(DBUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();

        if (!currentJar.endsWith(".jar")) {
            return; // Not running from JAR
        }

        try {
            final ProcessBuilder builder = new ProcessBuilder(javaBin, "-jar", currentJar);
            builder.start();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




