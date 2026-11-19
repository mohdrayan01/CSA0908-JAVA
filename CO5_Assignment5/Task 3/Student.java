public class Student{
 private final int id;private final String registerNo,name,email;
 public Student(int id,String registerNo,String name,String email){this.id=id;this.registerNo=registerNo;this.name=name;this.email=email;}
 public int getId(){return id;}public String getRegisterNo(){return registerNo;}
 public String getName(){return name;}public String getEmail(){return email;}
}