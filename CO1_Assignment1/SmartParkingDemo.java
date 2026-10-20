import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

interface Chargeable { BigDecimal chargingFee(BigDecimal kWh); }
interface ParkingPass { boolean isValidFor(Vehicle vehicle, LocalDate date); }

final class MonthlyPass implements ParkingPass {
    private final String registration;
    private final LocalDate validThrough;
    MonthlyPass(String registration, LocalDate validThrough) {
        this.registration = registration.trim().toUpperCase(Locale.ROOT);
        this.validThrough = Objects.requireNonNull(validThrough);
    }
    public boolean isValidFor(Vehicle v, LocalDate date) {
        return v.getRegNo().equals(registration) && !date.isAfter(validThrough);
    }
}

final class OneDayPass implements ParkingPass {
    private final String registration;
    private final LocalDate validDate;
    OneDayPass(String registration, LocalDate validDate) {
        this.registration = registration.trim().toUpperCase(Locale.ROOT);
        this.validDate = Objects.requireNonNull(validDate);
    }
    public boolean isValidFor(Vehicle v, LocalDate date) {
        return v.getRegNo().equals(registration) && validDate.equals(date);
    }
}

abstract class Vehicle {
    private final String regNo;
    Vehicle(String regNo) {
        if (regNo == null || regNo.isBlank()) throw new IllegalArgumentException("Registration required.");
        this.regNo = regNo.trim().toUpperCase(Locale.ROOT);
    }
    public final String getRegNo() { return regNo; }
    abstract BigDecimal hourlyRate();
    BigDecimal calculateFee(LocalDateTime in, LocalDateTime out) {
        long mins = Duration.between(in, out).toMinutes();
        if (mins < 0) throw new IllegalArgumentException("Exit precedes entry.");
        long hours = mins == 0 ? 0 : (mins + 59) / 60;
        return hourlyRate().multiply(BigDecimal.valueOf(hours));
    }
}
final class TwoWheeler extends Vehicle {
    TwoWheeler(String r) { super(r); }
    BigDecimal hourlyRate() { return new BigDecimal("10.00"); }
}
class Car extends Vehicle {
    Car(String r) { super(r); }
    BigDecimal hourlyRate() { return new BigDecimal("30.00"); }
}
final class ElectricCar extends Car implements Chargeable {
    ElectricCar(String r) { super(r); }
    public BigDecimal chargingFee(BigDecimal kWh) {
        if (kWh.signum() < 0) throw new IllegalArgumentException("Negative kWh.");
        return kWh.multiply(new BigDecimal("12.00"));
    }
}
final class EBike extends Vehicle {
    private static final BigDecimal FLAT_FEE = new BigDecimal("25.00");
    EBike(String r) { super(r); }
    BigDecimal hourlyRate() { return BigDecimal.ZERO; }
    BigDecimal calculateFee(LocalDateTime in, LocalDateTime out) {
        if (out.isBefore(in)) throw new IllegalArgumentException("Exit precedes entry.");
        return in.equals(out) ? BigDecimal.ZERO : FLAT_FEE;
    }
}
final class DeliveryVan extends Vehicle {
    DeliveryVan(String r) { super(r); }
    BigDecimal hourlyRate() { return new BigDecimal("30.00"); }
    BigDecimal calculateFee(LocalDateTime in, LocalDateTime out) {
        long mins = Duration.between(in, out).toMinutes();
        if (mins < 0) throw new IllegalArgumentException("Exit precedes entry.");
        long hours = mins == 0 ? 0 : (mins + 59) / 60;
        BigDecimal total = BigDecimal.ZERO;
        LocalDateTime hour = in;
        for (long i = 0; i < hours; i++, hour = hour.plusHours(1)) {
            LocalTime time = hour.toLocalTime();
            boolean peak = !time.isBefore(LocalTime.of(9, 0))
                    && time.isBefore(LocalTime.of(17, 0));
            total = total.add(peak ? new BigDecimal("60.00") : new BigDecimal("30.00"));
        }
        return total;
    }
}
final class ParkingTicket {
    private final long number;
    private final Vehicle vehicle;
    private final int floor;
    private final int slot;
    private final LocalDateTime entry;
    ParkingTicket(long n, Vehicle v, int f, int s, LocalDateTime e) {
        number=n; vehicle=v; floor=f; slot=s; entry=e;
    }
    long getNumber(){return number;}
    Vehicle getVehicle(){return vehicle;}
    int getFloor(){return floor;}
    int getSlot(){return slot;}
    LocalDateTime getEntry(){return entry;}
}
final class ParkingFacility {
    private final Vehicle[][] slots;
    ParkingFacility(int[] capacities) {
        slots = new Vehicle[capacities.length][];
        for(int f=0; f<capacities.length; f++) {
            if(capacities[f]<0) throw new IllegalArgumentException("Negative capacity.");
            slots[f]=new Vehicle[capacities[f]];
        }
    }
    int[] allocate(Vehicle v) {
        for(int f=0;f<slots.length;f++)
            for(int s=0;s<slots[f].length;s++)
                if(slots[f][s]==null) { slots[f][s]=v; return new int[]{f,s}; }
        return null;
    }
    boolean release(int f,int s) {
        if(f<0||f>=slots.length||s<0||s>=slots[f].length||slots[f][s]==null)return false;
        slots[f][s]=null; return true;
    }
    int floorCount(){return slots.length;}
}
final class ParkingService {
    private final ParkingFacility facility;
    private final Map<String,ParkingTicket> active=new HashMap<>();
    private long next=1;
    ParkingService(ParkingFacility f){facility=f;}
    ParkingTicket enter(Vehicle v,LocalDateTime entry) {
        if(active.containsKey(v.getRegNo())) throw new IllegalArgumentException("Duplicate active registration.");
        int[] pos=facility.allocate(v);
        if(pos==null)return null;
        ParkingTicket t=new ParkingTicket(next++,v,pos[0],pos[1],entry);
        active.put(v.getRegNo(),t); return t;
    }
    BigDecimal exit(String registration,LocalDateTime out,ParkingPass pass,BigDecimal kWh) {
        String key=registration.trim().toUpperCase(Locale.ROOT);
        ParkingTicket t=active.get(key);
        if(t==null)throw new IllegalArgumentException("No active ticket.");
        Vehicle v=t.getVehicle();
        if(out.isBefore(t.getEntry()))throw new IllegalArgumentException("Exit precedes entry.");
        BigDecimal parking=(pass!=null&&pass.isValidFor(v,t.getEntry().toLocalDate()))
                ?BigDecimal.ZERO:v.calculateFee(t.getEntry(),out);
        BigDecimal charging=(v instanceof Chargeable)
                ?((Chargeable)v).chargingFee(kWh):BigDecimal.ZERO;
        facility.release(t.getFloor(),t.getSlot()); active.remove(key);
        return parking.add(charging).setScale(2,RoundingMode.HALF_UP);
    }
}
public class SmartParkingDemo {
    public static void main(String[] args) {
        ParkingFacility f=new ParkingFacility(new int[]{2,1,2,1,2});
        ParkingService p=new ParkingService(f);
        LocalDate d=LocalDate.of(2026,10,20);
        ParkingTicket bike=p.enter(new TwoWheeler("TN01AA0001"),d.atTime(8,0));
        ParkingTicket car=p.enter(new Car("TN02BB0002"),d.atTime(10,0));
        ParkingTicket ev=p.enter(new ElectricCar("TN03CC0003"),d.atTime(11,0));
        ParkingTicket eb=p.enter(new EBike("TN04DD0004"),d.atTime(12,0));
        ParkingTicket peak=p.enter(new DeliveryVan("TN05EE0005"),d.atTime(10,0));
        ParkingTicket off=p.enter(new DeliveryVan("TN06FF0006"),d.atTime(18,0));
        ParkingTicket visitor=p.enter(new Car("TN07GG0007"),d.atTime(13,0));
        System.out.println("Floors: "+f.floorCount());
        System.out.println("TwoWheeler, 61 min: Rs "+p.exit(bike.getVehicle().getRegNo(),d.atTime(9,1),null,BigDecimal.ZERO));
        System.out.println("Car, 130 min: Rs "+p.exit(car.getVehicle().getRegNo(),d.atTime(12,10),null,BigDecimal.ZERO));
        System.out.println("ElectricCar, 60 min + 10 kWh: Rs "+p.exit(ev.getVehicle().getRegNo(),d.atTime(12,0),null,new BigDecimal("10")));
        System.out.println("EBike flat fee: Rs "+p.exit(eb.getVehicle().getRegNo(),d.atTime(12,30),null,BigDecimal.ZERO));
        System.out.println("DeliveryVan during 9-5, 60 min: Rs "+p.exit(peak.getVehicle().getRegNo(),d.atTime(11,0),null,BigDecimal.ZERO));
        System.out.println("DeliveryVan outside 9-5, 60 min: Rs "+p.exit(off.getVehicle().getRegNo(),d.atTime(19,0),null,BigDecimal.ZERO));
        OneDayPass dayPass=new OneDayPass("TN07GG0007",d);
        System.out.println("Visitor with valid one-day pass: Rs "+p.exit(visitor.getVehicle().getRegNo(),d.atTime(14,0),dayPass,BigDecimal.ZERO));
    }
}
