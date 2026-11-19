import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class EventRegistrationApp extends JFrame {
    private static final int PAGE_SIZE=50;
    private final RegistrationService service;
    private final JTextField sid=new JTextField(),name=new JTextField(),email=new JTextField();
    private final JComboBox<CampusEvent> event=new JComboBox<>(),filter=new JComboBox<>();
    private final DefaultTableModel model=new DefaultTableModel(
        new Object[]{"ID","Student ID","Name","Email","Event"},0){
        public boolean isCellEditable(int r,int c){return false;}};
    private int page=0;
    public EventRegistrationApp(RegistrationService service){
        super("Campus Event Registration");this.service=service;setJMenuBar(menu());
        JTabbedPane tabs=new JTabbedPane();tabs.addTab("Register",registerPanel());tabs.addTab("Registrations",listPanel());
        add(tabs);setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(850,520);setLocationRelativeTo(null);loadEvents();
    }
    private JMenuBar menu(){
        JMenuBar b=new JMenuBar();JMenu f=new JMenu("File"),h=new JMenu("Help");
        JMenuItem exit=new JMenuItem("Exit"),about=new JMenuItem("About");
        exit.addActionListener(e->dispose());about.addActionListener(e->JOptionPane.showMessageDialog(this,"Campus Event Registration"));
        f.add(exit);h.add(about);b.add(f);b.add(h);return b;
    }
    private JPanel registerPanel(){
        JPanel p=new JPanel(new GridLayout(5,2,8,8));
        p.add(new JLabel("Student ID"));p.add(sid);p.add(new JLabel("Name"));p.add(name);
        p.add(new JLabel("Email"));p.add(email);p.add(new JLabel("Event"));p.add(event);
        JButton submit=new JButton("Register");p.add(submit);submit.addActionListener(e->registerAsync());return p;
    }
    private JPanel listPanel(){
        JPanel p=new JPanel(new BorderLayout());JPanel top=new JPanel();top.add(new JLabel("Event filter"));top.add(filter);
        JButton apply=new JButton("Apply");top.add(apply);p.add(top,BorderLayout.NORTH);p.add(new JScrollPane(new JTable(model)));
        JPanel bottom=new JPanel();JButton prev=new JButton("Previous"),next=new JButton("Next");
        bottom.add(prev);bottom.add(new JLabel("Page numbers start at 1"));bottom.add(next);p.add(bottom,BorderLayout.SOUTH);
        apply.addActionListener(e->{page=0;loadPage();});filter.addActionListener(e->{page=0;loadPage();});
        prev.addActionListener(e->{if(page>0){page--;loadPage();}});next.addActionListener(e->{page++;loadPage();});return p;
    }
    private Integer filterId(){CampusEvent x=(CampusEvent)filter.getSelectedItem();return x==null?null:x.getId();}
    private void loadEvents(){new SwingWorker<List<CampusEvent>,Void>(){
        protected List<CampusEvent> doInBackground()throws Exception{return service.events();}
        protected void done(){try{for(CampusEvent x:get()){event.addItem(x);filter.addItem(x);}loadPage();}
            catch(Exception x){error(root(x));}}
    }.execute();}
    private void registerAsync(){
        CampusEvent e=(CampusEvent)event.getSelectedItem();if(e==null){error("No event is available.");return;}
        String a=sid.getText(),b=name.getText(),c=email.getText();int eventId=e.getId();
        new SwingWorker<Void,Void>(){
            protected Void doInBackground()throws Exception{service.register(a,b,c,eventId);return null;}
            protected void done(){try{get();JOptionPane.showMessageDialog(EventRegistrationApp.this,"Registration saved.");
                loadPage();}catch(Exception x){error(root(x));}}
        }.execute();
    }
    private void loadPage(){
        Integer id=filterId();int offset=page*PAGE_SIZE;
        new SwingWorker<List<Registration>,Void>(){
            protected List<Registration> doInBackground()throws Exception{return service.page(id,PAGE_SIZE,offset);}
            protected void done(){try{model.setRowCount(0);for(Registration r:get())model.addRow(new Object[]{
                r.getId(),r.getStudentId(),r.getName(),r.getEmail(),r.getEventName()});}
                catch(Exception x){error(root(x));}}
        }.execute();
    }
    private String root(Exception e){return e.getCause()!=null&&e.getCause().getMessage()!=null?e.getCause().getMessage():e.getMessage();}
    private void error(String s){JOptionPane.showMessageDialog(this,s,"Error",JOptionPane.ERROR_MESSAGE);}
    public static void main(String[] a){
        String url=System.getenv().getOrDefault("JAVA_DB_URL","jdbc:mysql://localhost:3306/campus_events");
        String user=System.getenv().getOrDefault("JAVA_DB_USER","root"),pwd=System.getenv().getOrDefault("JAVA_DB_PASSWORD","");
        RegistrationService s=new RegistrationService(new RegistrationDao(url,user,pwd));
        SwingUtilities.invokeLater(()->new EventRegistrationApp(s).setVisible(true));
    }
}