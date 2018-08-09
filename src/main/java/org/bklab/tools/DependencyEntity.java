package org.bklab.tools;

@SuppressWarnings("unused")
public class DependencyEntity {

    private final static String ApacheMaven2ServerRoot = "https://repo.maven.apache.org/maven2/";
    private final static String AliyunMaven2ServerRoot = "http://maven.aliyun.com/nexus/content/groups/public/";
    private String groupId = "";
    private String artifactId = "";
    private String version = "";
    private String type = "";
    private String scope = "";
    private String classifier = "";
    private String localPath = "";
    private String remoteApacheMaven2Url = "";
    private String remoteAliyunMaven2Url = "";

    public DependencyEntity generateRemoteUrl() {
        char[] gs = groupId.toCharArray();
        for (int i = 0; i < gs.length; i++) if(gs[i] == '.') gs[i] = '/';
        String uri = classifier.isEmpty()
                ? new String(gs) + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + "." + type
                : new String(gs) + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + "-" + classifier + "." + type;
        setRemoteApacheMaven2Url(ApacheMaven2ServerRoot + uri);
        setRemoteAliyunMaven2Url(AliyunMaven2ServerRoot + uri);
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public DependencyEntity setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public DependencyEntity setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public DependencyEntity setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getType() {
        return type;
    }

    public DependencyEntity setType(String type) {
        this.type = type;
        return this;
    }

    public String getClassifier() {
        return classifier;
    }

    public DependencyEntity setClassifier(String classifier) {
        this.classifier = classifier;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public DependencyEntity setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public String getLocalPath() {
        return localPath;
    }

    public DependencyEntity setLocalPath(String localPath) {
        this.localPath = localPath;
        return this;
    }

    public String getRemoteApacheMaven2Url() {
        return remoteApacheMaven2Url;
    }

    private DependencyEntity setRemoteApacheMaven2Url(String remoteApacheMaven2Url) {
        this.remoteApacheMaven2Url = remoteApacheMaven2Url;
        return this;
    }

    public String getRemoteAliyunMaven2Url() {
        return remoteAliyunMaven2Url;
    }

    private DependencyEntity setRemoteAliyunMaven2Url(String remoteAliyunMaven2Url) {
        this.remoteAliyunMaven2Url = remoteAliyunMaven2Url;
        return this;
    }
}