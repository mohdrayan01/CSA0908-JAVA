public class Registration {
    private final int id,eventId; private final String studentId,name,email,eventName;
    public Registration(int id,String studentId,String name,String email,int eventId,String eventName){
        this.id=id;this.studentId=studentId;this.name=name;this.email=email;this.eventId=eventId;this.eventName=eventName;}
    public int getId(){return id;} public int getEventId(){return eventId;}
    public String getStudentId(){return studentId;} public String getName(){return name;}
    public String getEmail(){return email;} public String getEventName(){return eventName;}
}