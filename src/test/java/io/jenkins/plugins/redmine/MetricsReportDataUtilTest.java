package io.jenkins.plugins.redmine;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class MetricsReportDataUtilTest {
	@Test
	public void testcopyFolder() {

		String CSV_PATH = "src/main/webapp/output/report/charts/";

		MetricsReportDataUtil dataUtil = new MetricsReportDataUtil();
		String src = "C:/Program Files (x86)/Jenkins/plugins/redmine-metrics-report/output";
		String dest = "C:/Program Files (x86)/Jenkins/workspace/stepCounter_test/test-project";
		File srcFolder = new File(src);
		File destFolder = new File(dest);
		try {
			dataUtil.copyFolder(srcFolder, destFolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
