import java.sql.*;import java.nio.file.*;
public class Migration{
 public static void main(String[] a)throws Exception{
  String url=System.getenv().getOrDefault("JAVA_DB_URL","jdbc:mysql://localhost:3306/transport");
  String user=System.getenv().getOrDefault("JAVA_DB_USER","root"),pwd=System.getenv().getOrDefault("JAVA_DB_PASSWORD","");
  try(Connection c=DriverManager.getConnection(url,user,pwd)){
   DatabaseMetaData m=c.getMetaData();
   System.out.println(m.getDatabaseProductName()+" "+m.getDatabaseProductVersion());
   System.out.println("Driver: "+m.getDriverName()+" "+m.getDriverVersion());
   try(ResultSet r=m.getTables(null,null,"%",new String[]{"TABLE"})){while(r.next())System.out.println(r.getString("TABLE_NAME"));}
  }
 }
}