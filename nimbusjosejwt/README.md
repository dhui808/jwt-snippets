### Exclude duplicate dependencies:
		<dependency>
			<groupId>com.google.crypto.tink</groupId>
			<artifactId>tink</artifactId>
			<version>1.3.0</version>
			<exclusions>
				<exclusion>
					<groupId>org.json</groupId>
					<artifactId>json</artifactId>
				</exclusion>
			</exclusions>
		</dependency>