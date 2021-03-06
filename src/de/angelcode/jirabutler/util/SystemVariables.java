package de.angelcode.jirabutler.util;

/**
 * This class can deliver some system-relevant information like the execution
 * path of the application's JAR file.
 * 
 * @author bennyn
 */
public class SystemVariables
{
  private static String javaClassPath = "java.class.path";
  private static String fileSeperator = "file.separator";

  public static void setFileSeperator(String fileSeperator)
  {
    SystemVariables.fileSeperator = fileSeperator;
  }

  public static void setJavaClassPath(String javaClassPath)
  {
    SystemVariables.javaClassPath = javaClassPath;
  }

	/**
	 * Returns the path where the currently running JAR-file is located. Example
	 * value: C:\MyProject\build\jar\
	 * 
	 * @return Path of the JAR-file
	 */
	public static String getJarExecutionDirectory() {
		String jarFile = null;
		String jarDirectory = null;
		int cutFileSeperator = 0;
		int cutSemicolon = -1;

    jarFile = System.getProperty(SystemVariables.javaClassPath);
    // Cut seperators
    cutFileSeperator = jarFile.lastIndexOf(System.getProperty(SystemVariables.fileSeperator));
    jarDirectory = jarFile.substring(0, cutFileSeperator);
    // Cut semicolons
    cutSemicolon = jarDirectory.lastIndexOf(';');
    jarDirectory = jarDirectory.substring(cutSemicolon+1, jarDirectory.length());

//    String test = jarDirectory+System.getProperty(SystemVariables.fileSeperator);
//
//    System.out.println("Daniel testet: " + test);

    return jarDirectory+System.getProperty(SystemVariables.fileSeperator);
  }

}
