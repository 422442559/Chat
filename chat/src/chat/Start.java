package chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Start extends JFrame implements ActionListener {
	private JTextField name1 = new JTextField(10);
	private JLabel jl = new JLabel("请输入你的姓名：");
	private JButton ok = new JButton("确定");
	public String name;
	
	public Start() {
		this.setBounds(800, 600, 400, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().add(jl,"West");
		this.getContentPane().add(name1,"Center");
		this.getContentPane().add(ok,"East");
		ok.addActionListener(this);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		name = name1.getText().trim();
		new ChatClient(this);
		this.setVisible(false);
	}
}
