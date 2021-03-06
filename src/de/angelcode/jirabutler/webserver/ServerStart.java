package de.angelcode.jirabutler.webserver;

import de.angelcode.jirabutler.exceptions.JiraButlerException;
import de.angelcode.jirabutler.util.FileOperations;
import de.angelcode.jirabutler.util.SystemVariables;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

/**
 * Handles the parameters given by the user.
 * @author bennyn
 */
public class ServerStart
{

  private String[] arguments;

  public ServerStart(String[] argv)
  {
    this.arguments = argv;
  }

  public void handleParameters() throws JiraButlerException
  {

    // Control variables
    int option = -1;
    String command = null;


    // Program information
    String programName = "JiraButler";
    String programVersion = "v0.1";

    // Presets
    String serverPort = "7070";
    StringBuffer logFilePath = new StringBuffer(SystemVariables.getJarExecutionDirectory()
            + "jira-butler.log");

    // Long option names
    LongOpt[] optionsLong = new LongOpt[4];
    optionsLong[0] = new LongOpt("port", LongOpt.OPTIONAL_ARGUMENT, null, 'p');
    optionsLong[1] = new LongOpt("log", LongOpt.OPTIONAL_ARGUMENT, logFilePath, 'l');
    optionsLong[2] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
    optionsLong[3] = new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'v');

    // GetOpt
    Getopt g = new Getopt(new String(), this.arguments, "-:p:l:hv", optionsLong);
    g.setOpterr(false);

    while ((option = g.getopt()) != -1)
    {
      switch (option)
      {
        case 1:
          command = g.getOptarg();
          if (command.equals("start"))
          {
            System.out.println("Starting server...");
            Server server = new Server(serverPort, logFilePath.toString());
            server.startServer();
          }
          else
          {
            System.out.println("Wrong parameters. Please read the manual.");
          }
          break;
        case 'h':
          String readmePath = SystemVariables.getJarExecutionDirectory() + "readme.txt";
          String readmeContent = FileOperations.getFileContent(readmePath);

          if (readmeContent != null)
          {
            System.out.println(readmeContent);
          }
          else
          {
            System.out.println("Readme file cannot be found or is not accessible.");
          }
          break;
        case 'l':
          logFilePath = new StringBuffer(g.getOptarg());
          break;
        case 'p':
          serverPort = new StringBuffer(g.getOptarg()).toString();
          break;
        case 'v':
          System.out.println(programName + " " + programVersion);
          break;
        case ':':
        case '?':
        default:
          System.out.println("Wrong parameters.");
          System.out.println("Please read the manual.");
          break;
      }
    }
  }
}
