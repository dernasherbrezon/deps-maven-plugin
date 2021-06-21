# deps-maven-plugin [![Build Status](https://travis-ci.com/dernasherbrezon/deps-maven-plugin.svg?branch=master)](https://travis-ci.com/dernasherbrezon/deps-maven-plugin) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.aerse.maven%3Adeps-maven-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.aerse.maven%3Adeps-maven-plugin)

Maven plugin that outputs dependency information suitable for bash script to download. This is reasonable tradeoff between creating fat jar and installing full jdk, maven with ton of dependencies.

# configuration

<table>
	<thead>
		<tr>
			<th>
				Parameter
			</th>
			<th>
				Default value
			</th>
			<th>
				Description
			</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>repositories</td>
			<td>${project.build.directory}/repositories.txt</td>
			<td>file with repositories used by project. Every repository on separate line.</td>
		</tr>
		<tr>
			<td>dependencies</td>
			<td>${project.build.directory}/dependencies.txt</td>
			<td>file with dependencies and transitive dependencies. Every dependency on separate line. Format suitable for download. I.e. ```/com/example/artifact/1.0/artifact-1.0.jar```</td>
		</tr>
		<tr>
			<td>script</td>
			<td>${project.build.directory}/download-dependencies.sh</td>
			<td>script that could download dependencies from repositories. no maven required.</td>
		</tr>
		<tr>
			<td>excludes</td>
			<td></td>
			<td>List of artifacts to exclude. This is very useful to exclude artifacts from the private repositories. The pattern follows <a href="https://maven.apache.org/plugins/maven-assembly-plugin/advanced-descriptor-topics.html">maven assembly plugin</a></td>
		</tr>
	</tbody>
</table>

# sample configuraiton

```xml
<plugin>
	<groupId>com.aerse.maven</groupId>
	<artifactId>deps-maven-plugin</artifactId>
	<configuration>
		<repositories>${project.build.directory}/deps/repositories.txt</repositories>
		<dependencies>${project.build.directory}/deps/dependencies.txt</dependencies>
		<script>${project.build.directory}/deps/script.sh</script>
		<excludes>
			<exclude>com.examples:*:*<exclude>
		</excludes>
	</configuration>
</plugin
```

The directory ```${project.build.directory}/deps``` will contain everything required for .jar download. For example this directory could be used by assembly plugin to generate result binary.

After unpacking on target machine, execute script: ```./script.sh . /usr/share/java/applicaiton/```. Script supports the following arguments:

- Directory with repositories.txt and dependencies.txt files
- Directory where to output downloaded dependencies
