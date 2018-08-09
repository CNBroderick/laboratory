package org.bklab.vaadin.autouploadwar;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadPom {

    /**
     * 针对war包命名格式为 项目名称 + .war
     * @return war 的路径
     * @throws IOException
     * @throws XmlPullParserException
     */
    public String findWarPath() throws IOException, XmlPullParserException{
        String pomPath = String.format("%s%spom.xml", System.getProperty("user.dir"), File.separator);
        return String.format("%s%starget%s%s.war", System.getProperty("user.dir"), File.separator, File.separator, new MavenXpp3Reader().read(new FileReader(pomPath)).getArtifactId());
    }

    /**
     * 针对war包命名格式为 项目名称 + "-" + "版本号" + ".war"
     * @return war 的路径
     * @throws IOException
     * @throws XmlPullParserException
     */
    public String findNameVersionWarPath() throws IOException, XmlPullParserException{
        String pomPath = String.format("%s%spom.xml", System.getProperty("user.dir"), File.separator);
        Model model = new MavenXpp3Reader().read(new FileReader(pomPath));
        return String.format("%s%starget%s%s-%s.war", System.getProperty("user.dir"), File.separator, File.separator, model.getArtifactId(), model.getVersion());
    }

    /**
     * 获取项目名称
     * @return 项目名称
     * @throws IOException
     * @throws XmlPullParserException
     */
    public String getProjectName() throws IOException, XmlPullParserException{
        return new MavenXpp3Reader().read(new FileReader(String.format("%s%spom.xml", System.getProperty("user.dir"), File.separator))).getArtifactId();
    }

    /**
     * 获取项目版本
     * @return 项目版本
     * @throws IOException
     * @throws XmlPullParserException
     */
    public String getProjectVersion() throws IOException, XmlPullParserException{
        return new MavenXpp3Reader().read(new FileReader(String.format("%s%spom.xml", System.getProperty("user.dir"), File.separator))).getVersion();
    }

    public File getPomXml() {
        return new File(String.format("%s%spom.xml", System.getProperty("user.dir"), File.separator));
    }

    public Map<String ,String> getDependencies() throws IOException, XmlPullParserException {

        MavenXpp3Reader reader = new MavenXpp3Reader();
        List<Dependency> dependencies = reader.read(new FileReader(String.format("%s%spom.xml", System.getProperty("user.dir"), File.separator))).getDependencies();
        Map<String ,String> d = new HashMap<>();
        dependencies.forEach( dependency -> {
            d.put(dependency.getArtifactId(),dependency.getGroupId());
            System.out.printf("%s\t%s\t%s\r\n",
                    dependency.getArtifactId(),
                    dependency.getGroupId(),
                    dependency.getVersion());
        });
        return d;
    }

}
