package user.toy;

public class RandomVO{
		private static final RandomVO RVO = new RandomVO();
		private RandomVO(){}
		public static RandomVO getRvo () {
			return RVO;
		}
		protected rdBox_Origin[] ro = {
				new rdBox_Origin(0.5, 2.5, 12, 20, 65)
				,new rdBox_Origin(1, 5, 24, 30, 40)
				,new rdBox_Origin(1.5, 8.5, 20, 30, 40)
				,new rdBox_Origin(3.0, 12.0, 25, 60, 0)
				,new rdBox_Origin(15, 85, 0, 0, 0)
				,new rdBox_Origin(20, 20, 20, 20, 20)
				};
		protected class rdBox_Origin {
			public double  SS_pro;
			public double SS_table[];
			public int SS_count;
			public int SS_over;
			public double SS_now;
			public double S_pro;
			public double S_table[];
			public int S_count;
			public int S_over;
			public double S_now;
			public double A_pro;
			public double A_table[];
			public int A_count;
			public int A_over;
			public double A_now;
			public double B_pro;
			public double B_table[];
			public int B_count;
			public int B_over;
			public double B_now;
			public double C_pro;
			public double C_table[];
			public int C_count;
			public int C_over;
			public double C_now;
			public rdBox_Origin() {}
			private rdBox_Origin(double SS_pro, double S_pro, double A_pro, double B_pro, double C_pro) {
				this.SS_pro = SS_pro;
				this.S_pro = S_pro;
				this.A_pro = A_pro;
				this.B_pro = B_pro;
				this.C_pro = C_pro;
			}
		}
	}