package org.bklab.tools;

import org.apache.maven.shared.invoker.*;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DependenciesFinder {

    private String pomPath = System.getProperty("user.dir") + File.separator + "pom.xml";
    private String mavenHomePath = System.getenv().get("maven_home");

    public List<DependencyEntity> find() {
        if (pomPath == null || pomPath.isEmpty() || mavenHomePath == null || mavenHomePath.isEmpty()) return null;

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(pomPath));
        request.setGoals(Arrays.asList("dependency:list"));

        Properties properties = new Properties();
        properties.setProperty("outputFile", "dependencies.txt");
        properties.setProperty("outputAbsoluteArtifactFilename", "true");
        properties.setProperty("includeScope", "runtime");

        request.setProperties(properties);

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(mavenHomePath));
        invoker.setOutputHandler(null); // not interested in Maven output itself
        InvocationResult result = null;

        try {
            result = invoker.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result.getExitCode() != 0) throw new IllegalStateException("Build failed.");

        List<DependencyEntity> dependencyEntityList = new ArrayList<>();
        String line = "", path = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("dependencies.txt"))) {
            while (!"The following files have been resolved:".equals(reader.readLine())) ;

            while ((line = reader.readLine()) != null && !line.isEmpty()) {

                Matcher pathMatcher = Pattern.compile("(?:compile|runtime):(.*)").matcher(line);
                if (pathMatcher.find()) path = pathMatcher.group(1);

                String[] vars = line.split(":");
                if (vars.length == 7) {
                    dependencyEntityList.add(new DependencyEntity()
                            .setGroupId(vars[0].trim())
                            .setArtifactId(vars[1])
                            .setType(vars[2])
                            .setVersion(vars[3])
                            .setScope(vars[4])
                            .setLocalPath(path)
                            .generateRemoteUrl());
                } else if (vars.length == 8) {
                    dependencyEntityList.add(new DependencyEntity()
                            .setGroupId(vars[0].trim())
                            .setArtifactId(vars[1])
                            .setType(vars[2])
                            .setClassifier(vars[3])
                            .setVersion(vars[4])
                            .setScope(vars[5])
                            .setLocalPath(path)
                            .generateRemoteUrl());
                }
            }
        } catch (Exception e) {
            System.out.println(line);
            e.printStackTrace();
        }

        return dependencyEntityList;
    }

    public String getPomPath() {
        return pomPath;
    }

    public DependenciesFinder setPomPath(String pomPath) {
        this.pomPath = pomPath;
        return this;
    }

    public String getMavenHomePath() {
        return mavenHomePath;
    }

    public DependenciesFinder setMavenHomePath(String mavenHomePath) {
        this.mavenHomePath = mavenHomePath;
        return this;
    }
}
