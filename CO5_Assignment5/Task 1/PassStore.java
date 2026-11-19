import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
public class PassStore {
 private final Path file;
 PassStore(Path file){this.file=file;}
 public void add(String id,String reg,String name,int route,double amount)throws IOException{
  if(id==null||reg==null||name==null||id.contains(",")||reg.contains(",")||name.contains(",")||amount<0)
   throw new IllegalArgumentException("Invalid or delimiter-containing field");
  try(BufferedWriter w=Files.newBufferedWriter(file,StandardOpenOption.CREATE,StandardOpenOption.APPEND)){
   w.write(String.join(",",id,reg,name,String.valueOf(route),String.valueOf(amount)));w.newLine();
  }
 }
 public String[] find(String passId)throws IOException{
  try(BufferedReader r=Files.newBufferedReader(file)){
   String line;
   while((line=r.readLine())!=null){
    String[] f=line.split(",",-1);
    if(f.length!=5)continue;
    try{Integer.parseInt(f[3]);Double.parseDouble(f[4]);}catch(NumberFormatException e){continue;}
    if(f[0].equals(passId))return f;
   }
  }
  return null;
 }
 public static void main(String[] args)throws IOException{
  Path f=Files.createTempFile("passes",".csv");PassStore s=new PassStore(f);
  s.add("P1001","192311001","Arun",12,4500);
  Files.writeString(f,"P1002,DAMAGED LINE
",StandardOpenOption.APPEND);
  s.add("P1003","192311003","Divya",7,3800);
  System.out.println(Arrays.toString(s.find("P1003")));
  System.out.println(Arrays.toString(s.find("P1002")));Files.delete(f);
 }
}