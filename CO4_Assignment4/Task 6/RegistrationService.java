import java.sql.SQLException;
import java.util.List;
public class RegistrationService {
    private final RegistrationDao dao;
    public RegistrationService(RegistrationDao dao){this.dao=dao;}
    public List<CampusEvent> events()throws SQLException{return dao.events();}
    public List<Registration> page(Integer event,int limit,int offset)throws SQLException{return dao.page(event,limit,offset);}
    public void register(String id,String name,String email,int event)throws SQLException{
        if(id==null||!id.trim().matches("[A-Za-z0-9-]{4,20}"))throw new IllegalArgumentException("Enter a valid student ID.");
        if(name==null||name.trim().length()<2)throw new IllegalArgumentException("Enter the student's name.");
        if(email==null||!email.trim().matches("^[^@\s]+@[^@\s]+\.[^@\s]+$"))
            throw new IllegalArgumentException("Enter a valid email address.");
        if(event<=0)throw new IllegalArgumentException("Select an event.");
        try{dao.insert(id.trim(),name.trim(),email.trim(),event);}
        catch(SQLException e){if("23000".equals(e.getSQLState()))
            throw new IllegalArgumentException("This student is already registered for this event.");throw e;}
    }
}