package user.comm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import oracle.jdbc.internal.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;

public class Dao {

	private final String _DRIVER = "oracle.jdbc.driver.OracleDriver";
	private String _URL = "jdbc:oracle:thin:@localhost:1521:orcl11";
	private String _USER = "SCOTT";
	private String _PW = "tiger";

	private static Connection con = null;
	private static PreparedStatement pstmt = null;
	private static CallableStatement cstmt = null;
	private static OracleCallableStatement ocstmt = null;
	private static ResultSet rs = null;
	private Dao dao = Dao.getInstance();

	private Dao() {
		initConnectionPool();
	}

	public static synchronized Dao getInstance() {
		return LazyHolder.instance;
	}

	private static class LazyHolder {
		private static final Dao instance = new Dao();
	}


	//	con.getAutoCommit();
	//	con.commit();
	//	con.rollback();
	protected List<Map<String, Object>> select(Map<String, Object> pMap) {
		int i = 0;
		try {
//			con = getConnection();
			con = DriverManager.getConnection("jdbc:apache:commons:dbcp:poolName");
			pstmt = con.prepareStatement(SqlVo.SELECT);
			i = 0;
			pstmt.setString(++i, (String) pMap.get("key"));
			rs = pstmt.executeQuery();
			List<Map<String, Object>> result = new ArrayList<>();
			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				i = 0;
				row.put("column", rs.getObject(++i));
				result.add(row);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SqlVo.SELECT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			freeConnection(rs, null, null, pstmt, con);
		}
		return null;
	}

	protected int sql(Map<String, Object> pMap) {
		int i = 0;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SqlVo.SQL);
			i = 0;
			pstmt.setString(++i, (String) pMap.get("key"));
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SqlVo.SQL);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			freeConnection(null, null, null, pstmt, con);
		}
		return -1;
	}

	protected List<Map<String, Object>> openCursor(Map<String, Object> pMap) {
		int i = 0;
		try {
			con = getConnection();
			ocstmt = (OracleCallableStatement) con.prepareCall(SqlVo.CURSOR);
			i = 0;
			ocstmt.setString(++i, (String) pMap.get("key"));
			ocstmt.registerOutParameter(++i, OracleTypes.CURSOR);
			rs = ocstmt.getCursor(i);
			List<Map<String, Object>> result = new ArrayList<>();
			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				i = 0;
				row.put("column", rs.getObject(++i));
				result.add(row);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SqlVo.CURSOR);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			freeConnection(rs, ocstmt, null, null, con);
		}
		return null;
	}

	protected String procedure(Map<String, Object> pMap) {
		int i = 0;
		try {
			con = getConnection();
			cstmt = con.prepareCall(SqlVo.PLSQL);
			i = 0;
			cstmt.setString(++i, (String) pMap.get("key"));
			cstmt.registerOutParameter(++i, java.sql.Types.VARCHAR);
			cstmt.executeUpdate();
			i = 0;
			return cstmt.getString(++i);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SqlVo.PLSQL);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			freeConnection(null, null, cstmt, null, con);
		}
		return "";
	}

	private Connection getConnection() {
		try {
			Class.forName(_DRIVER);
			con = DriverManager.getConnection(_URL, _USER, _PW);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return con;
	}

	private void initConnectionPool() {
		try {
			String poolName = "poolName";

			ConnectionFactory connFactory =
					new DriverManagerConnectionFactory(_URL, _USER, _PW);

			PoolableConnectionFactory poolableConnFactory =
					new PoolableConnectionFactory(connFactory, null);
			poolableConnFactory.setValidationQuery("SELECT 1");

			GenericObjectPoolConfig<PoolableConnection> poolConfig = new GenericObjectPoolConfig<>();
			poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L * 5L);
			poolConfig.setTestWhileIdle(true);
			poolConfig.setMinIdle(4);
			poolConfig.setMaxTotal(50);

			GenericObjectPool<PoolableConnection> connectionPool =
					new GenericObjectPool<>(poolableConnFactory, poolConfig);
			poolableConnFactory.setPool(connectionPool);

			Class.forName("org.apache.commons.dbcp2.PoolingDriver");
			PoolingDriver driver =
					(PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			driver.registerPool(poolName, connectionPool);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void freeConnection(ResultSet rs, OracleCallableStatement ocstmt, CallableStatement cstmt, PreparedStatement pstmt, Connection con) {
		try {
			if (rs != null) rs.close();
			if (ocstmt != null) ocstmt.close();
			if (cstmt != null) cstmt.close();
			if (pstmt != null) pstmt.close();
			if (con != null) con.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}
