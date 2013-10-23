/*    */ package sample.contact;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Contact
/*    */   implements Serializable
/*    */ {
/*    */   private Long id;
/*    */   private String email;
/*    */   private String name;
/*    */ 
/*    */   public Contact(String name, String email)
/*    */   {
/* 36 */     this.name = name;
/* 37 */     this.email = email;
/*    */   }
/*    */ 
/*    */   public Contact()
/*    */   {
/*    */   }
/*    */ 
/*    */   public String getEmail()
/*    */   {
/* 48 */     return this.email;
/*    */   }
/*    */ 
/*    */   public Long getId()
/*    */   {
/* 55 */     return this.id;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 62 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setEmail(String email)
/*    */   {
/* 69 */     this.email = email;
/*    */   }
/*    */ 
/*    */   public void setId(Long id) {
/* 73 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public void setName(String name)
/*    */   {
/* 80 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 84 */     StringBuilder sb = new StringBuilder();
/* 85 */     sb.append(super.toString() + ": ");
/* 86 */     sb.append("Id: " + getId() + "; ");
/* 87 */     sb.append("Name: " + getName() + "; ");
/* 88 */     sb.append("Email: " + getEmail());
/*    */ 
/* 90 */     return sb.toString();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\apache-tomcat-6.0.37\webapps\spring-security-samples-contacts-3.1.0.RELEASE\WEB-INF\classes\sample\contact\contact.zip
 * Qualified Name:     Contact
 * JD-Core Version:    0.6.0
 */