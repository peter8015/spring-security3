/*    */ package sample.contact;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Controller;
/*    */ import org.springframework.validation.BindingResult;
/*    */ import org.springframework.validation.Validator;
/*    */ import org.springframework.web.bind.WebDataBinder;
/*    */ import org.springframework.web.bind.annotation.InitBinder;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RequestParam;
/*    */ import org.springframework.web.servlet.ModelAndView;
/*    */ 
/*    */ @Controller
/*    */ public class AddDeleteContactController
/*    */ {
/*    */ 
/*    */   @Autowired
/*    */   private ContactManager contactManager;
/* 23 */   private final Validator validator = new WebContactValidator();
/*    */ 
/*    */   @RequestMapping(value={"/secure/add.htm"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
/*    */   public ModelAndView addContactDisplay()
/*    */   {
/* 30 */     return new ModelAndView("add", "webContact", new WebContact());
/*    */   }
/*    */   @InitBinder
/*    */   public void initBinder(WebDataBinder binder) {
/* 35 */     System.out.println("A binder for object: " + binder.getObjectName());
/*    */   }
/*    */ 
/*    */   @RequestMapping(value={"/secure/add.htm"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
/*    */   public String addContact(WebContact form, BindingResult result)
/*    */   {
/* 44 */     this.validator.validate(form, result);
/*    */ 
/* 46 */     if (result.hasErrors()) {
/* 47 */       return "add";
/*    */     }
/*    */ 
/* 50 */     Contact contact = new Contact(form.getName(), form.getEmail());
/* 51 */     this.contactManager.create(contact);
/*    */ 
/* 53 */     return "redirect:/secure/index.htm";
/*    */   }
/*    */   @RequestMapping(value={"/secure/del.htm"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
/*    */   public ModelAndView handleRequest(@RequestParam("contactId") int contactId) {
/* 58 */     Contact contact = this.contactManager.getById(Long.valueOf(contactId));
/* 59 */     this.contactManager.delete(contact);
/*    */ 
/* 61 */     return new ModelAndView("deleted", "contact", contact);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\apache-tomcat-6.0.37\webapps\spring-security-samples-contacts-3.1.0.RELEASE\WEB-INF\classes\sample\contact\contact.zip
 * Qualified Name:     AddDeleteContactController
 * JD-Core Version:    0.6.0
 */