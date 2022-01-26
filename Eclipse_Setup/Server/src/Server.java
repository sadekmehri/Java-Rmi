import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClassLoader;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("deprecation")
public class Server extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static JPanel contentPane;
	private static JButton connectBtn;
		
    public static void main(String[] args) {
    	EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	Server frame = new Server();
                    frame.setVisible(true);    	
                } 
                catch (Exception e) {
                	e.printStackTrace();
                	JOptionPane.showMessageDialog(null, e.toString(), "Failure", JOptionPane.ERROR_MESSAGE, null);
                }
            }
        }); 
    } 
    
    
    /**
     * Create the frame.
     */
    public Server() {
    	
        setTitle("Welcome Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(this.getClass().getResource("/myAppIcon.png")).getImage());
        setSize(300, 150);
        setResizable(false);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
     
        // New Matrix Button
        connectBtn = new JButton("Activate Server");
        connectBtn.setBounds(55, 35, 175, 35);
        connectBtn.setIcon(new ImageIcon(this.getClass().getResource("/activateIcon.png")));
        connectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });
        contentPane.add(connectBtn);
        
    }
    
    // Server is live
	@SuppressWarnings("rawtypes")
	private void connect() {	
    	System.setSecurityManager(new RMISecurityManager());
    	try {
    		Properties p = System.getProperties();
    		String url = p.getProperty("java.rmi.server.codebase");	
			Class ServerClass = RMIClassLoader.loadClass(url,"FabriqueImp");
			Registry rgsty = LocateRegistry.createRegistry(1099);
			rgsty.rebind("MyFabrique", (Remote) ServerClass.newInstance());
        	ImageIcon successIcon = new ImageIcon(this.getClass().getResource("/successIcon.png"));
            JOptionPane.showMessageDialog(this, "Server is live", "Success", JOptionPane.PLAIN_MESSAGE, successIcon);
            connectBtn.setEnabled(false);
        } 
    	catch (Exception e) {
    		e.printStackTrace();
        	ImageIcon warningIcon = new ImageIcon(this.getClass().getResource("/warningIcon.png"));
        	JOptionPane.showMessageDialog(this, "Something wrong happened! Please try again!", "Failure", JOptionPane.ERROR_MESSAGE, warningIcon);
        } 
    }

}