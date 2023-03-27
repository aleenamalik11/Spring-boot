package com.hazelsoft.custom_maven_goal;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Goal(custom)
 *
 */
@Mojo(name = "hello")
public class MyMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException {
		getLog().info("Hi there!!!");
	}
}
