package org.bklab.ant.java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class AntBuildXmlGenerator {

    private File base;

    public static AntBuildXmlGenerator create() {
        return new AntBuildXmlGenerator();
    }

    public File generateXml() {
        String outputFileName = "merge.jar";
        String nextLine = "\r\n";
        StringBuffer xmlBuffer = new StringBuffer();

        xmlBuffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append(nextLine)
                .append("<project ")
                .append("name=\"lzg_dlna\" ")
                .append("basedir=\"").append(base.getPath()).append("\\\" ")
                .append("default=\"makeSuperJar\" >").append(nextLine)
                .append("  <target").append("  ")
                .append("name=\"makeSuperJar\"").append("  ")
                .append("description=\"description\" >").append(nextLine)
                .append("    <jar destfile=\"").append(outputFileName).append("\" >").append(nextLine)
               ;

        File[] jars = base.listFiles();
        for (int i = 0; i < jars.length; i++) {
            if(jars[i].getName().endsWith(".jar"))
            xmlBuffer.append("      <zipfileset src=\"").append(jars[i].getName()).append("\" />").append(nextLine);
        }

        xmlBuffer.append("    </jar>").append(nextLine)
                .append("  </target>").append(nextLine)
                .append("</project>").append(nextLine)
                ;

        BufferedWriter writer;
        File build = new File(base.getPath() + File.separator + "build.xml");
        try {
            writer = new BufferedWriter(new FileWriter(build));
            writer.write(xmlBuffer.toString());
            writer.close();

        } catch (Exception ignore) {}

        return build;
    }

    public File getBase() {
        return base;
    }

    public AntBuildXmlGenerator setBase(File base) {
        this.base = base;
        return this;
    }
}
