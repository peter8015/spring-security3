/*    */ package sample.contact;
/*    */ 
/*    */ import org.springframework.validation.Errors;
/*    */ import org.springframework.validation.Validator;
/*    */ 
/*    */ public class WebContactValidator
/*    */   implements Validator
/*    */ {
/*    */   public boolean supports(Class clazz)
/*    */   {
/* 32 */     return clazz.equals(WebContact.class);
/*    */   }
/*    */ 
/*    */   public void validate(Object obj, Errors errors) {
/* 36 */     WebContact wc = (WebContact)obj;
/*    */ 
/* 38 */     if ((wc.getName() == null) || (wc.getName().length() < 3) || (wc.getName().length() > 50)) {
/* 39 */       errors.rejectValue("name", "err.name", "Name 3-50 characters is required. *");
/*    */     }
/*    */ 
/* 42 */     if ((wc.getEmail() == null) || (wc.getEmail().length() < 3) || (wc.getEmail().length() > 50))
/* 43 */       errors.rejectValue("email", "err.email", "Email 3-50 characters is required. *");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\apache-tomcat-6.0.37\webapps\spring-security-samples-contacts-3.1.0.RELEASE\WEB-INF\classes\sample\contact\contact.zip
 * Qualified Name:     WebContactValidator
 * JD-Core Version:    0.6.0
 */