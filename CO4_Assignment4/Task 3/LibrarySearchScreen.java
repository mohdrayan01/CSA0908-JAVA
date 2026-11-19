import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class LibrarySearchScreen extends JFrame {
    private final JTextField query=new JTextField(24);
    private final JComboBox<String> category=new JComboBox<>(new String[]{"All","Title","Author","ISBN"});
    private final JLabel status=new JLabel("Ready");
    private final DefaultTableModel model=new DefaultTableModel(new Object[]{"Title","Author","Category","Available"},0);
    public LibrarySearchScreen(){
        super("Campus Library Search");setLayout(new BorderLayout(8,8));
        JPanel search=new JPanel(new FlowLayout(FlowLayout.LEFT));JButton find=new JButton("Search");
        search.add(new JLabel("Search"));search.add(query);search.add(category);search.add(find);
        JPanel top=new JPanel(new BorderLayout());top.add(new JLabel("Library Search",JLabel.CENTER),BorderLayout.NORTH);
        top.add(search,BorderLayout.SOUTH);add(top,BorderLayout.NORTH);
        add(new JScrollPane(new JTable(model)),BorderLayout.CENTER);
        JPanel buttons=new JPanel(new FlowLayout());JButton prev=new JButton("Prev"),next=new JButton("Next"),reserve=new JButton("Reserve");
        buttons.add(prev);buttons.add(next);buttons.add(reserve);
        JPanel bottom=new JPanel(new GridLayout(2,1));bottom.add(buttons);bottom.add(status);add(bottom,BorderLayout.SOUTH);
        find.addActionListener(e->{model.setRowCount(0);model.addRow(new Object[]{"Example Java","A. Writer",category.getSelectedItem(),"Yes"});status.setText("1 result");});
        prev.addActionListener(e->status.setText("Previous results"));next.addActionListener(e->status.setText("Next results"));
        reserve.addActionListener(e->status.setText("Select a result to reserve"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(760,480);setLocationRelativeTo(null);
    }
    public static void main(String[] a){SwingUtilities.invokeLater(()->new LibrarySearchScreen().setVisible(true));}
}