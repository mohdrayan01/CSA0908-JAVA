import java.sql.*;import java.util.*;
public class StudentDao implements Dao<Student,Integer>{
 private final Connection con;public StudentDao(Connection c){con=c;}
 public int insert(Student s)throws SQLException{
  String q="INSERT INTO student(register_no,name,email) VALUES(?,?,?)";
  try(PreparedStatement p=con.prepareStatement(q,Statement.RETURN_GENERATED_KEYS)){
   p.setString(1,s.getRegisterNo());p.setString(2,s.getName());p.setString(3,s.getEmail());p.executeUpdate();
   try(ResultSet r=p.getGeneratedKeys()){return r.next()?r.getInt(1):-1;}
  }
 }
 public Optional<Student> findById(Integer id)throws SQLException{
  try(PreparedStatement p=con.prepareStatement("SELECT id,register_no,name,email FROM student WHERE id=?")){
   p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}
  }
 }
 public List<Student> findAll()throws SQLException{
  List<Student> a=new ArrayList<>();try(PreparedStatement p=con.prepareStatement("SELECT id,register_no,name,email FROM student");ResultSet r=p.executeQuery()){while(r.next())a.add(map(r));}return a;
 }
 public boolean update(Student s)throws SQLException{
  try(PreparedStatement p=con.prepareStatement("UPDATE student SET register_no=?,name=?,email=? WHERE id=?")){
   p.setString(1,s.getRegisterNo());p.setString(2,s.getName());p.setString(3,s.getEmail());p.setInt(4,s.getId());return p.executeUpdate()==1;
  }
 }
 public boolean delete(Integer id)throws SQLException{
  try(PreparedStatement p=con.prepareStatement("DELETE FROM student WHERE id=?")){p.setInt(1,id);return p.executeUpdate()==1;}
 }
 private Student map(ResultSet r)throws SQLException{return new Student(r.getInt("id"),r.getString("register_no"),r.getString("name"),r.getString("email"));}
}