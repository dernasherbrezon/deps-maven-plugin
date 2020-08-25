package com.aerse.maven.util;

import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;

public class ResolvedArtifact extends ArtifactStub {

	private ArtifactHandler artifactHandler;

	@Override
	public void setArtifactHandler(ArtifactHandler artifactHandler) {
		this.artifactHandler = artifactHandler;
	}

	@Override
	public ArtifactHandler getArtifactHandler() {
		return artifactHandler;
	}

	@Override
	public String getId() {
		return getGroupId() + ":" + getArtifactId() + ":" + getVersion();
	}
}
