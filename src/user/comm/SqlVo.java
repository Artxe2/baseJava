package user.comm;

public class SqlVo {
	/* protocol value object */
	protected static final String SELECT = "SELECT ? FROM table WHERE column = '%'||?||'%' ";
	protected static final String SQL = "INSERT INTO table VALUES(?,?,?)";
	protected static final String CURSOR = "{call proc_memberList(?,?)}";
	protected static final String PLSQL = "{call proc_memberList(?,?,?)}";
}
