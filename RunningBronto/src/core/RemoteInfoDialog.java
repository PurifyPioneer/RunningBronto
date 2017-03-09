package core;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Dialog that prompts the user to input host and port
 * information if the lighthouse view should be displayed on
 * a remote screen/server.
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class RemoteInfoDialog extends JDialog{

	private JPanel content;
	
	private JLabel lbl_Host;
	private JTextField fld_Host;
	
	private JLabel lbl_Port;
	private JTextField fld_Port;
	
	private JButton btn_confirm;
	
	private String host;
	private int port;
	
	public RemoteInfoDialog() {
		this.setModal(true);
		this.setTitle("Enter remote info");
		
		content = new JPanel();
		content.setLayout(new GridLayout(3, 2));
		content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		lbl_Host = new JLabel("Host:");
		
		fld_Host = new JTextField();
		fld_Host.setPreferredSize(new Dimension(100, 25));

		lbl_Port = new JLabel("Port:");
		
		fld_Port = new JTextField();
		fld_Host.setPreferredSize(new Dimension(100, 25));
		
		btn_confirm = new JButton("Confirm");
		btn_confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				host = fld_Host.getText();
				port = Integer.parseInt(fld_Port.getText());
				setVisible(false);
			}
		});
		
		content.add(lbl_Host);
		content.add(lbl_Port);
		content.add(fld_Host);
		content.add(fld_Port);
		content.add(new JLabel());
		content.add(btn_confirm);
		
		this.add(content);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public String getHost() {
		return this.host;
	}
	
	public int getPort() {
		return this.port;
	}
}
