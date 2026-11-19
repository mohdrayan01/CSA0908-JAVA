import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.JOptionPane;
public class FeeWindow extends Frame {
    private final TextField regNo=new TextField(18), amount=new TextField(18);
    private final FeeService service; private String lastReceipt; private final Button pay;
    public FeeWindow(FeeService service) {
        super("Student Fee Payment"); this.service=service;
        setLayout(new BorderLayout(8,8));
        Panel fields=new Panel(new GridLayout(2,2,6,6));
        fields.add(new Label("Registration number")); fields.add(regNo);
        fields.add(new Label("Amount")); fields.add(amount);
        add(fields,BorderLayout.CENTER);
        Panel buttons=new Panel(new FlowLayout());
        pay=new Button("Pay"); Button clear=new Button("Clear"), receipt=new Button("Receipt");
        buttons.add(pay); buttons.add(clear); buttons.add(receipt); add(buttons,BorderLayout.SOUTH);
        pay.addActionListener(e -> payFee());
        clear.addActionListener(e -> {regNo.setText(""); amount.setText("");});
        receipt.addActionListener(e -> showReceipt());
        addWindowListener(new WindowAdapter(){@Override public void windowClosing(WindowEvent e){dispose();}});
        setSize(420,180);
    }
    private void payFee() {
        final double value;
        try { value=Double.parseDouble(amount.getText().trim()); }
        catch(NumberFormatException e){error("Enter a numeric amount.");return;}
        final String id=regNo.getText(); pay.setEnabled(false);
        new javax.swing.SwingWorker<String,Void>() {
            protected String doInBackground() throws Exception { return service.pay(id,value); }
            protected void done() {
                pay.setEnabled(true);
                try { lastReceipt=get(); JOptionPane.showMessageDialog(FeeWindow.this,lastReceipt,
                        "Payment complete",JOptionPane.INFORMATION_MESSAGE); }
                catch(Exception e) { error(e.getCause()==null?e.getMessage():e.getCause().getMessage()); }
            }
        }.execute();
    }
    private void showReceipt() {
        if(lastReceipt==null) error("No payment receipt is available.");
        else JOptionPane.showMessageDialog(this,lastReceipt,"Receipt",JOptionPane.INFORMATION_MESSAGE);
    }
    private void error(String m){JOptionPane.showMessageDialog(this,m,"Payment error",JOptionPane.ERROR_MESSAGE);}
    public static void main(String[] args) {
        String url=System.getenv().getOrDefault("JAVA_DB_URL","jdbc:mysql://localhost:3306/campus");
        String user=System.getenv().getOrDefault("JAVA_DB_USER","root");
        String pwd=System.getenv().getOrDefault("JAVA_DB_PASSWORD","");
        EventQueue.invokeLater(() -> new FeeWindow(new FeeService(url,user,pwd)).setVisible(true));
    }
}