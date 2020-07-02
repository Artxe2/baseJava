package user.comm;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

public class MyBatis {

	Logger logger = Logger.getLogger(this.getClass());
	SqlSessionFactory sqlMapper = null;

	public List<Map<String, Object>> SELECT(Map<String, String> pMap) {
		String command = pMap.get("command");
		logger.info(command + " start");
//		logger.info("");
//		logger.debug("");
//		logger.warn("");
//		logger.error("");
//		logger.fatal("");
		List<Map<String, Object>> rList = null;
		try {
			Reader reader = Resources.getResourceAsReader("user/comm/Configuration.xml");
			if (sqlMapper == null) {
				sqlMapper = new SqlSessionFactoryBuilder().build(reader);
			}
			SqlSession sqlSes = sqlMapper.openSession();
			rList = sqlSes.selectList(command, pMap);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.error(sw.toString());
		} finally {
		}
		return rList;
	}
}
