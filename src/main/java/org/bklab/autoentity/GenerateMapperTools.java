package org.bklab.autoentity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class GenerateMapperTools {

    boolean printDate = true;           // 是否需要打印生成时间
    private String tablename = "";      // 表名
    private String currentClassName;
    private String shortName;
    private String dUrl = "\\org\\bklab\\autoentity\\entity\\";     // 文件存储位置
    private String pack = dUrl.substring(dUrl.indexOf("\\") + 1, dUrl.length() - 1).replace("\\", "."); // 包名
    private String[] colnames;          // 列名数组
    private String[] colTypes;          // 列名类型数组
    private int[] colSizes;             // 列名大小数组
    private boolean importUtilPackage = false;     // 是否需要导入包java.util.*
    private boolean importSqlPackage = false;      // 是否需要导入包java.sql.*
    private Connection conn;

    public GenerateMapperTools() {
        conn = SuperDao.getConnection();    // 得到数据库连接

        try {
            ResultSet rs = conn.getMetaData().getTables(null, "%", "%", new String[]{"TABLE"});
            while (rs.next()) {
                tablename = rs.getString("TABLE_NAME");
                currentClassName = UnderlineCamelConverter.underline2Camel(tablename, false);
                shortName = TypeUtils.getLocalVariableShortName(currentClassName);
                writeToFile(generateSetPart(generateBody(generateHeader(new StringBuffer())), rs.getString("TABLE_NAME")));
                importSqlPackage = false;
                importUtilPackage = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SuperDao.closeConnection();
        }
    }

    private void writeToFile(StringBuffer sb) {

        try {
            // 生成文件地址
            File dir = new File(System.getProperty("user.dir") + "\\src\\main\\java" + dUrl);
            File file = new File(System.getProperty("user.dir") + "\\src\\main\\java" + dUrl + currentClassName + "Mapper.java");
            if (!dir.exists()) dir.mkdirs();
            if (!file.exists()) file.createNewFile();
            System.out.println(file.getPath());
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            pw.println(sb);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StringBuffer generateSetPart(StringBuffer sb, String tablename) {

        String strsql = "select * from " + tablename;
        try {
            ResultSetMetaData rsmd = conn.prepareStatement(strsql).getMetaData();
            int size = rsmd.getColumnCount(); // 共有多少列
            colnames = new String[size];
            colTypes = new String[size];
            colSizes = new int[size];

            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                colnames[i] = UnderlineCamelConverter.underline2Camel(rsmd.getColumnName(i + 1));
                colTypes[i] = rsmd.getColumnTypeName(i + 1);
                if (colTypes[i].equalsIgnoreCase("datetime")) {
                    importUtilPackage = true;
                }
                if (colTypes[i].equalsIgnoreCase("text") || colTypes[i].equalsIgnoreCase("image")) {
                    importSqlPackage = true;
                }
                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
            }

            for (int i = 0; i < colnames.length; i++) {
                sb.append("\t\t" + shortName + ".set" + TypeUtils.InitCap(colnames[i]) + "(rs.get" + TypeUtils.InitCap(TypeUtils.SqlType2JavaType(colTypes[i])) + "(\"" + rsmd.getColumnName(i + 1) + "\"));\r\n");
            }

            sb.append("\t\treturn " + shortName + ";\r\n\t}\r\n}");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sb;
    }

    private StringBuffer generateHeader(StringBuffer sb) {
        return sb.append("package " + pack + ";\r\n\r\n")
                .append("import java.sql.ResultSet;\r\n")
                .append("import java.sql.SQLException;\r\n")
                .append("import java.sql.Timestamp;\r\n\r\n")
                .append("import " + pack + "." + currentClassName + ";\r\n")
                .append("import " + pack + "." + currentClassName + "State;\r\n")
                .append("import dataq.core.jdbc.IRowMapper;\r\n")
                .append("import dataq.util.DateTime;\r\n\r\n");
    }

    private StringBuffer generateBody(StringBuffer sb) {
        return sb.append("public class " + currentClassName + "Mapper implements IRowMapper{\r\n")
                .append("\r\n\t@Override\r\n")
                .append("\tpublic " + currentClassName + " mapRow(ResultSet rs) throws Exception{\r\n")
                .append("\t\t" + currentClassName + " " + shortName + " = new " + currentClassName + "();\r\n\r\n");
    }

}
