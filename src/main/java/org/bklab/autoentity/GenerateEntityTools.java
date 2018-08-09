package org.bklab.autoentity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateEntityTools {
    boolean printDate = true;           // 是否需要打印生成时间
    private String tablename = "";      // 表名
    private String dUrl = "\\org\\bklab\\autoentity\\entity\\";     // 文件存储位置
    private String pack = dUrl.substring(dUrl.indexOf("\\") + 1, dUrl.length() - 1).replace("\\", "."); // 包名
    private String[] colnames;          // 列名数组
    private String[] colTypes;          // 列名类型数组
    private int[] colSizes;             // 列名大小数组
    private boolean importUtilPackage = false;     // 是否需要导入包java.util.*
    private boolean importSqlPackage = false;      // 是否需要导入包java.sql.*
    private Connection conn;

    public GenerateEntityTools() {
        conn = SuperDao.getConnection();    // 得到数据库连接
        try {
            ResultSet rs = conn.getMetaData().getTables(null, "%", "%", new String[]{"TABLE"});
            while (rs.next()) {
                tablename = rs.getString("TABLE_NAME");
                generateSingleEntity(tablename);
                importSqlPackage = false;
                importUtilPackage = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SuperDao.closeConnection();
        }

    }

    private void generateSingleEntity(String tablename) {
        String strsql = "select * from " + tablename;
        try {

            PreparedStatement pstmt = conn.prepareStatement(strsql);
            ResultSetMetaData rsmd = pstmt.getMetaData();
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
                if (colTypes[i].equalsIgnoreCase("image") || colTypes[i].equalsIgnoreCase("text")) {
                    importSqlPackage = true;
                }
                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
            }
            String content = parse(colnames, colTypes, colSizes);
            try {
                // 生成文件地址
                File dir = new File(System.getProperty("user.dir") + "\\src\\main\\java" + dUrl);
                File file = new File(System.getProperty("user.dir") + "\\src\\main\\java" + dUrl + UnderlineCamelConverter.underline2Camel(TypeUtils.InitCap(tablename), false) + ".java");
                if (!dir.exists()) dir.mkdirs();
                if (!file.exists()) file.createNewFile();
                System.out.println(file.getPath());
                PrintWriter pw = new PrintWriter(new FileWriter(file));
                pw.println(content);
                pw.flush();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 解析处理(生成实体类主体代码)
     *
     * @param colNames
     * @param colTypes
     * @param colSizes
     * @return
     */
    private String parse(String[] colNames, String[] colTypes, int[] colSizes) {
        StringBuffer sb = new StringBuffer("package " + pack + ";\r\n\r\n");
        if (importUtilPackage) {
            sb.append("import java.util.Date;\r\n");
        }
        if (importSqlPackage) {
            sb.append("import java.sql.*;\r\n");
        }
        if (printDate) {
            sb.append("\r\n/**\r\n * auto generate at "
                    + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\r\n*/\r\n");
        }

        sb.append("public class " + UnderlineCamelConverter.underline2Camel(TypeUtils.InitCap(tablename), false) + " {\r\n");
        processAllAttrs(sb);
        processAllConstructor(sb);
        processAllMethod(sb);
        sb.append("}\r\n");
        return sb.toString();
    }

    /**
     * 创建构造方法
     *
     * @param sb
     */
    private void processAllConstructor(StringBuffer sb) {
        sb.append("\tpublic " + UnderlineCamelConverter.underline2Camel(TypeUtils.InitCap(tablename), false) + "(){\r\n\t}\r\n");
    }

    /**
     * 生成所有的方法
     *
     * @param sb
     */
    private void processAllMethod(StringBuffer sb) {
        for (int i = 0; i < colnames.length; i++) {
            sb.append("\tpublic " + UnderlineCamelConverter.underline2Camel(TypeUtils.InitCap(tablename), false)
                    + " set" + TypeUtils.InitCap(colnames[i]) + "(" + TypeUtils.SqlType2JavaType(colTypes[i]) + " " + colnames[i] + "){\r\n");
            sb.append("\t\tthis." + colnames[i] + "=" + colnames[i] + ";\r\n");
            sb.append("\t\treturn this;\r\n");
            sb.append("\t}\r\n");
            sb.append("\tpublic " + TypeUtils.SqlType2JavaType(colTypes[i]) + " get" + TypeUtils.InitCap(colnames[i]) + "(){\r\n");
            sb.append("\t\treturn " + colnames[i] + ";\r\n");
            sb.append("\t}\r\n");
        }
    }

    /**
     * 解析输出属性
     *
     * @param sb
     */
    private void processAllAttrs(StringBuffer sb) {
        for (int i = 0; i < colnames.length; i++) {
            sb.append("\tprivate " + TypeUtils.SqlType2JavaType(colTypes[i]) + " " + colnames[i] + ";\r\n");
        }
    }


} 