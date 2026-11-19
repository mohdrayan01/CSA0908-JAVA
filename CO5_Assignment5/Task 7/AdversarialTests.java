import java.nio.file.*;import java.sql.*;
public class AdversarialTests{
 public static void main(String[] a)throws Exception{
  System.out.println("Unsafe login payload to test locally: ' OR '1'='1' -- ");
  System.out.println("Use same payload in id or password; compare unsafe() with safe().");
  Path f=Files.createTempFile("bad-passes",".csv");
  Files.writeString(f,"P1,R1,Name,2,10.0\nP2,BROKEN\nP3,R3,Name,2,not-a-number\n\n");
  System.out.println("Damaged test file: "+f);
 }
}