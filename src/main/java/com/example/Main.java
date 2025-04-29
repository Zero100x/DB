package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        try {
         
          System.out.println("Conexion exitosa");
        } catch (Exception e) {
           System.out.println("Error Conexion");
        }


    }
}