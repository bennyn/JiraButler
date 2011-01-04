package de.angelcode.jirabutler.soap;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.angelcode.jirabutler.exceptions.VoidParameterException;
import de.angelcode.jirabutler.soap.service.JiraSoapService;

public class JiraClientImplTest
{

  private JiraClient jiraClient;
  private JiraSoapService apiMock;

  @Before
  public void setUp() throws Exception
  {
//    jiraClient = new JiraClient("http://angelcode.de:8080/rpc/soap/jirasoapservice-v2");
//    apiMock = createStrictMock(JiraSoapService.class);
//    jiraClient.setApi(apiMock);
    jiraClient = new JiraClient();
  }

  @Test
  public final void testValidLogin() throws Exception
  {
    String token = new String();
    String user = "testUser";
    String password = "testPassword";

    jiraClient.loadConfigFile();

//    expect(apiMock.login(user, password)).andReturn(token).times(1);
//    replay(apiMock);
//    assertTrue(jiraClient.login(user, password));
    assertTrue(jiraClient.login());
//    verify(apiMock);
  }

  @Test
  public final void testInvalidLogin() throws Exception
  {
    String user = "testUser";
    String password = "testPassword";

//    expect(apiMock.login(user, password)).andReturn(null).times(1);
//    replay(apiMock);
//    assertFalse(jiraClient.login(user, password));
//    verify(apiMock);
  }

  @Test(expected = VoidParameterException.class)
  public final void testVoidLogin() throws Exception
  {
    String user = "";
    String password = "";
//    boolean login = jiraClient.login(user, password);
  }
}
