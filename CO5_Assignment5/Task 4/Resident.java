import java.io.Serializable;
public class Resident implements Serializable{
 private static final long serialVersionUID=1L;
 private final String name,registerNo;private transient String password;private transient java.sql.Connection connection;
 public Resident(String n,String r,String p){name=n;registerNo=r;password=p;}
 public String getName(){return name;}public String getRegisterNo(){return registerNo;}
}