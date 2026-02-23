package com.promanage;

import java.sql.*;
import java.util.*;

public class Predictor {
    public static double expectedNextWeekRevenue(Connection con) throws Exception {
        List<Integer> last = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT avg_revenue FROM revenue_history ORDER BY week_no DESC LIMIT 3")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) last.add(rs.getInt(1));
        }
        double[] w = {0.5, 0.3, 0.2};
        double pred = 0;
        for (int i = 0; i < Math.min(3, last.size()); i++) pred += last.get(i) * w[i];
        return pred == 0 ? 0 : pred;
    }
}