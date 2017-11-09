package com.aerse.maven.deps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;

@Mojo(name = "extract", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = false, requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class DependenciesMojo extends AbstractMojo {

	private final static Pattern DOT = Pattern.compile("\\.");

	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	@Parameter(defaultValue = "${project.build.directory}/repositories.txt", readonly = true)
	private String repositories;

	@Parameter(defaultValue = "${project.build.directory}/dependencies.txt", readonly = true)
	private String dependencies;

	@Parameter(defaultValue = "${project.build.directory}/download-dependencies.sh", readonly = true)
	private String script;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		File repositoriesFile = new File(repositories);
		setupParentDirectory(repositoriesFile);
		try (BufferedWriter w = new BufferedWriter(new FileWriter(repositoriesFile))) {
			for (ArtifactRepository cur : project.getRemoteArtifactRepositories()) {
				String url = cur.getUrl();
				if (url.charAt(url.length() - 1) == '/') {
					url = url.substring(0, url.length() - 1);
				}
				w.append(url).append('\n');
			}
		} catch (Exception e) {
			throw new MojoExecutionException("unable to write repositories to: " + repositoriesFile.getAbsolutePath(), e);
		}
		File dependensiesFile = new File(dependencies);
		setupParentDirectory(dependensiesFile);
		try (BufferedWriter w = new BufferedWriter(new FileWriter(dependensiesFile))) {
			for (Artifact cur : project.getArtifacts()) {
				w.append('/');
				w.append(DOT.matcher(cur.getGroupId()).replaceAll("/")).append('/');
				w.append(cur.getArtifactId()).append('/');
				w.append(cur.getVersion()).append('/');
				w.append(cur.getArtifactId()).append('-').append(cur.getVersion()).append('.').append(cur.getArtifactHandler().getExtension()).append('\n');
			}
		} catch (Exception e) {
			throw new MojoExecutionException("unable to write dependencies to: " + dependensiesFile.getAbsolutePath(), e);
		}
		File scriptFile = new File(script);
		setupParentDirectory(scriptFile);
		try (OutputStream w = new FileOutputStream(scriptFile); InputStream is = DependenciesMojo.class.getClassLoader().getResourceAsStream("download-dependencies.sh")) {
			IOUtil.copy(is, w);
		} catch (Exception e) {
			throw new MojoExecutionException("unable to write script to: " + scriptFile.getAbsolutePath(), e);
		}
	}

	private static void setupParentDirectory(File file) {
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			throw new RuntimeException("unable to setup parent directory: " + file.getAbsolutePath());
		}
	}

}
