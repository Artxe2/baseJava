package user.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class WebCrawling {
	public static void main(String[] args) {
		try {
			URL url = new URL("http://www.google.com");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String inLine = null;
			StringBuilder html = new StringBuilder();
			while ( (inLine = br.readLine()) != null) {
				html.append(inLine + "\n");
			}
			br.close();
			System.out.println(html);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}