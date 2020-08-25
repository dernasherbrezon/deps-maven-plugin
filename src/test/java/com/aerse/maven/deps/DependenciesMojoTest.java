package com.aerse.maven.deps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import com.google.common.io.CharStreams;

public class DependenciesMojoTest extends AbstractMojoTestCase {

	public void testSuccess() throws Exception {
		assertSuccess("success");
	}

	public void testEmptyExcludes() throws Exception {
		assertSuccess("emptyExcludes");
	}

	private void assertSuccess(String project) throws Exception, MojoExecutionException, MojoFailureException, IOException, FileNotFoundException {
		File pom = getTestFile("./src/test/resources/" + project + "/pom.xml");
		DependenciesMojo mojo = (DependenciesMojo) lookupMojo("extract", pom);
		assertNotNull(mojo);
		mojo.execute();

		try (FileReader r = new FileReader("./target/" + project + "/deps.txt")) {
			assertEquals("/junit/junit/4.10/junit-4.10.jar\n", CharStreams.toString(r));
		}
		try (FileReader r = new FileReader("./target/" + project + "/repo.txt")) {
			assertEquals("https://jcenter.bintray.com\n", CharStreams.toString(r));
		}
		assertTrue(new File("./target/" + project + "/script.sh").exists());
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		new File("./target/success").delete();
		super.tearDown();
	}
}
