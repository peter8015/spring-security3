package sample.contact;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    private static final Permission[] HAS_DELETE = { BasePermission.DELETE,
	    BasePermission.ADMINISTRATION };
    private static final Permission[] HAS_ADMIN = { BasePermission.ADMINISTRATION };

    @Autowired
    private ContactManager contactManager;

    @Autowired
    private PermissionEvaluator permissionEvaluator;

    @RequestMapping(value = { "/hello.htm" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    public ModelAndView displayPublicIndex() {
	Contact rnd = this.contactManager.getRandomContact();

	return new ModelAndView("hello", "contact", rnd);
    }

    @RequestMapping(value = { "/secure/index.htm" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
    public ModelAndView displayUserContacts() {
	List<Contact> myContactsList = this.contactManager.getAll();
	Map hasDelete = new HashMap(myContactsList.size());
	Map hasAdmin = new HashMap(myContactsList.size());

	Authentication user = SecurityContextHolder.getContext()
		.getAuthentication();

	for (Contact contact : myContactsList) {
	    hasDelete.put(contact, Boolean.valueOf(this.permissionEvaluator
		    .hasPermission(user, contact, HAS_DELETE)));
	    hasAdmin.put(contact, Boolean.valueOf(this.permissionEvaluator
		    .hasPermission(user, contact, HAS_ADMIN)));
	}

	Map model = new HashMap();
	model.put("contacts", myContactsList);
	model.put("hasDeletePermission", hasDelete);
	model.put("hasAdminPermission", hasAdmin);

	return new ModelAndView("index", "model", model);
    }
}
