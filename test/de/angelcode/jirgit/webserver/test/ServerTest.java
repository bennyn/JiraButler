package de.angelcode.jirgit.webserver.test;

import de.angelcode.jirabutler.webserver.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the web server.
 * @author bennyn
 */
public class ServerTest
{

  private static Server server;

  public ServerTest()
  {
    super();
  }

  @Before
  public void setUp()
  {
    //server = new Server("7777", "testlog.txt");
  }

  @After
  public void tearDown()
  {
    server = null;
  }

  @Test
  public void testPortCastException1()
  {
    server = new Server("abc", "/var/log/messages");
    /*
    assertEquals("Test if the server works.",
    true, server.doSomething);
     * 
     */
  }

  @Test
  public void testPortCastException2()
  {
    server = new Server(null, "/var/log/messages");
  }

  @Test
  public void testMinPortRangeException()
  {
    server = new Server("-1", "/var/log/messages");
  }

  @Test
  public void testMaxPortRangeException()
  {
    server = new Server("65536", "/var/log/messages");
  }
}