/*    */ package sample.contact;
/*    */ 
/*    */ import org.springframework.security.acls.domain.BasePermission;
/*    */ import org.springframework.security.acls.model.Permission;
/*    */ import org.springframework.validation.Errors;
/*    */ import org.springframework.validation.ValidationUtils;
/*    */ import org.springframework.validation.Validator;
/*    */ 
/*    */ public class AddPermissionValidator
/*    */   implements Validator
/*    */ {
/*    */   public boolean supports(Class clazz)
/*    */   {
/* 34 */     return clazz.equals(AddPermission.class);
/*    */   }
/*    */ 
/*    */   public void validate(Object obj, Errors errors) {
/* 38 */     AddPermission addPermission = (AddPermission)obj;
/*    */ 
/* 40 */     ValidationUtils.rejectIfEmptyOrWhitespace(errors, "permission", "err.permission", "Permission is required. *");
/* 41 */     ValidationUtils.rejectIfEmptyOrWhitespace(errors, "recipient", "err.recipient", "Recipient is required. *");
/*    */ 
/* 43 */     if (addPermission.getPermission() != null) {
/* 44 */       int permission = addPermission.getPermission().intValue();
/*    */ 
/* 46 */       if ((permission != BasePermission.ADMINISTRATION.getMask()) && (permission != BasePermission.READ.getMask()) && (permission != BasePermission.DELETE.getMask()))
/*    */       {
/* 48 */         errors.rejectValue("permission", "err.permission.invalid", "The indicated permission is invalid. *");
/*    */       }
/*    */     }
/*    */ 
/* 52 */     if ((addPermission.getRecipient() != null) && 
/* 53 */       (addPermission.getRecipient().length() > 100))
/* 54 */       errors.rejectValue("recipient", "err.recipient.length", "The recipient is too long (maximum 100 characters). *");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\apache-tomcat-6.0.37\webapps\spring-security-samples-contacts-3.1.0.RELEASE\WEB-INF\classes\sample\contact\contact.zip
 * Qualified Name:     AddPermissionValidator
 * JD-Core Version:    0.6.0
 */