package com.aerse.maven.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;
import org.apache.maven.plugin.testing.stubs.DefaultArtifactHandlerStub;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;

public class ProjectStub extends MavenProjectStub {

	private Set<Artifact> resolved;
	private List<ArtifactRepository> repositories;

	public void setArtifactStubs(Set<ArtifactStub> artifacts) {
		resolved = new HashSet<Artifact>();
		for (ArtifactStub cur : artifacts) {
			ResolvedArtifact curResolved = new ResolvedArtifact();
			curResolved.setArtifactHandler(new DefaultArtifactHandlerStub("jar"));
			curResolved.setArtifactId(cur.getArtifactId());
			curResolved.setGroupId(cur.getGroupId());
			curResolved.setVersion(cur.getVersion());
			resolved.add(curResolved);
		}
	}

	public void setStubArtifactRepository(List<RemoteArtifactRepository> list) {
		this.repositories = new ArrayList<ArtifactRepository>();
		for (RemoteArtifactRepository cur : list) {
			this.repositories.add(cur);
		}
	}

	@Override
	public List<ArtifactRepository> getRemoteArtifactRepositories() {
		return repositories;
	}

	@Override
	public Set<Artifact> getArtifacts() {
		return resolved;
	}

	public void setScopeArtifactFilter(String scope) {
		super.setArtifactFilter(new ScopeArtifactFilter(scope));
	}

}
