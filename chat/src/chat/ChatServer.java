package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class ChatServer{
	ServerSocket ss = null;
	boolean started;
	List<Client> clients = new ArrayList<>();
	
	public static void main(String[] args) {
		
		new ChatServer().start();
		
	}
	public void start() {
		try {
			
			ss = new ServerSocket(8888);
			started = true;
		} catch (BindException e) {
			
			System.out.println("端口使用中");
			System.out.println("请关掉相应程序重新启动");
			System.exit(0);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		try {
			while(started) {
				
				Socket s = ss.accept();
				Client c = new Client(s);
				System.out.println("一个连接");
				new Thread(c).start();
				clients.add(c);
				
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally {
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private class Client implements Runnable {
		private Socket s = null;
		private DataOutputStream dos = null;
		private DataInputStream dis = null;
		private boolean bConnection = false;
		
		public Client(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnection = true;
			} catch (IOException e) {				
				e.printStackTrace();
			}
			
		}
		
		public void send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				clients.remove(this);
				try {
					dos.writeUTF(str);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		public void run() {
			Client c = null;
			try {
				while(bConnection) {
					String str;				
					str = dis.readUTF();
					System.out.println(str);
					for(int i=0;i<clients.size();i++) {
						c = clients.get(i);
						c.send(str);
					}
				}
			}catch (EOFException e) {
				String str;				
				try {
					str = dis.readUTF();
					System.out.println(str);
					for(int i=0;i<clients.size();i++) {
						c = clients.get(i);
						c.send(str);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {				
				try {
					if(dis != null) dis.close();
					if(dos != null) dos.close();
					if(s != null) {
						s.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
				
		}
		
	}

	
	
}

