package com.sequenceiq.ambari.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.shell.commands.ExitCommands;
import org.springframework.shell.commands.HelpCommands;
import org.springframework.shell.commands.VersionCommands;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.ExitShellRequest;
import org.springframework.shell.core.JLineShellComponent;

@Configuration
@ComponentScan(basePackageClasses={AmbariShell.class})
public class AmbariShell implements CommandLineRunner 
{
	
	@Bean
	public CommandMarker exitCommand() {
		return new ExitCommands();
	}

	@Bean
	public CommandMarker versionCommands() {
		return new VersionCommands();
	}

	@Bean
	public CommandMarker helpCommands() {
		return new HelpCommands();
	}

	
	@Autowired
	GenericApplicationContext ctx;

	public void run(String... arg0) throws Exception {		
		JLineShellComponent shell = ctx.getBean("shell", JLineShellComponent.class);
		ExitShellRequest exitShellRequest;

		shell.start();
		shell.promptLoop();
		exitShellRequest = shell.getExitShellRequest();
		if (exitShellRequest == null) {
			// shouldn't really happen, but we'll fallback to this anyway
			exitShellRequest = ExitShellRequest.NORMAL_EXIT;
		}
		shell.waitForComplete();

		ctx.close();
	}
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(AmbariShell.class)
		.showBanner(false)
		.run(args);
	}
}
