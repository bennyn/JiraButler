package de.angelcode.jirabutler.soap;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.xml.rpc.ServiceException;

import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.soap.beans.RemoteComment;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;

import de.angelcode.jirabutler.exceptions.JiraButlerException;
import de.angelcode.jirabutler.soap.service.JiraSoapService;
import de.angelcode.jirabutler.soap.service.JiraSoapServiceService;
import de.angelcode.jirabutler.soap.service.JiraSoapServiceServiceLocator;
import de.angelcode.jirabutler.util.SystemVariables;

/**
 * 
 * @author danielp, robert, jens
 */
public class JiraClient {

	private String jiraProjectVersion;
	private String jiraIssueKey;
	private String username;
	private String gitCommitMessage;
	private String connectionUrl;
	private String connectionUsername;
	private String connectionPassword;
	private String jiraProjectKey;
	private JiraSoapServiceService service;
	private JiraSoapService api;
	private String token;

	public JiraClient() {
		super();
		this.jiraProjectVersion = null;
		this.jiraIssueKey = null;
		this.username = null;
		this.gitCommitMessage = null;
		this.connectionUrl = null;
		this.connectionUsername = null;
		this.connectionPassword = null;
		this.service = null;
		this.api = null;
		this.token = null;
	}

	public JiraClient(String version, String key, String username,
			String message) {
		this();
		this.jiraProjectVersion = version;
		this.jiraIssueKey = key;
		this.username = username;
		this.gitCommitMessage = message;
	}

	public void setApi(JiraSoapService api) {
		this.api = api;
	}

	/**
	 * Reads the connection URL and JIRA authentication credentials (username
	 * and password) from the default "jira.properties" configuration file.
	 */
	public void loadConfigFile() throws JiraButlerException {
		Properties properties = new Properties();
		BufferedInputStream stream;
		String file = "jira.properties";
		try {
			stream = new BufferedInputStream(new FileInputStream(
					SystemVariables.getJarExecutionDirectory() + file));
			properties.load(stream);
			stream.close();
		} catch (FileNotFoundException ex) {
			throw new JiraButlerException("The file " + file + "could not be found");
		} catch (IOException ex) {
			throw new JiraButlerException("Error while reading/writing the file " + file);
		}
		this.connectionUrl = properties.getProperty("url");
		this.connectionUsername = properties.getProperty("username");
		this.connectionPassword = properties.getProperty("password");
		this.service = (JiraSoapServiceService) new JiraSoapServiceServiceLocator(
				this.connectionUrl);
		try {
			this.api = this.service.getJirasoapserviceV2();
		} catch (ServiceException ex) {
			throw new JiraButlerException("Cannot get the JIRA SOAP service");
		}
		if (this.jiraIssueKey != null) {
			this.jiraProjectKey = this.jiraIssueKey.substring(0, this.jiraIssueKey.indexOf("-"));
		}
	}

	public boolean login() throws JiraButlerException {
		if (this.connectionUsername == null || this.connectionPassword == null || this.connectionUrl == null) {
			throw new JiraButlerException("Invalid connection information");
		}
		try {
			this.token = api.login(this.connectionUsername,
					this.connectionPassword);
		} catch (RemoteException ex) {
			throw new JiraButlerException(
					"The authentication credentials are wrong");
		} catch (java.rmi.RemoteException e) {
			throw new JiraButlerException("No connection could be established");
		}
		return this.token == null ? false : true;
	}

	public boolean logout() throws JiraButlerException {
		try {
			return this.api.logout(this.token);
		} catch (java.rmi.RemoteException ex) {
			throw new JiraButlerException("Logout was not successfully");
		}
	}

	public boolean addVersion() throws JiraButlerException {
		if (this.jiraProjectKey == null || this.jiraProjectVersion == null) {
			throw new JiraButlerException("Null project key or version");
		}
		RemoteVersion newVersion = new RemoteVersion();
		newVersion.setName(this.jiraProjectVersion);
		try {
			newVersion = this.api.addVersion(this.token, this.jiraProjectKey,
					newVersion);
		} catch (RemoteException ex) {
			throw new JiraButlerException("Version already exists or no permission to set the version");
		} catch (java.rmi.RemoteException e) {
			throw new JiraButlerException("No connection could be established");
		} catch (NoClassDefFoundError ex) {
			System.out.println("Version added successfully.");
		}
		return newVersion == null ? false : true;
	}

	public boolean addComment() throws JiraButlerException {
		boolean success = false;
		if (this.jiraIssueKey == null || this.gitCommitMessage == null || this.username == null) {
			throw new JiraButlerException("Invalid information");
		}
		try {
			RemoteComment comment = new RemoteComment();
			comment.setBody(this.username + ": " + this.gitCommitMessage);
			System.out.println("This comment is going to be written into the JIRA issue: " 
					+ this.username + ": " + this.gitCommitMessage);
			this.api.addComment(this.token, this.jiraIssueKey, comment);
			success = true;
		} catch (RemoteException ex) {
			throw new JiraButlerException("Comment could not be written. Please check your authentication credentials and user permissions");
		} catch (java.rmi.RemoteException e) {
			throw new JiraButlerException("No connection could be established");
		}
		return success;
	}

	public void setGitCommitMessage(String gitCommitMessage) {
		this.gitCommitMessage = gitCommitMessage;
	}

	public void setJiraProjectKey(String jiraProjectKey) {
		this.jiraProjectKey = jiraProjectKey;
	}

	public void setJiraIssueKey(String jiraIssueKey) {
		this.jiraIssueKey = jiraIssueKey;
	}

	public void setJiraProjectVersion(String jiraProjectVersion) {
		this.jiraProjectVersion = jiraProjectVersion;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setConnectionUsername(String connectionUsername) {
		this.connectionUsername = connectionUsername;
	}

	public void setConnectionPassword(String connectionPassword) {
		this.connectionPassword = connectionPassword;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public void setService(JiraSoapServiceService service) {
		this.service = service;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
