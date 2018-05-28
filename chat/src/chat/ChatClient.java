package chat;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame{
	
	public JTextField textfiled = new JTextField();
	public JTextArea textarea = new JTextArea();
	Socket s = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	boolean bConnection = false;
	String name;
	
	
	public ChatClient(Start st) {
		this.name = st.name;
		this.setBounds(400,300,1200,600);		
		this.getContentPane().add(textarea,"Center");
		this.getContentPane().add(textfiled,"South");
		textfiled.addActionListener(new TFlistener());
		Font font1 = new Font("宋体", Font.PLAIN, 30);
		textfiled.setFont(font1);
		Font font2 = new Font("宋体", Font.PLAIN, 30);
		textarea.setFont(font2);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				disconnection();
				System.exit(0);
			}
			
		});
		this.setVisible(true);
		
		connection();
		new Thread(new RecvThread()).start();
	}
	
		
	public void connection() {
		try {
			s = new Socket("192.168.0.105", 8888);
			System.out.println("连接成功");
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			
			bConnection = true;
			dos.writeUTF(name + "已上线。。。");
			dos.flush();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void disconnection() {
		try {			
			dos.close();
			s.close();	
			dis.close();
		}  catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private class TFlistener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String str = textfiled.getText().trim();
			
			textfiled.setText("");
			
			try {
				
				dos.writeUTF(name+" : "+str);
				dos.flush();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}
	
	private class RecvThread implements Runnable {

		@Override
		public void run() {
			
			try {
				while(bConnection) {
					String str = dis.readUTF();
					textarea.setText(textarea.getText() + str +"\n");
				}
			}catch (SocketException e) {
				
			} catch (EOFException e) {
				try {
					dos.writeUTF(name+" : 退出了聊天");
					dos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			
		}
		
		
	}
}
