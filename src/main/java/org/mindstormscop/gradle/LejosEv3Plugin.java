package org.mindstormscop.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class LejosEv3Plugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().create("ev3", LejosEv3PluginExtension.class);

        Ev3DeployTask ev3Deploy = project.getTasks().create("ev3Deploy", Ev3DeployTask.class);
        ev3Deploy.setDescription("Deploys leJOS EV3 application to EV3 brick");
        ev3Deploy.setGroup("leJOS EV3");
        ev3Deploy.dependsOn(project.getTasksByName("shadowJar", false));
    }

}

