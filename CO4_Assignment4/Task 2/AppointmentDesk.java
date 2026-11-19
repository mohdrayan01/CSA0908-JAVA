import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
public class AppointmentDesk extends JFrame {
    private final JTextField patientId=new JTextField();
    private final JComboBox<String> dept=new JComboBox<>(), doctor=new JComboBox<>();
    private final JComboBox<String> slot=new JComboBox<>(new String[]{"09:00","10:00","11:00","14:00"});
    private final JLabel status=new JLabel("Ready");
    private final Map<String,java.util.List<String>> doctors=new LinkedHashMap<>();
    private boolean bookingInProgress;
    public AppointmentDesk() {
        super("Outpatient Appointment Desk");
        doctors.put("Cardiology",Arrays.asList("Dr. Rao","Dr. Shah"));
        doctors.put("Dermatology",Arrays.asList("Dr. Iyer","Dr. Khan"));
        doctors.put("Pediatrics",Arrays.asList("Dr. Das","Dr. Mehta"));
        setLayout(new BorderLayout(8,8)); JPanel form=new JPanel(new GridLayout(5,2,6,6));
        form.add(new JLabel("Patient ID"));form.add(patientId);
        form.add(new JLabel("Department"));form.add(dept);
        form.add(new JLabel("Doctor"));form.add(doctor);
        form.add(new JLabel("Time slot"));form.add(slot);
        JButton book=new JButton("Book appointment");form.add(book);form.add(status);add(form);
        doctors.keySet().forEach(dept::addItem); refreshDoctors();
        dept.addActionListener(e -> refreshDoctors()); book.addActionListener(e -> book());
        addWindowListener(new WindowAdapter(){@Override public void windowClosing(WindowEvent e){
            if(!bookingInProgress || JOptionPane.showConfirmDialog(AppointmentDesk.this,
              "A booking is in progress. Close?","Confirm close",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) dispose();
        }});
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);setSize(480,260);setLocationRelativeTo(null);
    }
    private void refreshDoctors(){
        String d=(String)dept.getSelectedItem();doctor.removeAllItems();
        if(d!=null) doctors.get(d).forEach(doctor::addItem);
    }
    private void book(){
        String id=patientId.getText().trim();
        if(id.isEmpty()||dept.getSelectedItem()==null||doctor.getSelectedItem()==null||slot.getSelectedItem()==null){
            status.setText("Complete all fields.");return;
        }
        bookingInProgress=true;status.setText("Booking...");
        String details=id+" | "+dept.getSelectedItem()+" | "+doctor.getSelectedItem()+" | "+slot.getSelectedItem();
        new SwingWorker<String,Void>(){
            protected String doInBackground() throws Exception {Thread.sleep(300);return details;}
            protected void done(){bookingInProgress=false;try{
                JOptionPane.showMessageDialog(AppointmentDesk.this,"Confirmed: "+get());
                status.setText("Confirmed");
            }catch(Exception ex){status.setText("Booking failed");JOptionPane.showMessageDialog(AppointmentDesk.this,ex.getMessage());}}
        }.execute();
    }
    public static void main(String[] a){SwingUtilities.invokeLater(()->new AppointmentDesk().setVisible(true));}
}