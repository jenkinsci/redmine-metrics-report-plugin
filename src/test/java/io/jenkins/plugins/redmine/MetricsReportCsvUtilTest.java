package io.jenkins.plugins.redmine;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.taskadapter.redmineapi.bean.Issue;

public class MetricsReportCsvUtilTest {
	@Test
	public void testWrite() {
//		// prepare the redmine server to conduct this test.
//		String CSV_PATH = "src/main/webapp/output/report/charts/";
//		
//		MetricsReportSetting setting = new MetricsReportSetting("http://localhost:3000",
//				"04fd42a2be7a8ea36159f4c29f3d291bd565a90b", "test-project", 1, 1);
//
//		MetricsReportDataUtil dataUtil = new MetricsReportDataUtil();
//		MetricsReportCsvUtil csvUtil = new MetricsReportCsvUtil();
//
//		// get issue by query id
//		List<Issue> issues = dataUtil.getIssueList(setting);
//		//assertEquals(9, issues.size());
//
//		// get collection date list
//		Map<LocalDate, LocalDate> collectionDateMap = dataUtil.getcollectionDateMap(issues, setting.getSprintSize());
//		
//		// get user list
//		List<String> userList = dataUtil.getUserList(issues);
//		
//		// categorize by tracker
//		Map<String, List<Issue>> ListByTrackers = dataUtil.categorizeList(issues);
//		
//		// conduct test
//		csvUtil.delete(CSV_PATH);
//		csvUtil.write(collectionDateMap, userList, ListByTrackers, setting, CSV_PATH);
	}

}
