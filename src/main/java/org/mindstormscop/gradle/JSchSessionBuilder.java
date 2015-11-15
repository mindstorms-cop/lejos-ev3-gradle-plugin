package org.mindstormscop.gradle;

import com.google.common.base.Optional;
import com.jcraft.jsch.JSchException;

public class JSchSessionBuilder {

    private Optional<String> host = Optional.absent();
    private Optional<Integer> port = Optional.of(22);
    private Optional<String> user = Optional.of("root");
    private Optional<String> sshKey = Optional.absent();

    public JSchSessionBuilder()
    {
    }

    public JSchSessionBuilder setHost(String host) {
        this.host = Optional.of(host);
        return this;
    }

    public JSchSessionBuilder setPort(Integer port) {
        this.port = Optional.of(port);
        return this;
    }


    public JSchSessionBuilder setUser(String user) {
        this.user = Optional.of(user);
        return this;
    }

    public JSchSessionBuilder setSshKey(String sshKey) {
        this.sshKey = Optional.of(sshKey);
        return this;
    }

    public JSchSession build() throws JSchException {
        JSchSession jSchSession = new JSchSession(host.get(), port.get(), user.get());
        if (sshKey.isPresent()) {
            jSchSession.setSshKey(sshKey.get());
        }
        return jSchSession;
    }
}
