package user.toy;

import java.util.stream.IntStream;

public class AdaptiveProCalc {

	private double wPro;//목표 평균 확률
	private double fPro;// 시작 확률
	private int maxLoof;//최대 시행 횟수
	private double proCoe = 1;//시행 횟수에 따른 확률 증가 계수
	private double exPro;/**/ //실제 평균 확률
	private double proTable[];/**///proTable[i] = i번째 확률
	private int dum;
	private double stack;
	private double residual;
	
	private static final AdaptiveProCalc INSTANCE = new AdaptiveProCalc();
	private AdaptiveProCalc() {
	}
	public static AdaptiveProCalc getInstance() {
		return INSTANCE;
	}
	private void gLoof() {
		proTable = new double [maxLoof+1];/*확률테이블 생성.
																	0~maxLoof까지의 시행 횟수 = for문의 i로 대체
																	proTable[i] = 시행 횟수에 따른 확률*/
		proTable[maxLoof] = 100;//최대 시행 횟수시 성공 확률 = 100%
		for (int i = 1 ;i < maxLoof ; i++) {
			if ((proTable[maxLoof - i + 1]/proCoe >= fPro)) {
				proTable[maxLoof - i] = proTable[maxLoof-i + 1]/proCoe;
			} else {
				proTable[maxLoof - i] = fPro;
			}
		}//시행 횟수 i에 따른 확률을 역순으로 계산
		dum = 1;
		stack = 0;
		residual = dum;
		int ptLength = proTable.length;
		for (int i = 1; i < ptLength; i++) {			
			stack = stack + residual * proTable[i] / 100 * i;
			residual = residual - residual * proTable[i] / 100;
		}
		exPro = 100 / (stack / dum); 
	}
	protected double[] exProCalc (double wpro) {
		this.wPro = wpro;
		fPro = wPro/2;
		maxLoof = (int)(200 / wPro);
		proCoe = 1;
		int i = 1;
		while(i <= 12) {
			
			gLoof();
			if (exPro>wPro) {
				proCoe=proCoe+1/Math.pow(10,i);
			}else if(exPro<wPro) {				
				proCoe=proCoe-1/Math.pow(10,i);
				i++;
				proCoe=proCoe+1/Math.pow(10,i);
			}else if(exPro==wPro) {
			}
			if(proCoe > 1.5) {
				maxLoof++;
				proCoe = 1;
				i = 1;
			}
		}
		proCoe = proCoe+1/Math.pow(10,i);
		i--;
		proCoe = proCoe-1/Math.pow(10,i);
		gLoof();
//		proPrint();
//		voPrint();
		return proTable;
	}
//	private void proPrint() {
//		for (int i=1 ; i<=maxLoof ; i++) {
//			System.out.printf("No.%3d : %3.3f%%\n",i,proTable[i]);
//		}
//	}
//	private void voPrint() {
//		System.out.println("exPro : "+exPro+", maxLoof : "+maxLoof);
//	}
}