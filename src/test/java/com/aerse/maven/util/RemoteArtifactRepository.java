package com.aerse.maven.util;

import org.apache.maven.plugin.testing.stubs.StubArtifactRepository;

public class RemoteArtifactRepository extends StubArtifactRepository {

	private String url;

	public RemoteArtifactRepository() {
		super(null);
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

}
