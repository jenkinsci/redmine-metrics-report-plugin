package io.jenkins.plugins.redmine;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.Secret;

public class MetricsReportSetting extends AbstractDescribableImpl<MetricsReportSetting> {

	private String url;
	private Secret apiKey;
	private String projectName;
	private int customQueryId;
	private int sprintSize;

	@DataBoundConstructor
	public MetricsReportSetting(String url, String apiKey, String projectName, int customQueryId, int sprintSize) {
		this.url = url;
		this.apiKey = Secret.fromString(apiKey);
		this.projectName = projectName;
		this.customQueryId = customQueryId;
		this.sprintSize = sprintSize;
	}

	public String getUrl() {
		return url;
	}

	public Secret getApiKey() {
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

	@Extension
	@Symbol("redmineMetricsSetting")
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
		public FormValidation doCheckUrl(@QueryParameter String value) {
			if (value == null || "".equals(value)) {
				return FormValidation.error(Messages.error_require_redmineUrl());
			}
			return FormValidation.ok();
		}

		public FormValidation doCheckProjectName(@QueryParameter String value) {
			if (value == null || "".equals(value)) {
				return FormValidation.error(Messages.error_require_projectName());
			}
			return FormValidation.ok();
		}

		public FormValidation doCheckApiKey(@QueryParameter String value) {
			if (value == null || "".equals(value)) {
				return FormValidation.error(Messages.error_require_apiKey());
			}
			return FormValidation.ok();
		}

		public FormValidation doCheckCustomQueryId(@QueryParameter String value) {
			try {
				if (value == null || "".equals(value)) {
					return FormValidation.error(Messages.error_require_queryId());
				}
				Integer.parseInt(value);
				return FormValidation.ok();
			} catch (NumberFormatException e) {
				return FormValidation.error(Messages.error_invalid_queryId());
			}
		}

		public FormValidation doCheckSprintSize(@QueryParameter String value) {
			try {
				if (value == null || "".equals(value)) {
					return FormValidation.error(Messages.error_require_sprintSize());
				}
				Integer.parseInt(value);
				return FormValidation.ok();
			} catch (NumberFormatException e) {
				return FormValidation.error(Messages.error_invalid_sprintSize());
			}
		}
	}
}
