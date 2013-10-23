/*    */ package sample.contact;
/*    */ 
/*    */ import org.springframework.security.acls.domain.BasePermission;
/*    */ import org.springframework.security.acls.model.Permission;
/*    */ 
/*    */ public class AddPermission
/*    */ {
/*    */   public Contact contact;
/* 29 */   public Integer permission = Integer.valueOf(BasePermission.READ.getMask());
/*    */   public String recipient;
/*    */ 
/*    */   public Contact getContact()
/*    */   {
/* 35 */     return this.contact;
/*    */   }
/*    */ 
/*    */   public Integer getPermission() {
/* 39 */     return this.permission;
/*    */   }
/*    */ 
/*    */   public String getRecipient() {
/* 43 */     return this.recipient;
/*    */   }
/*    */ 
/*    */   public void setContact(Contact contact) {
/* 47 */     this.contact = contact;
/*    */   }
/*    */ 
/*    */   public void setPermission(Integer permission) {
/* 51 */     this.permission = permission;
/*    */   }
/*    */ 
/*    */   public void setRecipient(String recipient) {
/* 55 */     this.recipient = recipient;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\apache-tomcat-6.0.37\webapps\spring-security-samples-contacts-3.1.0.RELEASE\WEB-INF\classes\sample\contact\contact.zip
 * Qualified Name:     AddPermission
 * JD-Core Version:    0.6.0
 */