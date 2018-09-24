package io.jenkins.plugins.redmine;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;

public class MetricsReportSetting extends AbstractDescribableImpl<MetricsReportSetting> {

	private String url;
	private String apiKey;
	private String projectName;
	private int customQueryId;
	private int sprintSize;

	@DataBoundConstructor
	public MetricsReportSetting(String url, String apiKey, String projectName, int customQueryId, int sprintSize) {
		this.url = url;
		this.apiKey = apiKey;
		this.projectName = projectName;
		this.customQueryId = customQueryId;
		this.sprintSize = sprintSize;
	}

	public String getUrl() {
		return url;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getProjectName() {
		return projectName;
	}

	public int getCustomQueryId() {
		return customQueryId;
	}
	
	public int getSprintSize() {
		return sprintSize;
	}
	
	@Extension @Symbol("redminMetricsSetting")
	public static class DescriptorImpl extends Descriptor<MetricsReportSetting> {
		@Override
		public String getDisplayName() {
			return "";
		}

		/**
		 * Performs on-the-fly validation of the form field 'name'.
		 *
		 * @param value
		 *            This parameter receives the value that the user has typed.
		 * @return Indicates the outcome of the validation. This is sent to the browser.
		 */
		public FormValidation doCheckFilePattern(@QueryParameter String value) {
			return FormValidation.ok();
		}

		public FormValidation doCheckEncoding(@QueryParameter String value) {
			return FormValidation.ok();
		}

		public FormValidation doCheckKey(@QueryParameter String key) {
			if (key == null || "".equals(key)) {
				return FormValidation.error("");
				// return FormValidation.error(Messages.errorCategoryRequired());
			}
			return FormValidation.ok();
		}
	}
}
