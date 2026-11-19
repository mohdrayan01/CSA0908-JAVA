import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.*;
public class Converter extends JFrame {
    private final JTextField amount=new JTextField(10);
    private final JComboBox<String> target=new JComboBox<>(new String[]{"USD","EUR"});
    private final JLabel result=new JLabel(" ");
    private static double rate(String c){return c.equals("USD")?0.012:0.011;}
    public Converter(){
        super("Currency Converter");setLayout(new FlowLayout());
        JButton button=new JButton("Convert");
        add(new JLabel("INR"));add(amount);add(target);add(button);add(result);
        ActionListener action=e->convert();
        button.addActionListener(action);amount.addActionListener(action);
        setDefaultCloseOperation(EXIT_ON_CLOSE);pack();setLocationRelativeTo(null);
    }
    private void convert(){
        try{
            double value=Double.parseDouble(amount.getText().trim());
            String text=new DecimalFormat("0.00").format(value*rate((String)target.getSelectedItem()))
                    +" "+target.getSelectedItem();
            result.setText(text);JOptionPane.showMessageDialog(this,text);
        }catch(NumberFormatException e){
            result.setText("Invalid amount");
            JOptionPane.showMessageDialog(this,"Invalid amount","Input error",JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] a){SwingUtilities.invokeLater(()->new Converter().setVisible(true));}
}