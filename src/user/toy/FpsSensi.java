package user.toy;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FpsSensi {



	public static void main(String[] args) throws Exception {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
		} catch (Exception e) {
			// TODO: handle exception
		}
		double ratio = 0;
		System.out.print("Input LOL sensi(0 ~ 100): ");
		int lol = Integer.parseInt(br.readLine() );
		if (lol == 0) ratio = 0.0625;
		else if (lol == 5) ratio = 0.09375;
		else if (lol == 10) ratio = 0.125;
		else if (lol == 15) ratio = 0.1875;
		else if (lol == 20) ratio = 0.25;
		else if (lol == 25) ratio = 0.0375;
		else if (lol == 30) ratio = 0.5;
		else if (lol == 35) ratio = 0.625;
		else if (lol == 40) ratio = 0.75;
		else if (lol == 45) ratio = 0.875;
		else if (lol == 50) ratio = 1;
		else if (lol == 55) ratio = 1.25;
		else if (lol == 60) ratio = 1.5;
		else if (lol == 65) ratio = 1.75;
		else if (lol == 70) ratio = 2;
		else if (lol == 75) ratio = 2.25;
		else if (lol == 80) ratio = 2.5;
		else if (lol == 85) ratio = 2.75;
		else if (lol == 90) ratio = 3;
		else if (lol == 95) ratio = 3.25;
		else if (lol == 100) ratio = 3.5;
		int mon_x = 1920;
		int cpi = 1300;
		int fov = 103;
		double dpd = 600000 / 11 / 10.6 / 360.0;
		double sensi = dpd * ratio / (1 / Math.asin(Math.sin(fov / Math.PI / 360) / mon_x * Math.PI * 180) / 2);
		System.out.printf("\nedpi: %5.1f\n", sensi * cpi * ratio);
		System.out.printf("sensi: %2.3f\n\n", sensi);
		System.out.printf("x1.15: %2.3f\n", 1 / 1.15);
		System.out.printf("x1.25: %2.3f\n", 1 / 1.25);
		System.out.printf("x1.5: %2.3f\n", 1 / 1.5);
		System.out.printf("x2.5: %2.3f\n", 1 / 2.5);
		System.out.printf("x5.0: %2.3f\n", 1 / 5.0);
	}
}