package pachong;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

public class JCalculator extends JFrame implements ActionListener {

	private static final long serialVersionUID = -169068472193786457L;

	private class WindowCloser extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}

	JButton reset = new JButton("走起");
	JTextField display = new JTextField("");
	JTextField result = new JTextField("");

	public JCalculator() {
		super("版权：王丹鹏");

		JPanel panel2 = new JPanel(new GridLayout(3, 1));
		panel2.add(display);
		panel2.add(reset);
		panel2.add(result);
		getContentPane().add("Center", panel2);

		reset.addActionListener(this);
		display.addActionListener(this);
		result.addActionListener(this);
		addWindowListener(new WindowCloser());
		setSize(800, 200);
		setLocation(600, 400);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println(display.getText());
		try {
			// 抓取到网站源码
			StringBuffer html = capture(display.getText());
			Pattern patt = Pattern
					.compile("flashplay\\(\"(.+?)\",\\s\"(.+?)\",\"(.+?)\"");
			Matcher matc = patt.matcher(html);
			if (matc.find()) {
				// 获取到需要传的三个数据
				String strURL = matc.group(1);
				String sid = matc.group(2);
				String t = matc.group(3);
				String url = "http://www.songtaste.com/time.php?str=" + strURL
						+ "&sid=" + sid + "&t=" + t;
				// 再抓取一遍，拿到下载链接
				StringBuffer html2 = capture(url);
				result.setText(html2.toString());
			} else {
				System.out.println("抓取失败！");
				result.setText("抓取失败！");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			result.setText("抓取失败！");
		}
	}

	public static StringBuffer capture(String url) throws IOException {
		URL urlmy = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlmy.openConnection();
		con.setFollowRedirects(true);
		con.setInstanceFollowRedirects(false);
		con.connect();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				con.getInputStream(), "gb2312"));
		String s = "";
		StringBuffer sb = new StringBuffer("");
		while ((s = br.readLine()) != null) {
			sb.append(s + "\r\n");
		}
		return sb;
	}

	public static void main(String[] args) {
		new JCalculator();
	}
}