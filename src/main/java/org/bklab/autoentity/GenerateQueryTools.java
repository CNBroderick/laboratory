package org.bklab.autoentity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class GenerateQueryTools {

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

    public GenerateQueryTools() {
        conn = SuperDao.getConnection();    // 得到数据库连接

        try {
            ResultSet rs = conn.getMetaData().getTables(null, "%", "%", new String[]{"TABLE"});
            while (rs.next()) {
                tablename = rs.getString("TABLE_NAME");
                currentClassName = UnderlineCamelConverter.underline2Camel(tablename, false);
                shortName = TypeUtils.getLocalVariableShortName(currentClassName);

                writeToFile(generateSetPart(generateBody(generateHeader(new StringBuffer())), tablename));

                importSqlPackage = false;
                importUtilPackage = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SuperDao.closeConnection();
        }
    }


    private StringBuffer generateHeader(StringBuffer sb) {
        return sb.append("package " + pack + ";\r\n\r\n")
                .append("import " + pack + "." + currentClassName + ";\r\n")
                .append("import " + pack + "." + currentClassName + "Query;\r\n")
                .append("import " + pack + "." + currentClassName + "Mapper;\r\n")
                .append("import dataq.core.operation.JdbcQueryOperation;\r\n\r\n");
    }


    private StringBuffer generateBody(StringBuffer sb) {
        return sb.append("public class " + currentClassName + "Query extends JdbcQueryOperation{\r\n")
                .append("\tpublic " + currentClassName + "Query() {\r\n")
                .append("\t\tsetRowMapper(new " + currentClassName + "Mapper());\r\n")
                .append("\t\tsetQueryFor(QueryFor.forList);\r\n")
                .append("\t}\r\n\n")
                .append("\t@Override\r\n")
                .append("\tpublic String createSqlSelect() \r\n{")
                ;
    }

    private StringBuffer generateSetPart(StringBuffer sb, String tablename) {

        String strsql = "select * from " + tablename;
        try {
            ResultSetMetaData rsmd = conn.prepareStatement(strsql).getMetaData();
            int size = rsmd.getColumnCount(); // 共有多少列
            colnames = new String[size];
            colTypes = new String[size];
            colSizes = new int[size];

            sb.append("\t\tString sql = \"SELECT ");

            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                colnames[i] = UnderlineCamelConverter.underline2Camel(rsmd.getColumnName(i + 1));
                sb.append("\"\r\n\t\t\t+ \"").append(rsmd.getColumnName(i + 1)).append(", ");
                colTypes[i] = rsmd.getColumnTypeName(i + 1);

                if (colTypes[i].equalsIgnoreCase("datetime")) {
                    importUtilPackage = true;
                }
                if (colTypes[i].equalsIgnoreCase("image") || "text".equalsIgnoreCase(colTypes[i])) {
                    importSqlPackage = true;
                }
                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
            }

            sb.delete(sb.length() - 2, sb.length());
            sb.append(" FROM " + tablename + " WHERE createWhereCondition()\";\r\n")
                    .append("\t\treturn sql;\r\n\t}\r\n\r\n")
                    .append("\tprivate String createWhereCondition() {\r\n")
                    .append("\t\tStringBuffer sb = new StringBuffer(\"1=1\");\r\n")
                    .append("\t\treturn sb.toString();\r\n\t}\r\n}")
            ;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sb;
    }

    private void writeToFile(StringBuffer sb) {

        try {
            // 生成文件地址
            File dir = new File(System.getProperty("user.dir") + "\\src\\main\\java" + dUrl);
            File file = new File(System.getProperty("user.dir") + "\\src\\main\\java" + dUrl + currentClassName + "Query.java");
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
}
