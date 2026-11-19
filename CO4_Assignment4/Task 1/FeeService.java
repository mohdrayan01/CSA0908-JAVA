import java.sql.*;
public class FeeService {
    private final String url, user, password;
    public FeeService(String url, String user, String password) {
        this.url=url; this.user=user; this.password=password;
    }
    public String pay(String registrationNumber, double amount) throws SQLException {
        String id=registrationNumber==null ? "" : registrationNumber.trim();
        if (!id.matches("[A-Za-z0-9-]{4,20}"))
            throw new IllegalArgumentException("Enter a valid registration number.");
        if (!Double.isFinite(amount) || amount<=0)
            throw new IllegalArgumentException("Enter a positive fee amount.");
        String sql="INSERT INTO fees (reg_no, amount, paid_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (Connection c=DriverManager.getConnection(url,user,password);
             PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,id); ps.setDouble(2,amount); ps.executeUpdate();
        }
        return String.format("Payment recorded for %s: %.2f",id,amount);
    }
}