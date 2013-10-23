/*     */ package sample.contact;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.support.ApplicationObjectSupport;
/*     */ import org.springframework.security.acls.domain.BasePermission;
/*     */ import org.springframework.security.acls.domain.ObjectIdentityImpl;
/*     */ import org.springframework.security.acls.domain.PrincipalSid;
/*     */ import org.springframework.security.acls.model.AccessControlEntry;
/*     */ import org.springframework.security.acls.model.MutableAcl;
/*     */ import org.springframework.security.acls.model.MutableAclService;
/*     */ import org.springframework.security.acls.model.NotFoundException;
/*     */ import org.springframework.security.acls.model.ObjectIdentity;
/*     */ import org.springframework.security.acls.model.Permission;
/*     */ import org.springframework.security.acls.model.Sid;
/*     */ import org.springframework.security.core.Authentication;
/*     */ import org.springframework.security.core.context.SecurityContext;
/*     */ import org.springframework.security.core.context.SecurityContextHolder;
/*     */ import org.springframework.security.core.userdetails.UserDetails;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ @Transactional
/*     */ public class ContactManagerBackend extends ApplicationObjectSupport
/*     */   implements ContactManager, InitializingBean
/*     */ {
/*     */   private ContactDao contactDao;
/*     */   private MutableAclService mutableAclService;
/*  56 */   private int counter = 1000;
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */     throws Exception
/*     */   {
/*  61 */     Assert.notNull(this.contactDao, "contactDao required");
/*  62 */     Assert.notNull(this.mutableAclService, "mutableAclService required");
/*     */   }
/*     */   public void addPermission(Contact contact, Sid recipient, Permission permission) {
/*  67 */     ObjectIdentity oid = new ObjectIdentityImpl(Contact.class, contact.getId());
/*     */     MutableAcl acl;
/*     */     try {
/*  70 */       acl = (MutableAcl)this.mutableAclService.readAclById(oid);
/*     */     } catch (NotFoundException nfe) {
/*  72 */       acl = this.mutableAclService.createAcl(oid);
/*     */     }
/*     */ 
/*  75 */     acl.insertAce(acl.getEntries().size(), permission, recipient, true);
/*  76 */     this.mutableAclService.updateAcl(acl);
/*     */ 
/*  78 */     this.logger.debug("Added permission " + permission + " for Sid " + recipient + " contact " + contact);
/*     */   }
/*     */ 
/*     */   public void create(Contact contact)
/*     */   {
/*  83 */     contact.setId(new Long(this.counter++));
/*  84 */     this.contactDao.create(contact);
/*     */ 
/*  87 */     addPermission(contact, new PrincipalSid(getUsername()), BasePermission.ADMINISTRATION);
/*     */ 
/*  89 */     if (this.logger.isDebugEnabled())
/*  90 */       this.logger.debug("Created contact " + contact + " and granted admin permission to recipient " + getUsername());
/*     */   }
/*     */ 
/*     */   public void delete(Contact contact)
/*     */   {
/*  95 */     this.contactDao.delete(contact.getId());
/*     */ 
/*  98 */     ObjectIdentity oid = new ObjectIdentityImpl(Contact.class, contact.getId());
/*  99 */     this.mutableAclService.deleteAcl(oid, false);
/*     */ 
/* 101 */     if (this.logger.isDebugEnabled())
/* 102 */       this.logger.debug("Deleted contact " + contact + " including ACL permissions");
/*     */   }
/*     */ 
/*     */   public void deletePermission(Contact contact, Sid recipient, Permission permission)
/*     */   {
/* 107 */     ObjectIdentity oid = new ObjectIdentityImpl(Contact.class, contact.getId());
/* 108 */     MutableAcl acl = (MutableAcl)this.mutableAclService.readAclById(oid);
/*     */ 
/* 111 */     List entries = acl.getEntries();
/*     */ 
/* 113 */     for (int i = 0; i < entries.size(); i++) {
/* 114 */       if ((((AccessControlEntry)entries.get(i)).getSid().equals(recipient)) && (((AccessControlEntry)entries.get(i)).getPermission().equals(permission))) {
/* 115 */         acl.deleteAce(i);
/*     */       }
/*     */     }
/*     */ 
/* 119 */     this.mutableAclService.updateAcl(acl);
/*     */ 
/* 121 */     if (this.logger.isDebugEnabled())
/* 122 */       this.logger.debug("Deleted contact " + contact + " ACL permissions for recipient " + recipient);
/*     */   }
/*     */ 
/*     */   @Transactional(readOnly=true)
/*     */   public List<Contact> getAll() {
/* 128 */     this.logger.debug("Returning all contacts");
/*     */ 
/* 130 */     return this.contactDao.findAll();
/*     */   }
/*     */   @Transactional(readOnly=true)
/*     */   public List<String> getAllRecipients() {
/* 135 */     this.logger.debug("Returning all recipients");
/*     */ 
/* 137 */     return this.contactDao.findAllPrincipals();
/*     */   }
/*     */   @Transactional(readOnly=true)
/*     */   public Contact getById(Long id) {
/* 142 */     if (this.logger.isDebugEnabled()) {
/* 143 */       this.logger.debug("Returning contact with id: " + id);
/*     */     }
/*     */ 
/* 146 */     return this.contactDao.getById(id);
/*     */   }
/*     */ 
/*     */   @Transactional(readOnly=true)
/*     */   public Contact getRandomContact()
/*     */   {
/* 154 */     this.logger.debug("Returning random contact");
/*     */ 
/* 156 */     Random rnd = new Random();
/* 157 */     List contacts = this.contactDao.findAll();
/* 158 */     int getNumber = rnd.nextInt(contacts.size());
/*     */ 
/* 160 */     return (Contact)contacts.get(getNumber);
/*     */   }
/*     */ 
/*     */   protected String getUsername() {
/* 164 */     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
/*     */ 
/* 166 */     if ((auth.getPrincipal() instanceof UserDetails)) {
/* 167 */       return ((UserDetails)auth.getPrincipal()).getUsername();
/*     */     }
/* 169 */     return auth.getPrincipal().toString();
/*     */   }
/*     */ 
/*     */   public void setContactDao(ContactDao contactDao)
/*     */   {
/* 174 */     this.contactDao = contactDao;
/*     */   }
/*     */ 
/*     */   public void setMutableAclService(MutableAclService mutableAclService) {
/* 178 */     this.mutableAclService = mutableAclService;
/*     */   }
/*     */ 
/*     */   public void update(Contact contact) {
/* 182 */     this.contactDao.update(contact);
/*     */ 
/* 184 */     this.logger.debug("Updated contact " + contact);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\apache-tomcat-6.0.37\webapps\spring-security-samples-contacts-3.1.0.RELEASE\WEB-INF\classes\sample\contact\contact.zip
 * Qualified Name:     ContactManagerBackend
 * JD-Core Version:    0.6.0
 */