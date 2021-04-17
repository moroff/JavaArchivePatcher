package info.moroff.tools.archivepatcher;

import org.junit.jupiter.api.Test;

public class JavaArchivePatcherTest {
	String earFile = "target/testdata/module-ear-1.0-SNAPSHOT.ear";
	
	@Test
	void testUpdateEar() {
		JavaArchivePatcher earPatcher = new JavaArchivePatcher();
		
		earPatcher.setTempFolder("target/temp");
		earPatcher.list(earFile);
	}

}
