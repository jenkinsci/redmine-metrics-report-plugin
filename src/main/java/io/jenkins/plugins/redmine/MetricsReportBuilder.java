package io.jenkins.plugins.redmine;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import com.taskadapter.redmineapi.bean.Issue;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;

public class MetricsReportBuilder extends Builder implements SimpleBuildStep {

	private static final String COPY_PATH = "/plugins/redmine-metrics-report/output";

	private static final String CSV_PATH = Jenkins.getInstance().getRootDir()
			+ "/plugins/redmine-metrics-report/output/report/charts/";

	private List<MetricsReportSetting> settings;

	@DataBoundConstructor
	public MetricsReportBuilder(List<MetricsReportSetting> settings) {
		this.settings = settings;
	}

	@Extension
	@Symbol("redmineMetricsReport")
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return Messages.metrics_report_displayName();
		}
	}

	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener)
			throws InterruptedException, IOException {

		MetricsReportDataUtil dataUtil = new MetricsReportDataUtil();
		MetricsReportCsvUtil csvUtil = new MetricsReportCsvUtil();

		for (MetricsReportSetting setting : settings) {

			// get issue by query id
			List<Issue> issues = dataUtil.getIssueList(setting);
			listener.getLogger().println("[Redmine Metrics Report]-INFO-> Project Name: " + setting.getProjectName());

			if (issues == null) {
				listener.getLogger().println("[Redmine Metrics Report]-ERROR-> Can't generate report for project ["
						+ setting.getProjectName() + "], please verify plugin configuration.");
				issues = new ArrayList<>();
			} else {
				listener.getLogger().println("[Redmine Metrics Report]-INFO-> Total Issues Count: " + issues.size());
			}

			// get collection date list
			Map<LocalDate, LocalDate> collectionDateMap = dataUtil.getcollectionDateMap(issues,
					setting.getSprintSize());

			// Get user list
			List<String> userList = dataUtil.getUserList(issues);

			// categorize by tracker
			Map<String, List<Issue>> ListByTrackers = dataUtil.categorizeList(issues);

			// delete csv
			csvUtil.delete(CSV_PATH);
			// output csv
			csvUtil.write(collectionDateMap, userList, ListByTrackers, setting, CSV_PATH);
			// copy report folder to workspace
			copyReportToWorkspace(workspace, launcher, setting, listener);
		}
	}

	private void copyReportToWorkspace(FilePath workspace, Launcher launcher, MetricsReportSetting setting,
			TaskListener listener) throws IOException, InterruptedException {
		// get report folder
		FilePath srcFolder = new FilePath(new File(Jenkins.getInstance().getRootDir() + COPY_PATH));
		
		// get current build project workspace folder
		FilePath projectWorkspace = new FilePath(launcher.getChannel(), workspace.getRemote() + "/redmineReports/" + setting.getProjectName());
		projectWorkspace.deleteContents();
		projectWorkspace.mkdirs();
		FilePath destFolder = projectWorkspace.absolutize();

		listener.getLogger().println("[Redmine Metrics Report]-INFO-> Copy From: " + srcFolder.getRemote());
		listener.getLogger().println("[Redmine Metrics Report]-INFO-> Copy To: " + destFolder.getRemote());
		
		// copy the contents of srcFolder directory recursively into the destFolder directory.
		srcFolder.copyRecursiveTo(destFolder);
	}
	
	public List<MetricsReportSetting> getSettings() {
		return settings;
	}
	
	@DataBoundSetter
	public void setSettings(List<MetricsReportSetting> settings) {
		this.settings = settings;
	}
}
