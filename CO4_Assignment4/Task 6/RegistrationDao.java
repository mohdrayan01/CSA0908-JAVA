import java.sql.*;
import java.util.*;
public class RegistrationDao {
    private final String url,user,password;
    public RegistrationDao(String url,String user,String password){this.url=url;this.user=user;this.password=password;}
    private Connection connect()throws SQLException{return DriverManager.getConnection(url,user,password);}
    public List<CampusEvent> events()throws SQLException{
        List<CampusEvent> out=new ArrayList<>();
        try(Connection c=connect();PreparedStatement p=c.prepareStatement("SELECT event_id,event_name FROM events ORDER BY event_name");
            ResultSet r=p.executeQuery()){while(r.next())out.add(new CampusEvent(r.getInt(1),r.getString(2)));}
        return out;
    }
    public void insert(String id,String name,String email,int eventId)throws SQLException{
        String sql="INSERT INTO registrations(student_id,student_name,email,event_id) VALUES(?,?,?,?)";
        try(Connection c=connect();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,id);p.setString(2,name);p.setString(3,email);p.setInt(4,eventId);p.executeUpdate();}
    }
    public List<Registration> page(Integer eventId,int limit,int offset)throws SQLException{
        List<Registration> out=new ArrayList<>();
        String sql="SELECT r.registration_id,r.student_id,r.student_name,r.email,r.event_id,e.event_name "
            +"FROM registrations r JOIN events e ON e.event_id=r.event_id "
            +(eventId==null?"":"WHERE r.event_id=? ")+"ORDER BY r.registration_id DESC LIMIT ? OFFSET ?";
        try(Connection c=connect();PreparedStatement p=c.prepareStatement(sql)){
            int i=1;if(eventId!=null)p.setInt(i++,eventId);p.setInt(i++,limit);p.setInt(i,offset);
            try(ResultSet r=p.executeQuery()){while(r.next())out.add(new Registration(r.getInt(1),r.getString(2),
                r.getString(3),r.getString(4),r.getInt(5),r.getString(6)));}
        }return out;
    }
}