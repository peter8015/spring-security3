package sample.contact;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StopWatch;

public class ClientApplication {
    private final ListableBeanFactory beanFactory;

    public ClientApplication(ListableBeanFactory beanFactory) {
	this.beanFactory = beanFactory;
    }

    public void invokeContactManager(Authentication authentication,
	    int nrOfCalls) {
	StopWatch stopWatch = new StopWatch(nrOfCalls
		+ " ContactManager call(s)");
	Map<String,ContactManager> contactServices = this.beanFactory.getBeansOfType(
		ContactManager.class, true, true);

	SecurityContextHolder.getContext().setAuthentication(authentication);

	for (String beanName : contactServices.keySet()) {
	    Object object = this.beanFactory.getBean("&" + beanName);
	    try {
		System.out
			.println("Trying to find setUsername(String) method on: "
				+ object.getClass().getName());

		Method method = object.getClass().getMethod("setUsername",
			new Class[] { String.class });
		System.out.println("Found; Trying to setUsername(String) to "
			+ authentication.getPrincipal());
		method.invoke(object,
			new Object[] { authentication.getPrincipal() });
	    } catch (NoSuchMethodException ignored) {
		System.out
			.println("This client proxy factory does not have a setUsername(String) method");
	    } catch (IllegalAccessException ignored) {
		ignored.printStackTrace();
	    } catch (InvocationTargetException ignored) {
		ignored.printStackTrace();
	    }
	    try {
		System.out
			.println("Trying to find setPassword(String) method on: "
				+ object.getClass().getName());

		Method method = object.getClass().getMethod("setPassword",
			new Class[] { String.class });
		method.invoke(object,
			new Object[] { authentication.getCredentials() });
		System.out.println("Found; Trying to setPassword(String) to "
			+ authentication.getCredentials());
	    } catch (NoSuchMethodException ignored) {
		System.out
			.println("This client proxy factory does not have a setPassword(String) method");
	    } catch (IllegalAccessException ignored) {
	    } catch (InvocationTargetException ignored) {
	    }
	    ContactManager remoteContactManager = (ContactManager) contactServices
		    .get(beanName);
	    System.out.println("Calling ContactManager '" + beanName + "'");

	    stopWatch.start(beanName);

	    List<Contact> contacts = null;

	    for (int i = 0; i < nrOfCalls; i++) {
		contacts = remoteContactManager.getAll();
	    }

	    stopWatch.stop();

	    if (contacts.size() != 0) {
		for (Contact contact : contacts)
		    System.out.println("Contact: " + contact);
	    } else {
		System.out
			.println("No contacts found which this user has permission to");
	    }

	    System.out.println();
	    System.out.println(stopWatch.prettyPrint());
	}

	SecurityContextHolder.clearContext();
    }

    public static void main(String[] args) {
	String username = System.getProperty("username", "");
	String password = System.getProperty("password", "");
	String nrOfCallsString = System.getProperty("nrOfCalls", "");

	if (("".equals(username)) || ("".equals(password))) {
	    System.out
		    .println("You need to specify the user ID to use, the password to use, and optionally a number of calls using the username, password, and nrOfCalls system properties respectively. eg for user rod, use: -Dusername=rod -Dpassword=koala' for a single call per service and use: -Dusername=rod -Dpassword=koala -DnrOfCalls=10 for ten calls per service.");

	    System.exit(-1);
	} else {
	    int nrOfCalls = 1;

	    if (!"".equals(nrOfCallsString)) {
		nrOfCalls = Integer.parseInt(nrOfCallsString);
	    }

	    ListableBeanFactory beanFactory = new FileSystemXmlApplicationContext(
		    "clientContext.xml");
	    ClientApplication client = new ClientApplication(beanFactory);

	    client.invokeContactManager(
		    new UsernamePasswordAuthenticationToken(username, password),
		    nrOfCalls);
	    System.exit(0);
	}
    }
}
