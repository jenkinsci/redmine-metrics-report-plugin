package io.jenkins.plugins.redmine;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;

/**
 * @author shepherd lee
 *
 */
public class MetricsReportDataUtil {

	public Map<LocalDate, LocalDate> getcollectionDateMap(List<Issue> issues, int sprintSize) {

		Map<LocalDate, LocalDate> collectionDateMap = new LinkedHashMap();

		LocalDate minUpdatedDate = LocalDate.of(2900, 01, 01);
		LocalDate maxUpdatedDate = LocalDate.of(1900, 01, 01);
		LocalDate minCreatedDate = LocalDate.of(2900, 01, 01);
		LocalDate maxCreatedDate = LocalDate.of(1900, 01, 01);
		
		for (Issue i : issues) {
			if (minUpdatedDate.isAfter(i.getUpdatedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
				minUpdatedDate = i.getUpdatedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}
			if (maxUpdatedDate.isBefore(i.getUpdatedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
				maxUpdatedDate = i.getUpdatedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}
			if (minCreatedDate.isAfter(i.getCreatedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
				minCreatedDate = i.getCreatedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}
			if (maxCreatedDate.isBefore(i.getCreatedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
				maxCreatedDate = i.getCreatedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}
		}

		long range = ChronoUnit.DAYS.between(minCreatedDate, maxUpdatedDate);
		long sprintRange = (range + 1) / sprintSize;
		
		for (long l = 0; l < sprintRange + 1; l++) {
			LocalDate rangeEndDate = minCreatedDate.plusDays(sprintSize);
			collectionDateMap.put(minCreatedDate, rangeEndDate);
			minCreatedDate = rangeEndDate;
		}
		return collectionDateMap;
	}

	public List<String> getUserList(List<Issue> issues) {
		List<String> userList = new ArrayList<>();
		issues.forEach(issue -> {
			if (issue.getAssigneeName() != null && !userList.contains(issue.getAssigneeName())) {
				userList.add(issue.getAssigneeName());
			}
		});
		Collections.sort(userList);
		return userList;
	}

	public Map<String, List<Issue>> categorizeList(List<Issue> issues) {

		Map<String, List<Issue>> ListByTrackers = new HashMap<>();

		issues.forEach(i -> {
			ListByTrackers.computeIfAbsent(i.getTracker().getName(), v -> new ArrayList<>()).add(i);
		});

		return ListByTrackers;
	}

	public List<Issue> getIssueList(MetricsReportSetting setting) {

		RedmineManager mgr = RedmineManagerFactory.createWithApiKey(setting.getUrl(), setting.getApiKey().getPlainText());
		List<Issue> issues = null;

		try {
			issues = mgr.getIssueManager().getIssues(setting.getProjectName(), setting.getCustomQueryId());
		} catch (RedmineException e) {
			e.printStackTrace();
		}

		return issues;
	}
}
