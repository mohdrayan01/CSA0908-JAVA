import java.sql.*;
public class LoginDao{
 public static boolean unsafe(Connection c,String id,String pwd)throws SQLException{
  String sql="SELECT * FROM users WHERE id='"+id+"' AND pwd='"+pwd+"'";
  try(Statement s=c.createStatement();ResultSet r=s.executeQuery(sql)){return r.next();}
 }
 public static boolean safe(Connection c,String id,String pwd)throws SQLException{
  String sql="SELECT 1 FROM users WHERE id=? AND pwd=?";
  try(PreparedStatement p=c.prepareStatement(sql)){p.setString(1,id);p.setString(2,pwd);try(ResultSet r=p.executeQuery()){return r.next();}}
 }
}