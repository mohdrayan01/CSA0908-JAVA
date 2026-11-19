import java.io.*;import java.util.*;
public class Hostel implements Serializable{
 private static final long serialVersionUID=1L;
 private final List<Room> rooms=new ArrayList<>();
 public void addRoom(Room r){rooms.add(r);}
 public void save(File f)throws IOException{try(ObjectOutputStream o=new ObjectOutputStream(new FileOutputStream(f))){o.writeObject(this);}}
 public static Hostel load(File f)throws IOException,ClassNotFoundException{try(ObjectInputStream i=new ObjectInputStream(new FileInputStream(f))){return (Hostel)i.readObject();}}
}
class Room implements Serializable{
 private static final long serialVersionUID=1L;private final String roomNo;private final List<Resident> residents=new ArrayList<>();
 Room(String n){roomNo=n;}void add(Resident r){residents.add(r);}
}