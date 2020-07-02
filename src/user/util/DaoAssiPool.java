package user.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import oracle.jdbc.internal.OracleCallableStatement;

public class DaoAssiPool extends JFrame implements ActionListener, ItemListener {

	private final String _DRIVER = "oracle.jdbc.driver.OracleDriver";
	private String _URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String _USER = "scott";
	private String _PW = "tiger";
	private JLabel jlb = new JLabel("SELECT * FROM ");
	private String idFormat = "$('#ins_id').val()";
	private String Macro = "$('#id').textbox('setValue', Row.ID);";
	//	private String Macro = "AND id = #{id}";
	private String dateFormat = "'YYYY-MM-DD'";

	private void setJta() {
		jta.setFont(new Font("Dialog", 0, 16));
		jta.setTabSize(4);
	}
	/* config...
	/*********************************************************************************/

	private JTextArea jta = new JTextArea();
	private JScrollPane jsp = new JScrollPane(jta);
	private JPanel jpSouth = new JPanel();
	private JTextField jtf = new JTextField(25);
	private JPanel jpEast = new JPanel();
	private JPanel jp123 = new JPanel();
	private JLabel jlbc = new JLabel("Code Select");
	private String[] cbList1 = {
			"VO", "SQL_SEL", "SQL_INS", "SQL_UPD", "MybatisINS", "MybatisUPD", "VO list", "Map list", "VO sett", "VO gett", "Map put", "Map get", "Mybatis put", "Form", "Req get", "Macro"
	};
	private JComboBox<String> jcb1 = new JComboBox<>(cbList1);
	private JCheckBox check1 = new JCheckBox("VO private?");
	private JCheckBox check2 = new JCheckBox("Req param?");
	private JLabel jlb00 = new JLabel("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
	private JLabel jlb1 = new JLabel("VO name");
	private JTextField jtf1 = new JTextField();
	private JLabel jlb2 = new JLabel("stmt Name");
	private JTextField jtf2 = new JTextField();
	private JLabel jlb0 = new JLabel("Form ID");
	private JTextField jtf0 = new JTextField(idFormat);
	private JLabel jlb3 = new JLabel("ID Macro");
	private JTextField jtf3 = new JTextField(Macro);
	private JLabel jlb4 = new JLabel("DATE Format");
	private JTextField jtf4 = new JTextField(dateFormat);
	private JButton codeGen = new JButton("Generate Code");

	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String[] strs = new String[cbList1.length];
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

	public DaoAssiPool() {
		loadJDBCDriver();
		initConnectionPool();

		addListener();
		setJta();
		initDisplay();
	}

	private void addListener() {
		jtf.addActionListener(this);
		codeGen.addActionListener(this);
		jcb1.addItemListener(this);
	}

	private void initDisplay() {
		setTitle("DAO Code Assistance v1.0");
		setSize(800, 800);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add("Center", jsp);
		jta.setLineWrap(true);
		add("South", jpSouth);
		jpSouth.setLayout(new FlowLayout(FlowLayout.LEADING));
		jpSouth.add(jlb);
		jpSouth.add(jtf);
		add("East", jpEast);
		jpEast.setLayout(new BorderLayout());
		jpEast.add("North", jp123);
		jp123.setLayout(new GridLayout(16, 1));
		jp123.add(jlbc);
		jp123.add(jcb1);
		jp123.add(jlb1);
		jp123.add(jtf1);
		jp123.add(jlb2);
		jp123.add(jtf2);
		jp123.add(jlb0);
		jp123.add(jtf0);
		jp123.add(jlb3);
		jp123.add(jtf3);
		jp123.add(jlb4);
		jp123.add(jtf4);
		jp123.add(check1);
		check1.setSelected(true);
		jp123.add(check2);
		check2.setSelected(true);
		jp123.add(jlb00);
		jpEast.add("Center", codeGen);
		setVisible(true);
		jtf.grabFocus();
	}

	private void copyCode() {
		int n = jcb1.getSelectedIndex();
		StringSelection ss = new StringSelection(strs[n]);
		clipboard.setContents(ss, null);
		jta.setText(strs[n] + "\n\n\n- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -"
				+ "\n- - - - - - - - - - - - - - - - Code Copy Complete-"
				+ "\n- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n\n");
	}

	private String toLower(String s) {
		char[] ca = s.toCharArray();
		int i = 0;
		for (char c : s.toCharArray()) {
			if (65 <= c && c <= 90) {
				ca[i++] = (char) (c + 32);
			} else {
				i++;
			}
		}
		return String.copyValueOf(ca);
	}

	private String toUpper(String s) {
		char[] ca = s.toCharArray();
		int i = 0;
		for (char c : s.toCharArray()) {
			if (97 <= c && c <= 122) {
				ca[i++] = (char) (c - 32);
			} else {
				i++;
			}
		}
		return String.copyValueOf(ca);
	}

	private String toCamel(String s) {
		char c = s.charAt(0);
		if (97 <= c && c <= 122) {
			c = (char) (c - 32);
		}
		return c + s.substring(1);
	}

	private String toAliasVo(String s) {
		char c = s.charAt(0);
		if (65 <= c && c <= 90) {
			c = (char) (c + 32);
		}
		return c + "VO";
	}

	private String toAliasList(String s) {
		char c = s.charAt(0);
		if (65 <= c && c <= 90) {
			c = (char) (c + 32);
		}
		return c + "List";
	}

	private void selectTable(String table) {
		try {
			//			con = getConnection();
			con = DriverManager.getConnection("jdbc:apache:commons:dbcp:ConPool");
			String sql = table.split(" ").length == 1 ? " WHERE rownum = 0" : " AND rownum = 0";
			pstmt = con.prepareStatement(jlb.getText() + table + sql);
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			String[] col = new String[rsmd.getColumnCount()],// id
					cols = col.clone(),// type id
					fcol = col.clone(),// format id
					rcol = col.clone();// request.getParameter(id)
			boolean[] isDate = new boolean[cols.length];
			int i = 0, z = 0;
			for (; i < strs.length; i++) {
				strs[i] = "";
			}
			boolean isPrivate = check1.isSelected();
			for (i = 1; i <= cols.length; i++) {
				col[i - 1] = toLower(rsmd.getColumnName(i));
				rcol[i - 1] = "request.getParameter(\"" + col[i - 1] + "\") == null ";
				switch (rsmd.getColumnType(i)) {
				case Types.NUMERIC:
					if (rsmd.getScale(i) == 0) {
						cols[i - 1] = "int ";
						rcol[i - 1] += "|| request.getParameter(\"" + col[i - 1] + "\") == \"\" ? 0 : Integer.parseInt(request.getParameter(\"" + col[i - 1] + "\"))";
					} else {
						cols[i - 1] = ("double ");
						rcol[i - 1] += "|| request.getParameter(\"" + col[i - 1] + "\") == \"\" ? 0.0 : Double.parseDouble(request.getParameter(\"" + col[i - 1] + "\"))";
					}
					break;
				case Types.INTEGER:
					cols[i - 1] = "int ";
					rcol[i - 1] += "|| request.getParameter(\"" + col[i - 1] + "\") == \"\" ? 0 : Integer.parseInt(request.getParameter(\"" + col[i - 1] + "\"))";
					break;
				case Types.LONGVARCHAR:
					cols[i - 1] = "long ";
					rcol[i - 1] += "|| request.getParameter(\"" + col[i - 1] + "\") == \"\" ? 0 : Long.parseLong(request.getParameter(\"" + col[i - 1] + "\"))";
					break;
				case Types.DOUBLE:
				case Types.FLOAT:
					cols[i - 1] = "double ";
					rcol[i - 1] += "|| request.getParameter(\"" + col[i - 1] + "\") == \"\" ? 0.0 : Double.parseDouble(request.getParameter(\"" + col[i - 1] + "\"))";
					break;
				case Types.DATE:
				case Types.TIMESTAMP:
					isDate[i - 1] = true;
					cols[i - 1] = "String ";
					rcol[i - 1] += "? \"\" : request.getParameter(\"" + col[i - 1] + "\")";
					break;
				default:
					cols[i - 1] = "String ";
					rcol[i - 1] += "? \"\" : request.getParameter(\"" + col[i - 1] + "\")";
					break;
				}
				// VO
				if (isPrivate) {
					strs[z] += "private " + cols[i - 1] + col[i - 1];
					if (cols[i - 1].compareTo("String ") == 0) {
						strs[z] += " = \"\";\n";
					} else {
						strs[z] += " = 0;\n";
					}
				} else {
					strs[z] += cols[i - 1] + col[i - 1] + " = " + rcol[i - 1] + ";\n";
				}
				cols[i - 1] += col[i - 1];
			}
			if (isPrivate) {
				strs[z] += "private ";
			}
			strs[z] += "String crud = \"\";\n";

			i = 0;
			String w = jtf0.getText();
			for (String s : col) {
				fcol[i] = "";
				int c = 0;
				int u = w.indexOf("id");
				int l = w.indexOf("ID");
				while (u > -1 || l > -1) {
					if (u < l && u > -1 || l < 0) {
						fcol[i] += w.substring(c, u);
						fcol[i] += s;
						c = u + 2;
						u = w.indexOf("id", c);
					} else {
						fcol[i] += w.substring(c, l);
						fcol[i] += toUpper(s);
						c = l + 2;
						l = w.indexOf("ID", c);
					}
				}
				fcol[i++] += w.substring(c);
			}

			z++; // SQL_SEL
			i = 0;
			Iterator<String> is = Arrays.asList(col).iterator();
			strs[z] = "SELECT ";
			String f = jtf4.getText();
			while (is.hasNext()) {
				if (isDate[i++]) {
					strs[z] += "to_char(" + is.next() + ", " + f + ") " + col[i - 1];
				} else {
					strs[z] += is.next();
				}
				if (is.hasNext()) {
					strs[z] += ", ";
				}
			}
			strs[z] += "\nFROM " + table;

			z++; // SQL_INS
			i = 0;
			strs[z] = "INSERT INTO " + table + " (";
			is = Arrays.asList(col).iterator();
			while (is.hasNext()) {
				strs[z] += is.next();
				if (is.hasNext()) strs[z] += ", ";
			}
			strs[z] += ")\nVALUES(";
			is = Arrays.asList(col).iterator();
			while (is.hasNext()) {
				is.next();
				strs[z] += "?";
				if (is.hasNext()) strs[z] += ", ";
			}
			strs[z] += ")";

			z++; // SQL_UPD
			i = 0;
			is = Arrays.asList(col).iterator();
			strs[z] = "UPDATE " + table + " SET ";
			while (is.hasNext()) {
				if (isDate[i]) {
					strs[z] += is.next() + " = to_date(?, " + f + ")";
				} else {
					strs[z] += is.next() + " = ?";
				}
				i++;
				if (is.hasNext()) {
					strs[z] += ", ";
				}
			}
			strs[z] += "\nWHERE " + col[0] + " = ?\n";

			z++; // MybatisINS
			i = 0;
			strs[z] = "INSERT INTO " + table + " (";
			is = Arrays.asList(col).iterator();
			while (is.hasNext()) {
				strs[z] += is.next();
				if (is.hasNext()) strs[z] += ", ";
			}
			strs[z] += ")\nVALUES(";
			is = Arrays.asList(col).iterator();
			while (is.hasNext()) {
				if (isDate[i++]) {
					strs[z] += "to_date(#{" + is.next() + "}, " + f + ")";
				} else {
					strs[z] += "#{" + is.next() + "}";
				}
				if (is.hasNext()) strs[z] += ", ";
			}
			strs[z] += ")";

			z++; // MybatisUPD
			i = 0;
			is = Arrays.asList(col).iterator();
			strs[z] = "UPDATE " + table + " SET ";
			while (is.hasNext()) {
				if (isDate[i]) {
					strs[z] += col[i++] + " = to_date(#{" + is.next() + "}, " + f + ")";
				} else {
					strs[z] += col[i++] + " = #{" + is.next() + "}";
				}
				if (is.hasNext()) {
					strs[z] += ", ";
				}
			}
			strs[z] += "\nWHERE " + col[0] + " = " + "#{" + col[0] + "}\n";

			z++; // VO list
			String vo = jtf1.getText().compareTo("") != 0 ? jtf1.getText() : table.split(" ")[0] + "Vo";
			strs[z] += "List<" + vo + "> " + toAliasList(vo) + " = null;\n\n";
			strs[z] += toAliasList(vo) + " = new ArrayList<>();\n";
			strs[z] += "while (rs.next()) {\n";
			String ivo = toAliasVo(vo);
			strs[z] += "	" + vo + " " + ivo + " = new " + vo + "();\n";
			for (String s : cols) {
				String[] sa = s.split(" ");
				strs[z] += "	" + ivo + ".set" + toCamel(sa[0]) + "(\"" + sa[1] + "\", rs.get" + toCamel(sa[0]) + "(\"" + sa[1] + "\"));\n";
			}
			strs[z] += "	" + toAliasList(vo) + ".add(" + ivo + ");\n";
			strs[z] += "}\n";

			z++; // Map list
			strs[z] += "List<Map<String, Object>> mList = null;\n\n";
			strs[z] += "mList = new ArrayList<>();\n";
			strs[z] += "while (rs.next()) {\n";
			strs[z] += "	Map<String, Object> rMap = new HashMap<>();\n";
			for (String s : cols) {
				String[] sa = s.split(" ");
				strs[z] += "	rMap.put(\"" + sa[1] + "\", rs.get" + toCamel(sa[0]) + "(\"" + sa[1] + "\"));\n";
			}
			strs[z] += "	mList.add(rMap);\n";
			strs[z] += "}\n";

			z++; // VO sett
			strs[z] += vo + " " + ivo + " = new " + vo + "();\n";
			boolean isReq = check2.isSelected();
			i = 0;
			for (String s : col) {
				if (isReq) strs[z] += ivo + ".set" + toCamel(s) + "(" + rcol[i++] + ");\n";
				else
					strs[z] += ivo + ".set" + toCamel(s) + "(" + fcol[i++] + ");\n";
			}
			if (isReq) {
				strs[z] += ivo + ".setCrud(request.getParameter(\"crud\") == null ? \"\" : request.getParameter(\"crud\"));\n";
			} else {
				strs[z] += ivo + ".setCrud(\"\");\n";
			}

			String[] a = cols[0].split(" ");
			z++; // VO gett
			strs[z] += "int i = 0;\n";
			String stmt = jtf2.getText().compareTo("") != 0 ? jtf2.getText() : "pstmt";
			for (String s : cols) {
				String[] sa = s.split(" ");
				strs[z] += stmt + ".set" + toCamel(sa[0]) + "(++i, " + ivo + ".get" + toCamel(sa[1]) + "());\n";
			}
			strs[z] += "\n" + stmt + ".set" + toCamel(a[0]) + "(++i, " + ivo + ".get" + toCamel(a[1]) + "());\n";
			strs[z] += stmt + ".executeUpdate();\n";

			z++; // Map put
			strs[z] += "Map<String, Object> pMap = new HashMap<>();\n";
			i = 0;
			for (String s : col) {
				if (isReq) {
					strs[z] += "pMap.put(\"" + s + "\", " + rcol[i++] + ");\n";
				} else {
					strs[z] += "pMap.put(\"" + s + "\", " + fcol[i++] + ");\n";
				}
			}
			if (isReq) {
				strs[z] += "pMap.put(\"crud\", request.getParameter(\"crud\") == null ? \"\" : request.getParameter(\"crud\"));\n";
			} else {
				strs[z] += "pMap.put(\"crud\", \"\");\n";
			}

			z++; // Map get
			strs[z] += "int i = 0;\n";
			for (String s : cols) {
				String[] sa = s.split(" ");
				strs[z] += stmt + ".set" + toCamel(sa[0]) + "(++i, (" + sa[0] + ")pMap.get(\"" + sa[1] + "\"));\n";
			}
			strs[z] += "\n" + stmt + ".set" + toCamel(a[0]) + "(++i, (" + a[0] + ")pMap.get(\"" + a[1] + "\"));\n";
			strs[z] += stmt + ".executeUpdate();\n";

			z++; // Mybatis put
			strs[z] += "Map<String, String> pMap = new HashMap<>();\n";
			i = 0;
			for (String s : col) {
				strs[z] += "pMap.put(\"" + s + "\", request.getParameter(\"" + s + "\") == null ? \"\" : request.getParameter(\"" + s + "\"));\n";
			}
			if (isReq) {
				strs[z] += "pMap.put(\"crud\", request.getParameter(\"crud\") == null ? \"\" : request.getParameter(\"crud\"));\n";
			} else {
				strs[z] += "pMap.put(\"crud\", \"\");\n";
			}

			z++; // Form
			i = 0;
			is = Arrays.asList(col).iterator();
			strs[z] = "\"&";
			while (is.hasNext()) {
				strs[z] += is.next() + "=\"+" + fcol[i++];
				if (is.hasNext()) {
					strs[z] += "+\"&";
				}
			}
			//			strs[z] += ";";

			z++; // Req get
			i = 0;
			for (String s : cols) {
				strs[z] += s + " = " + rcol[i++] + ";\n";
			}

			z++; // Macro
			String mac = jtf3.getText();
			for (String s : col) {
				int c = 0;
				int u = mac.indexOf("id");
				int l = mac.indexOf("ID");
				while (u > -1 || l > -1) {
					if (u < l && u > -1 || l < 0) {
						strs[z] += mac.substring(c, u);
						strs[z] += s;
						c = u + 2;
						u = mac.indexOf("id", c);
					} else {
						strs[z] += mac.substring(c, l);
						strs[z] += toUpper(s);
						c = l + 2;
						l = mac.indexOf("ID", c);
					}
				}
				strs[z] += mac.substring(c) + "\n";
			}
		} catch (Exception e) {
			for (int i = 0; i < strs.length; i++) {
				strs[i] = "error: Not Found table";
			}
			e.printStackTrace();
		} finally {
			freeConnection(rs, null, null, pstmt, con);
		}
	}

	private void loadJDBCDriver() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("fail to load JDBC Driver", ex);
		}
	}

	private void initConnectionPool() {
		try {
			String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:orcl";
			String username = "scott";
			String pw = "tiger";
			String poolName = "ConPool";

			ConnectionFactory connFactory =
					new DriverManagerConnectionFactory(jdbcUrl, username, pw);

			PoolableConnectionFactory poolableConnFactory =
					new PoolableConnectionFactory(connFactory, null);
			poolableConnFactory.setValidationQuery("select 1");

			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
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
		} finally {
		}
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

	public static void main(String[] args) {
		setDefaultLookAndFeelDecorated(true);
		// DaoAssi da = new DaoAssi();
		new DaoAssiPool();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		if (obj.equals(jcb1)) {
			copyCode();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj.equals(jtf) || obj.equals(codeGen)) {
			selectTable(jtf.getText());
			copyCode();
		}
	}
}
