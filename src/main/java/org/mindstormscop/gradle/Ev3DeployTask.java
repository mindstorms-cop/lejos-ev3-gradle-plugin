package org.mindstormscop.gradle;

import com.google.common.collect.Iterables;
import com.jcraft.jsch.JSchException;
import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.PublishArtifact;
import org.gradle.api.artifacts.PublishArtifactSet;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;

public class Ev3DeployTask extends DefaultTask {

    public static final String SSH_KEY = System.getProperty("user.home") + "/.ssh/id_rsa";
    public static final String LEJOS_PROGRAM_DIRECTORY = "/home/lejos/programs/";

    @TaskAction
    public void ev3Deploy() {
        Project project = getProject();
        Logger logger = getLogger();
        LejosEv3PluginExtension ev3 = getEv3PluginExtension();

        Configuration shadowConfiguration = project.getConfigurations().getByName("shadow");
        if (shadowConfiguration == null) {
            throw new GradleException("Configuration shadow not found -> is the Gradle shadow plugin included?");
        }
        PublishArtifactSet shadowArtifacts = shadowConfiguration.getAllArtifacts();
        if (shadowArtifacts.isEmpty()) {
            throw new GradleException("Configuration shadow has no artifacts");
        }
        PublishArtifact shadowArtifact = Iterables.get(shadowArtifacts, 0);
        String shadowArtifactPath = shadowArtifact.getFile().getAbsolutePath();

        try {
            JSchSession jSchSession = new JSchSessionBuilder()
                    .setHost(getEv3Host())
                    .setPort(ev3.getPort())
                    .setUser(ev3.getUser())
                    .setSshKey(SSH_KEY)
                    .build();
            logger.debug("Copy " + shadowArtifactPath + " to root@" + ev3.getHost() + ":/home/lejos/programs/");
            jSchSession.copyFile(shadowArtifactPath, LEJOS_PROGRAM_DIRECTORY);
        } catch (IOException | JSchException | RuntimeException e) {
            throw new GradleException("Error on copying file by scp", e);
        }

    }

    private String getEv3Host() {
        Logger logger = getLogger();
        LejosEv3PluginExtension ev3 = getEv3PluginExtension();
        String host;
        if (ev3.discoverBrickEnabled()) {
            BrickInfo brick;
            try {
                brick =  BrickFinder.discover()[0];
            } catch (IOException e) {
                throw new GradleException("Error on discovering bricks", e);
            }
            logger.info("Found brick " + brick.getName() + " at " + brick.getIPAddress());
            host = brick.getIPAddress();
        } else {
            if (ev3.getHost() == null) {
                throw new GradleException("Please set property ev3.host");
            }
            host = ev3.getHost();
        }
        return host;
    }

    private LejosEv3PluginExtension getEv3PluginExtension() {
        return getProject().getExtensions().getByType(LejosEv3PluginExtension.class);
    }

}
