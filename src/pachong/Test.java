package pachong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) throws IOException {
		//抓取到网站源码
		StringBuffer html = capture("http://www.songtaste.com/song/926141/");
		
		
		Pattern patt = Pattern
				.compile("flashplay\\(\"(.+?)\",\\s\"(.+?)\",\"(.+?)\"");
		Matcher matc = patt.matcher(html);
		if (matc.find()) {
			//获取到需要传的三个数据
			String strURL = matc.group(1);
			String sid = matc.group(2);
			String t = matc.group(3);
			String url = "http://www.songtaste.com/time.php?str=" + strURL
					+ "&sid=" + sid + "&t=" + t;
			//再抓取一遍，拿到下载链接
			StringBuffer html2 = capture(url);
			System.out.println(html2);
		} else {
			System.out.println("抓取失败！");
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
}
