package org.bklab.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

/**
 * Merge jars to ones.
 * Only adapt for Windows or Linux.
 */
public class JarsMerger {

    private File javaJar;
    private List<File> jars;
    private File jar;

    public File start() {

        if (javaJar == null) setJavaJar();
        if (getJavaJar() == null) return null;

        File tempDirectory = new File(System.getProperty("user.dir") + File.separator + "lib" + File.separator + Long.toString(new Date().getTime()));
        tempDirectory.mkdirs();
        jars.forEach(jar -> {
            try {
                Files.copy(jar.toPath(), tempDirectory.toPath().resolve(jar.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception ignore) {
            }
        });

        File bat = new File(tempDirectory.getPath() + File.separator + (System.getProperty("os.name").contains("Windows") ? "merger.bat" : "merger.sh"));
        try {
            FileWriter batWriter = new FileWriter(bat);
            batWriter.write("cd /d \"" + tempDirectory.getPath() + "\"\r\n");
            batWriter.write("jar -cvf \"" + jar.getPath() + "\" *.jar");
            batWriter.flush();
            batWriter.close();
            Process process = Runtime.getRuntime().exec(bat.getPath());

            String s;
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), (System.getProperty("os.name").contains("Windows") ? "GBK" : "UTF-8")));
            while ((s = reader.readLine()) != null) System.out.println(s);
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), (System.getProperty("os.name").contains("Windows") ? "GBK" : "UTF-8")));
            while ((s = reader.readLine()) != null) System.out.println(s);

            fileDeleter(tempDirectory).getParentFile().delete();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return jar;
    }


    public File getJavaJar() {
        return this.javaJar;
    }

    public JarsMerger setJavaJar(File javaJar) {
        this.javaJar = javaJar;
        return this;
    }

    public JarsMerger setJavaJar() {
        final String javaLibraryPath = System.getProperty("java.library.path");
        String jvmFolder = javaLibraryPath.substring(0,
                javaLibraryPath.indexOf(System.getProperty("os.name").contains("Windows") ? ";" : ":"));
        File jar = new File(jvmFolder + File.separator + (System.getProperty("os.name").contains("Windows") ? "jar.exe" : "jar"));
        this.javaJar = jar;
        return this;
    }

    public File getJar() {
        return jar;
    }

    public JarsMerger setJar(File jar) {
        this.jar = jar;
        return this;
    }

    public List<File> getJars() {
        return jars;
    }

    public JarsMerger setJars(List<File> jars) {
        this.jars = jars;
        return this;
    }

    private File fileDeleter(File f) {
        if (f.isDirectory())
            for (File s : f.listFiles()) fileDeleter(s);
        f.delete();
        return f;
    }
}
