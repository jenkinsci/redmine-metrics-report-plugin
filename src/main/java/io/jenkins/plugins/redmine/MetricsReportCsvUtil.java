package io.jenkins.plugins.redmine;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.taskadapter.redmineapi.bean.Issue;

public class MetricsReportCsvUtil {

	private static final String COMMA_DELIMITER = ",";

	private static final String NEW_LINE_SEPARATOR = "\r\n";

	public void delete(String path) {

		final File folder = new File(path);
		final File[] files = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return name.endsWith(".csv");
			}
		});
		for (final File file : files) {
			file.delete();
		}
	}

	public void write(Map<LocalDate, LocalDate> collectionDateMap, List<String> userList,
			Map<String, List<Issue>> ListByTrackers, MetricsReportSetting setting, String path) {

		List<LocalDate> collectionDateList = collectionDateMap.keySet().stream().collect(Collectors.toList());
		writeCsvFile(path + "date.csv", collectionDateList);
		writeCsvFile(path + "tracker.csv", ListByTrackers.keySet().stream().collect(Collectors.toList()));
		writeCsvFile(path + "user.csv", userList);

		// Create csv rows by tracker
		ListByTrackers.forEach((tracker, issues) -> {

			// Get list for increment csv
			List<Integer> incrementList = getIncrementList(collectionDateMap, issues);
			writeCsvFile(path + "increment.csv", incrementList);

			// Get list for assignee csv
			List<Integer> assigneeList = getAssigneeList(userList, issues);
			writeCsvFile(path + "assignee.csv", assigneeList);

			// Get list for status csv
			List<Integer> openStatusList = getOpenStatusList(collectionDateMap, issues);
			writeCsvFile(path + "status.csv", openStatusList);
			List<Integer> closeStatusList = getCloseStatusList(collectionDateMap, issues);
			writeCsvFile(path + "status.csv", closeStatusList);
		});
	}

	private List<Integer> getOpenStatusList(Map<LocalDate, LocalDate> collectionDateMap, List<Issue> issues) {

		Map<LocalDate, List<Issue>> openList = new LinkedHashMap<>();
		issues.forEach(issue -> {
			LocalDate createdOn = issue.getCreatedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			collectionDateMap.forEach((fromDate, toDate) -> {
				if ((fromDate.isBefore(createdOn) || fromDate.isEqual(createdOn)) && toDate.isAfter(createdOn)) {
					openList.computeIfAbsent(fromDate, i -> new ArrayList<>()).add(issue);
				} else {
					openList.computeIfAbsent(fromDate, i -> new ArrayList<>());
				}
			});
		});
		return openList.values().stream().map(v -> v.size()).collect(Collectors.toList());
	}

	private List<Integer> getCloseStatusList(Map<LocalDate, LocalDate> collectionDateMap, List<Issue> issues) {

		Map<LocalDate, List<Issue>> closeList = new LinkedHashMap<>();
		issues.forEach(issue -> {
			LocalDate closedOn = issue.getClosedOn() == null ? LocalDate.of(1900, 01, 01)
					: issue.getClosedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			collectionDateMap.forEach((fromDate, toDate) -> {
				if ((fromDate.isBefore(closedOn) || fromDate.isEqual(closedOn)) && toDate.isAfter(closedOn)) {
					closeList.computeIfAbsent(fromDate, i -> new ArrayList<>()).add(issue);
				} else {
					closeList.computeIfAbsent(fromDate, i -> new ArrayList<>());
				}
			});
		});
		return closeList.values().stream().map(v -> v.size()).collect(Collectors.toList());
	}

	private List<Integer> getAssigneeList(List<String> userList, List<Issue> issues) {

		Map<String, List<Issue>> assigneeList = new LinkedHashMap<>();

		issues.forEach(issue -> {
			userList.forEach(user -> {
				if (user.equals(issue.getAssigneeName())) {
					assigneeList.computeIfAbsent(user, i -> new ArrayList<>()).add(issue);
				} else {
					assigneeList.computeIfAbsent(user, i -> new ArrayList<>());
				}
			});
		});
		return assigneeList.values().stream().map(v -> v.size()).collect(Collectors.toList());
	}

	private List<Integer> getIncrementList(Map<LocalDate, LocalDate> collectionDateMap, List<Issue> issues) {

		Map<LocalDate, List<Issue>> updateList = new LinkedHashMap<>();
		issues.forEach(issue -> {
			LocalDate updatedOn = issue.getUpdatedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			collectionDateMap.forEach((fromDate, toDate) -> {
				if (issue.getClosedOn() == null && (fromDate.isBefore(updatedOn) || fromDate.isEqual(updatedOn))
						&& toDate.isAfter(updatedOn)) {
					updateList.computeIfAbsent(fromDate, i -> new ArrayList<>()).add(issue);
				} else {
					updateList.computeIfAbsent(fromDate, i -> new ArrayList<>());
				}
			});
		});
		List<Integer> incrementList = updateList.values().stream().map(v -> v.size()).collect(Collectors.toList());
		for (int i = 1; i < incrementList.size(); i++) {
			incrementList.set(i, incrementList.get(i - 1) + incrementList.get(i));
		}
		return incrementList;
	}

	private <T> void writeCsvFile(String fileName, List<T> list) {

		try (FileWriter fileWriter = new FileWriter(fileName, true)) {

			int cnt = 0;
			// Write list to the CSV file
			for (T elem : list) {
				fileWriter.append(String.valueOf(elem));
				if (++cnt != list.size()) {
					fileWriter.append(COMMA_DELIMITER);
				}
			}
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.flush();
			fileWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
