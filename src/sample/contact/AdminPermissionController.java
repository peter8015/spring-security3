/*     */ package sample.contact;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceAware;
/*     */ import org.springframework.context.support.MessageSourceAccessor;
/*     */ import org.springframework.dao.DataAccessException;
/*     */ import org.springframework.security.acls.domain.BasePermission;
/*     */ import org.springframework.security.acls.domain.DefaultPermissionFactory;
/*     */ import org.springframework.security.acls.domain.ObjectIdentityImpl;
/*     */ import org.springframework.security.acls.domain.PermissionFactory;
/*     */ import org.springframework.security.acls.domain.PrincipalSid;
/*     */ import org.springframework.security.acls.model.Acl;
/*     */ import org.springframework.security.acls.model.AclService;
/*     */ import org.springframework.security.acls.model.Permission;
/*     */ import org.springframework.security.acls.model.Sid;
/*     */ import org.springframework.stereotype.Controller;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.Validator;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.annotation.InitBinder;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.SessionAttributes;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ 
/*     */ @Controller
/*     */ @SessionAttributes({"addPermission"})
/*     */ public final class AdminPermissionController
/*     */   implements MessageSourceAware
/*     */ {
/*     */ 
/*     */   @Autowired
/*     */   private AclService aclService;
/*     */ 
/*     */   @Autowired
/*     */   private ContactManager contactManager;
/*     */   private MessageSourceAccessor messages;
/*  48 */   private final Validator addPermissionValidator = new AddPermissionValidator();
/*  49 */   private final PermissionFactory permissionFactory = new DefaultPermissionFactory();
/*     */ 
/*     */   @RequestMapping(value={"/secure/adminPermission.htm"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
/*     */   public ModelAndView displayAdminPage(@RequestParam("contactId") int contactId)
/*     */   {
/*  56 */     Contact contact = this.contactManager.getById(Long.valueOf(contactId));
/*  57 */     Acl acl = this.aclService.readAclById(new ObjectIdentityImpl(contact));
/*     */ 
/*  59 */     Map model = new HashMap();
/*  60 */     model.put("contact", contact);
/*  61 */     model.put("acl", acl);
/*     */ 
/*  63 */     return new ModelAndView("adminPermission", "model", model);
/*     */   }
/*     */ 
/*     */   @RequestMapping(value={"/secure/addPermission.htm"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
/*     */   public ModelAndView displayAddPermissionPageForContact(@RequestParam("contactId") int contactId)
/*     */   {
/*  71 */     Contact contact = this.contactManager.getById(new Long(contactId));
/*     */ 
/*  73 */     AddPermission addPermission = new AddPermission();
/*  74 */     addPermission.setContact(contact);
/*     */ 
/*  76 */     Map model = new HashMap();
/*  77 */     model.put("addPermission", addPermission);
/*  78 */     model.put("recipients", listRecipients());
/*  79 */     model.put("permissions", listPermissions());
/*     */ 
/*  81 */     return new ModelAndView("addPermission", model);
/*     */   }
/*     */   @InitBinder({"addPermission"})
/*     */   public void initBinder(WebDataBinder binder) {
/*  86 */     binder.setAllowedFields(new String[] { "recipient", "permission" });
/*     */   }
/*     */ 
/*     */   @RequestMapping(value={"/secure/addPermission.htm"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
/*     */   public String addPermission(AddPermission addPermission, BindingResult result, ModelMap model)
/*     */   {
/*  94 */     this.addPermissionValidator.validate(addPermission, result);
/*     */ 
/*  96 */     if (result.hasErrors()) {
/*  97 */       model.put("recipients", listRecipients());
/*  98 */       model.put("permissions", listPermissions());
/*     */ 
/* 100 */       return "addPermission";
/*     */     }
/*     */ 
/* 103 */     PrincipalSid sid = new PrincipalSid(addPermission.getRecipient());
/* 104 */     Permission permission = this.permissionFactory.buildFromMask(addPermission.getPermission().intValue());
/*     */     try
/*     */     {
/* 107 */       this.contactManager.addPermission(addPermission.getContact(), sid, permission);
/*     */     } catch (DataAccessException existingPermission) {
/* 109 */       existingPermission.printStackTrace();
/* 110 */       result.rejectValue("recipient", "err.recipientExistsForContact", "Addition failure.");
/*     */ 
/* 112 */       model.put("recipients", listRecipients());
/* 113 */       model.put("permissions", listPermissions());
/* 114 */       return "addPermission";
/*     */     }
/*     */ 
/* 117 */     return "redirect:/secure/index.htm";
/*     */   }
/*     */ 
/*     */   @RequestMapping({"/secure/deletePermission.htm"})
/*     */   public ModelAndView deletePermission(@RequestParam("contactId") int contactId, @RequestParam("sid") String sid, @RequestParam("permission") int mask)
/*     */   {
/* 129 */     Contact contact = this.contactManager.getById(new Long(contactId));
/*     */ 
/* 131 */     Sid sidObject = new PrincipalSid(sid);
/* 132 */     Permission permission = this.permissionFactory.buildFromMask(mask);
/*     */ 
/* 134 */     this.contactManager.deletePermission(contact, sidObject, permission);
/*     */ 
/* 136 */     Map model = new HashMap();
/* 137 */     model.put("contact", contact);
/* 138 */     model.put("sid", sidObject);
/* 139 */     model.put("permission", permission);
/*     */ 
/* 141 */     return new ModelAndView("deletePermission", "model", model);
/*     */   }
/*     */ 
/*     */   private Map<Integer, String> listPermissions() {
/* 145 */     Map map = new LinkedHashMap();
/* 146 */     map.put(Integer.valueOf(BasePermission.ADMINISTRATION.getMask()), this.messages.getMessage("select.administer", "Administer"));
/* 147 */     map.put(Integer.valueOf(BasePermission.READ.getMask()), this.messages.getMessage("select.read", "Read"));
/* 148 */     map.put(Integer.valueOf(BasePermission.DELETE.getMask()), this.messages.getMessage("select.delete", "Delete"));
/*     */ 
/* 150 */     return map;
/*     */   }
/*     */ 
/*     */   private Map<String, String> listRecipients() {
/* 154 */     Map map = new LinkedHashMap();
/* 155 */     map.put("", this.messages.getMessage("select.pleaseSelect", "-- please select --"));
/*     */ 
/* 157 */     for (String recipient : this.contactManager.getAllRecipients()) {
/* 158 */       map.put(recipient, recipient);
/*     */     }
/*     */ 
/* 161 */     return map;
/*     */   }
/*     */ 
/*     */   public void setMessageSource(MessageSource messageSource) {
/* 165 */     this.messages = new MessageSourceAccessor(messageSource);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\apache-tomcat-6.0.37\webapps\spring-security-samples-contacts-3.1.0.RELEASE\WEB-INF\classes\sample\contact\contact.zip
 * Qualified Name:     AdminPermissionController
 * JD-Core Version:    0.6.0
 */