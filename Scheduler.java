package com.promanage;

import java.sql.*;
import java.util.*;

public class Scheduler {

    static final int DAYS = 5; // Mon–Fri

    public static void run(int currentWeek) throws Exception {
        try (Connection con = DB.get()) {

            double expectedNext = Predictor.expectedNextWeekRevenue(con);

            List<Project> list = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT id,title,deadline,revenue,week_no FROM projects WHERE week_no=?")) {
                ps.setInt(1, currentWeek);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    list.add(new Project(
                            rs.getInt(1), rs.getString(2), rs.getInt(3),
                            rs.getInt(4), rs.getInt(5)
                    ));
                }
            }

            for (Project p : list) {
                double urgency = 1.0 / Math.max(1, p.deadline);
                double risk = (p.deadline <= DAYS) ? 1.0 : 0.0;
                double futurePenalty = (p.deadline > DAYS && p.revenue < expectedNext) ? 1.0 : 0.0;
                p.score = 0.6 * p.revenue + 0.3 * urgency + 0.1 * risk - 0.2 * futurePenalty;
            }

            list.sort((a,b) -> Double.compare(b.score, a.score));

            Project[] slots = new Project[DAYS];
            int total = 0;

            for (Project p : list) {
                int lastDay = Math.min(p.deadline, DAYS) - 1;
                for (int d = lastDay; d >= 0; d--) {
                    if (slots[d] == null) {
                        slots[d] = p;
                        total += p.revenue;
                        break;
                    }
                }
            }

            String[] days = {"Mon","Tue","Wed","Thu","Fri"};
            System.out.println("\nOptimal Schedule (Week " + currentWeek + ")");
            for (int i = 0; i < DAYS; i++) {
                if (slots[i] != null)
                    System.out.println(days[i] + " -> " + slots[i].title + " (₹" + slots[i].revenue + ")");
                else
                    System.out.println(days[i] + " -> Free");
            }
            System.out.println("Total Revenue: ₹" + total);
        }
    }
}