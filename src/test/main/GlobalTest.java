package main;

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.SftpException;
import org.apache.maven.shared.invoker.*;
import org.bklab.*;
import org.bklab.vaadin.autouploadwar.BroderickSftpUtil;
import org.bklab.vaadin.autouploadwar.ReadPom;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalTest {
    @Test
    public void testUploadAbilities() {
        // 从 获取项目路径 +　pom.xml 处定义 pom 文件
        File pom = new File(String.format("%s%spom.xml", System.getProperty("user.dir"), File.separator));
        File war;

        try {
            war = new File(new ReadPom().findWarPath());

            // 初始化SFTP链接信息
            BroderickSftpUtil sftpUtil = new BroderickSftpUtil("BroderickSftp", "broderick.06201", "broderick.cn", 62);

            // 远程登陆SFTP服务器
            sftpUtil.login();

            // 上传 war 文件
            sftpUtil.upload("default/d/", war.getName(), war);

            // 上传 pom 文件
            sftpUtil.upload("default/d/", pom.getName(), pom);

            // 从远程SFTP服务器登出
            sftpUtil.logout();

        } catch (SftpException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            ReadPom pom = new ReadPom();
            System.out.println(pom.getDependencies().size());

        } catch (Exception ignore) {}
    }
    @Test
    public void test3() {
        System.out.println(.1 +.2);
        System.out.println(Math.pow(216, 1.0/3));
        System.out.println(((float)Math.pow(216, 1.0/3))==6);
    }

    @Test
    public void test4() {
            String line = "broderick_labs";
            //下划线转驼峰（大驼峰）
            //BroderickLabs
            String camel = UnderlineCamelConverter.underline2Camel(line, false);
            System.out.println(camel);

            //下划线转驼峰（小驼峰）
            //broderickLabs
            camel = UnderlineCamelConverter.underline2Camel(line);
            System.out.println(camel);

            //驼峰转下划线
            //BRODERICK_LABS
            System.out.println(UnderlineCamelConverter.camel2Underline(camel));

        System.out.println(UnderlineCamelConverter.underline2Camel("broderickLabs"));
    }

    @Test
    public void test5() {
        new GenerateEntityTools();
    }

    @Test
    public void test6() {
        new GenerateMapperTools();
    }

    @Test
    public void test7() {
        new GenerateAddTools();
    }

    @Test
    public void test8() {
        new GenerateQueryTools();
    }

    @Test
    public void test9() {
        try {
            System.out.println(new PinyinTools()
                    .setEnableFirstCase()
                    .setWithToneNumber()
                    .toPinYin("天朝蓝鹰", "_"));

        } catch ( Exception ignore){}

    }

    @Test
    public void test10() {
        new AntBuildXmlGenerator().setBase(new File("E:\\Broderick Developments\\libs\\")).generateXml();
        System.out.println(new Date().toString());
    }

    @Test
    public void test11() {
        try {
            Process p = Runtime.getRuntime().exec("mvn clean install -DmyAwsomeProperty=awsome");
            p.waitFor();

            String line;

            BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while((line = error.readLine()) != null){
                System.out.println(line);
            }
            error.close();

            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((line=input.readLine()) != null){
                System.out.println(line);
            }

            input.close();

            OutputStream outputStream = p.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            printStream.println();
            printStream.flush();
            printStream.close();
        } catch (Exception ignore){}
    }

    @Test
    public void test12() throws Exception{
        String pomPath = "E:\\Broderick Developments\\IdeaProjects\\laboratory\\pom.xml";
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(pomPath));
        request.setGoals(Arrays.asList("dependency:list"));
        Properties properties = new Properties();
        properties.setProperty("outputFile", "dependencies.txt"); // redirect output to a file
        properties.setProperty("outputAbsoluteArtifactFilename", "true"); // with paths
        properties.setProperty("includeScope", "runtime"); // only runtime (scope compile + runtime)
// if only interested in scope runtime, you may replace with excludeScope = compile
        request.setProperties(properties);

        Invoker invoker = new DefaultInvoker();
// the Maven home can be omitted if the "maven.home" system property is set
        invoker.setMavenHome(new File("E:\\Broderick Developments\\apache-maven-3.5.4\\"));
        invoker.setOutputHandler(null); // not interested in Maven output itself
        InvocationResult result = invoker.execute(request);
        if (result.getExitCode() != 0) {
            throw new IllegalStateException("Build failed.");
        }

        Pattern pattern = Pattern.compile("(?:compile|runtime):(.*)");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("dependencies.txt"))) {
            while (!"The following files have been resolved:".equals(reader.readLine()));
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    // group 1 contains the path to the file
                    System.out.println(matcher.group(1));
                }
            }
        }
    }

    @Test
    public void test13() {
        String g = "org.apache.maven";
        String a = "maven-artifact";
        String v = "3.1.1";
        String t = "jar";
        System.out.println(new DependencyEntity()
                .setArtifactId(a)
                .setGroupId(g)
                .setType(t)
                .setVersion(v)
                .generateRemoteUrl().getRemoteApacheMaven2Url()
        );
        System.out.println(new DependencyEntity()
                .setArtifactId(a)
                .setGroupId(g)
                .setType(t)
                .setVersion(v)
                .generateRemoteUrl().getRemoteAliyunMaven2Url()
        );
    }

    @Test
    public void test14() {
        String line = "org.codehaus.plexus:plexus-interpolation:jar:1.19:compile:C:\\Users\\Broderick\\.m2\\repository\\org\\codehaus\\plexus\\plexus-interpolation\\1.19\\plexus-interpolation-1.19.jar";
        String path2 = "org.sonatype.sisu:sisu-guice:jar:no_aop:3.1.0:compile:C:\\Users\\Broderick\\.m2\\repository\\org\\sonatype\\sisu\\sisu-guice\\3.1.0\\sisu-guice-3.1.0-no_aop.jar";

        DependencyEntity dependency = new DependencyEntity();
        Matcher matcherLocalPath = Pattern.compile("(?:compile|runtime):(.*)").matcher(line);
        String localPath = "";
        if (matcherLocalPath.find()) localPath = matcherLocalPath.group(1);

        String[] vars = line.split(":");
        if(vars.length == 7) {
            dependency.setGroupId(vars[0]).setArtifactId(vars[1]).setType(vars[2]).setVersion(vars[3]).setScope(vars[4]).setLocalPath(localPath).generateRemoteUrl();
        } else if (vars.length == 8) {
            dependency.setGroupId(vars[0])
                    .setArtifactId(vars[1])
                    .setType(vars[2])
                    .setClassifier(vars[3])
                    .setVersion(vars[4])
                    .setScope(vars[5])
                    .setLocalPath(localPath)
                    .generateRemoteUrl();
            ;
        }
        System.out.println(dependency.getRemoteApacheMaven2Url());
        System.out.println(dependency.getRemoteAliyunMaven2Url());
    }

    @Test
    public void test15() {
        String path = "\\Users\\Broderick\\.m2\\repository\\org\\codehaus\\plexus\\plexus-interpolation\\1.19\\plexus-interpolation-1.19.jar";
        String path2 = "\\Users\\Broderick\\.m2\\repository\\org\\sonatype\\sisu\\sisu-guice\\3.1.0\\sisu-guice-3.1.0-no_aop.jar";
        String[] vars = path2.split("\\\\");
        System.out.println("version: " + vars[vars.length - 2]);
        System.out.println("artifactid: " + vars[vars.length - 3]);

    }

    @Test
    public void test16() {
        System.out.println(System.getProperty("user.dir"));
        if(new File(System.getProperty("user.dir") + File.separator + "pom.xml").exists()) {
            System.out.println("find");
        }
        System.out.println(System.getenv().get("maven_home"));
    }

    @Test
    public void test17() {
        String line = "org.codehaus.plexus:plexus-interpolation:jar:1.19:compile:E:\\Broderick Developments\\.m2\\repository\\org\\codehaus\\plexus\\plexus-interpolation\\1.19\\plexus-interpolation-1.19.jar";

        Matcher matcher = Pattern.compile("(?:compile|runtime):(.*)").matcher(line);
        if (matcher.find())
            System.out.println(matcher.group(1));
    }

    @Test
    public void test18() {
        String line = "org.codehaus.plexus:plexus-interpolation:jar:1.19:compile:E:\\Broderick Developments\\.m2\\repository\\org\\codehaus\\plexus\\plexus-interpolation\\1.19\\plexus-interpolation-1.19.jar";
        Pattern pattern = Pattern.compile("(?:compile|runtime):(.*)");

        Matcher matcher = pattern.matcher(line);
        if (matcher.find())
            System.out.println(matcher.group(1));
    }

    @Test
    public void test19() {
        List<DependencyEntity> dependencyEntityList = new DependenciesFinder().find();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\DependencyEntity.json"));
            writer.write(JSON.toJSONString(dependencyEntityList, true));
            writer.flush();
            writer.close();
        } catch (Exception ignore) {};

    }

    @Test
    public void test20() {
        final String javaLibraryPath = System.getProperty("java.library.path");
        String jvmFolder = javaLibraryPath.substring(0,
                javaLibraryPath.indexOf(System.getProperty("os.name").contains("Windows") ? ";" : ":"));
        System.out.println(jvmFolder + File.separator + (System.getProperty("os.name").contains("Windows") ? "jar.exe" : "jar"));
        File jar = new File(jvmFolder + File.separator + (System.getProperty("os.name").contains("Windows") ? "jar.exe" : "jar"));
        System.out.println(jar.getPath());
    }

    @Test
    public void test21() {
        File out = new File("E:\\Broderick Developments\\IdeaProjects\\laboratory\\out.jar");
        List<DependencyEntity> dependencyEntityList = new DependenciesFinder().find();
        List<File> jars = new ArrayList<>();
        dependencyEntityList.forEach(d -> {
            if(new File(d.getLocalPath()).exists())
                jars.add(new File(d.getLocalPath()));
        });
        System.out.println("Merged to:" + new JarsMerger().setJars(jars).setJavaJar().setJar(out).start().getPath());;
    }

    @Test
    public void test22() {
        try {
            Runtime.getRuntime().exec("cmd /c cd /d E:\\Broderick Developments\\IdeaProjects\\laboratory\\lib\\1532327101240");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test23(){
        String fileName = "out.jar";
        try {
            FileWriter bat = new FileWriter("e:\\test.bat");
            bat.write("jar -cvf " + fileName + " *.jar");
            bat.flush();
            bat.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test24(){
        new File("E:\\Broderick Developments\\IdeaProjects\\laboratory\\lib\\1532333962184");
    }

    @Test
    public void test25() {
        File base = new File("E:\\Broderick Developments\\libs\\");
        File build = AntBuildXmlGenerator.create().setBase(base).generateXml();
        System.out.println(AntExecutor.create().setBuildXml(build).execute());
    }
}
