package org.mindstormscop.gradle;

class LejosEv3PluginExtension {

    private String host;
    private Integer port;
    private String user;
    private Boolean discoverBrick;

    public LejosEv3PluginExtension() {
        port = 22;
        user = "root";
        discoverBrick = false;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Boolean discoverBrickEnabled() {
        return discoverBrick;
    }

    public void setDiscoverBrick(Boolean discoverBrick) {
        this.discoverBrick = discoverBrick;
    }
}
