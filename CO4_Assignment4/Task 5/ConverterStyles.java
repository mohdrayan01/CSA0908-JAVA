import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

abstract class BaseConverter extends JFrame {
    protected final JTextField amount=new JTextField(8);
    protected final JComboBox<String> from=new JComboBox<>(new String[]{"INR","USD","EUR"});
    protected final JComboBox<String> to=new JComboBox<>(new String[]{"USD","EUR","INR"});
    protected final JLabel result=new JLabel(" ");
    protected final JButton convert=new JButton("Convert");
    BaseConverter(String title){super(title);setLayout(new FlowLayout());add(amount);add(from);add(to);add(convert);add(result);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);setSize(360,130);}
    protected void runConversion(){try{
        double a=Double.parseDouble(amount.getText().trim());
        double x=a*rate((String)from.getSelectedItem())/rate((String)to.getSelectedItem());
        String s=String.format("%.2f %s",x,to.getSelectedItem());result.setText(s);
        JOptionPane.showMessageDialog(this,s);
    }catch(NumberFormatException e){result.setText("Invalid amount");JOptionPane.showMessageDialog(this,"Invalid amount");}}
    private double rate(String c){switch(c){case "USD":return 83;case "EUR":return 90;default:return 1;}}
    abstract void bind();
}
class FrameListener extends BaseConverter implements ActionListener {
    FrameListener(){super("Frame implements listener");bind();}
    void bind(){convert.addActionListener(this);amount.addActionListener(this);}
    public void actionPerformed(ActionEvent e){runConversion();}
}
class NamedInner extends BaseConverter {
    NamedInner(){super("Named inner class");bind();}
    void bind(){Handler h=new Handler();convert.addActionListener(h);amount.addActionListener(h);}
    private class Handler implements ActionListener {public void actionPerformed(ActionEvent e){runConversion();}}
}
class AnonymousHandler extends BaseConverter {
    AnonymousHandler(){super("Anonymous inner class");bind();}
    void bind(){ActionListener h=new ActionListener(){public void actionPerformed(ActionEvent e){runConversion();}};
        convert.addActionListener(h);amount.addActionListener(h);}
}
class LambdaHandler extends BaseConverter {
    LambdaHandler(){super("Lambda");bind();}
    void bind(){ActionListener h=e->runConversion();convert.addActionListener(h);amount.addActionListener(h);}
}
public class ConverterStyles {
    public static void main(String[] a){SwingUtilities.invokeLater(()->{
        new FrameListener().setVisible(true);new NamedInner().setVisible(true);
        new AnonymousHandler().setVisible(true);new LambdaHandler().setVisible(true);
    });}
}