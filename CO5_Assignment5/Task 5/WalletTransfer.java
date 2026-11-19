import java.sql.*;
public class WalletTransfer{
 public static void transfer(Connection c,int from,int to,double amount)throws SQLException{
  if(amount<=0)throw new IllegalArgumentException("Amount must be positive");
  boolean old=c.getAutoCommit();c.setAutoCommit(false);
  try(PreparedStatement debit=c.prepareStatement("UPDATE wallet SET balance=balance-? WHERE id=? AND balance>=?");
      PreparedStatement credit=c.prepareStatement("UPDATE wallet SET balance=balance+? WHERE id=?")){
   debit.setDouble(1,amount);debit.setInt(2,from);debit.setDouble(3,amount);
   if(debit.executeUpdate()!=1)throw new SQLException("Missing wallet or insufficient funds");
   credit.setDouble(1,amount);credit.setInt(2,to);
   if(credit.executeUpdate()!=1)throw new SQLException("Destination wallet not found");
   c.commit();
  }catch(SQLException|RuntimeException e){c.rollback();throw e;}
  finally{c.setAutoCommit(old);}
 }
}