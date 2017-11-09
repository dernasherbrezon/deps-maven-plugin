package com.aerse.maven.deps;

import java.io.File;
import java.io.FileReader;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import com.google.common.io.CharStreams;

public class DependenciesMojoTest extends AbstractMojoTestCase {

	public void testSuccess() throws Exception {
		File pom = getTestFile("./src/test/resources/success/pom.xml");
		DependenciesMojo mojo = (DependenciesMojo) lookupMojo("extract", pom);
		assertNotNull(mojo);
		mojo.execute();

		try (FileReader r = new FileReader("./target/success/deps.txt")) {
			assertEquals("/junit/junit/4.10/junit-4.10.jar\n", CharStreams.toString(r));
		}
		try (FileReader r = new FileReader("./target/success/repo.txt")) {
			assertEquals("https://jcenter.bintray.com\n", CharStreams.toString(r));
		}
		assertTrue(new File("./target/success/script.sh").exists());
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
