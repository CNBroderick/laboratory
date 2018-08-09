package org.bklab.labs15.ant.java;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import java.io.*;
import java.util.concurrent.atomic.AtomicReferenceArray;

@SuppressWarnings({"unused","WeakerAccess"})
public class AntExecutor {

    private static final String CONSOLE_ENCODING = System.getProperty("os.name").toLowerCase().contains("windows") ? "GBK" : "UTF8";
    private static final String SEPARATOR = File.separator;
    private String workingPath;
    private File workDirectory;
    private File buildXml;
    private String buildXmlName = "build.xml";
    private File logFile;


    public static AntExecutor create(){
        return new AntExecutor();
    }

    public void execute() {
        if(!preCheck()) return;

        Project p = new Project();

        p.setUserProperty("ant.file", buildXml.getAbsolutePath());
        ByteArrayOutputStream byteStream = null;

        try {
            PrintStream out = new PrintStream(new FileOutputStream(logFile), true, CONSOLE_ENCODING);

            DefaultLogger consoleLogger = new DefaultLogger();
            consoleLogger.setErrorPrintStream(out);
            consoleLogger.setOutputPrintStream(out);
            consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
            p.addBuildListener(consoleLogger);

            p.fireBuildStarted();
            p.init();
            ProjectHelper helper = ProjectHelper.getProjectHelper();
            p.addReference("ant.projectHelper", helper);
            helper.parse(p, buildXml);
            p.executeTarget(p.getDefaultTarget());
            p.fireBuildFinished(null);
        } catch (Exception e) {
            p.fireBuildFinished(e);
        }
    }

    private boolean preCheck() {

        if(workDirectory == null) {
            workDirectory = new File(workingPath);
        }

        if(buildXml == null) {
            AtomicReferenceArray<File> bx = new AtomicReferenceArray<File>(workDirectory.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return ((dir != null) && (name != null)) && name.equalsIgnoreCase(buildXmlName);
                }
            }));
            if(bx.length() < 1) {
                throw new RuntimeException("Configure XML File not found at: " + workDirectory.getAbsolutePath() + SEPARATOR + buildXmlName);
            } else {
                buildXml = bx.get(0);
            }
        }

        if(logFile == null) {
            logFile = new File(workDirectory.getAbsolutePath() + SEPARATOR + "ant.log");
        }

        return workDirectory.exists() && buildXml.exists();
    }

    public static String getConsoleEncoding() {
        return CONSOLE_ENCODING;
    }

    public File getWorkDirectory() {
        return workDirectory;
    }

    public AntExecutor setWorkDirectory(File workDirectory) {
        this.workDirectory = workDirectory;
        return this;
    }

    public File getBuildXml() {
        return buildXml;
    }

    public AntExecutor setBuildXml(File buildXml) {
        this.buildXml = buildXml;
        return this;
    }

    public String getBuildXmlName() {
        return buildXmlName;
    }

    public AntExecutor setBuildXmlName(String buildXmlName) {
        this.buildXmlName = buildXmlName;
        return this;
    }

    public File getLogFile() {
        return logFile;
    }

    public AntExecutor setLogFile(File logFile) {
        this.logFile = logFile;
        return this;
    }

    public static String getSEPARATOR() {
        return SEPARATOR;
    }

    public String getWorkingPath() {
        return workingPath;
    }

    public AntExecutor setWorkingPath(String workingPath) {
        this.workingPath = workingPath;
        return this;
    }
}
