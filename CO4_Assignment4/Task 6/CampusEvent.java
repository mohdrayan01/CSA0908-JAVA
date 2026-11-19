public class CampusEvent {
    private final int id;private final String name;
    public CampusEvent(int id,String name){this.id=id;this.name=name;}
    public int getId(){return id;}public String getName(){return name;}
    public String toString(){return name;}
}