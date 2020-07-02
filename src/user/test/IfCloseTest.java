package user.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import oracle.jdbc.internal.OracleCallableStatement;

public class IfCloseTest implements PerformTest {

	private Connection con = null;
	private PreparedStatement pstmt = null;
	private CallableStatement cstmt = null;
	private OracleCallableStatement ocstmt = null;
	private ResultSet rs = null;

	public IfCloseTest() {
		start(10, Integer.MAX_VALUE-1);
	}
	@Override
	public void test1() {
		for (int i = 0; i < Integer.MAX_VALUE; i++)
		freeConnection(null, null, null, null, null);
	}
	@Override
	public void test2() {
		for (int i = 0; i < Integer.MAX_VALUE; i++);
	}
	@Override
	public void test3() {
		for (int i = 0; i < Integer.MAX_VALUE; i++)
		freeConnection(rs, ocstmt);
	}
	@Override
	public void test4() {
		for (int i = 0; i < Integer.MAX_VALUE; i++)
		freeConnection(rs, ocstmt, cstmt, pstmt, con);
	}
	@Override
	public void test5() {
		for (int i = 0; i < Integer.MAX_VALUE; i++)
		freeConnection(null, null, null, null, null);
	}

	private void freeConnection (ResultSet rs, OracleCallableStatement ocstmt) {
		try {
			if(rs!=null) rs.close();
			if(ocstmt!=null) ocstmt.close();
		} catch (Exception e) { e.printStackTrace(); }
	}

	private void freeConnection (ResultSet rs, OracleCallableStatement ocstmt, CallableStatement cstmt, PreparedStatement pstmt, Connection con) {
		try {
			if(rs!=null) rs.close();
			if(ocstmt!=null) ocstmt.close();
			if(cstmt!=null) cstmt.close();
			if(pstmt!=null) pstmt.close();
			if(con!=null) con.close();
		} catch (Exception e) { e.printStackTrace(); }
	}

	public static void main(String[] args) {
		new IfCloseTest();
	}
}
