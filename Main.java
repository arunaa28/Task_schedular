package com.promanage;

import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        try (Connection con = DB.get()) {
            while (true) {
                System.out.println("\n1) Add Project  2) Run Scheduler  0) Exit");
                int ch = Integer.parseInt(sc.nextLine());
                if (ch == 0) break;

                if (ch == 1) {
                    System.out.print("Title: "); String t = sc.nextLine();
                    System.out.print("Deadline (days 1..n): "); int d = Integer.parseInt(sc.nextLine());
                    System.out.print("Revenue: "); int r = Integer.parseInt(sc.nextLine());
                    System.out.print("Week No: "); int w = Integer.parseInt(sc.nextLine());

                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO projects(title,deadline,revenue,week_no) VALUES (?,?,?,?)")) {
                        ps.setString(1, t); ps.setInt(2, d); ps.setInt(3, r); ps.setInt(4, w);
                        ps.executeUpdate();
                    }
                }

                if (ch == 2) {
                    System.out.print("Current Week No: ");
                    int w = Integer.parseInt(sc.nextLine());
                    Scheduler.run(w);
                }
            }
        }
    }
}