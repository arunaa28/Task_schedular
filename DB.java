package com.promanage;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    public static Connection get() throws Exception {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/promanage",
                "postgres",
                "1234"
        );
    }
}