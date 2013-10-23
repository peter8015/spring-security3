package sample.contact;

import java.util.List;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

public abstract interface ContactManager
{
  @PreAuthorize("hasPermission(#contact, admin)")
  public abstract void addPermission(Contact paramContact, Sid paramSid, Permission paramPermission);

  @PreAuthorize("hasPermission(#contact, admin)")
  public abstract void deletePermission(Contact paramContact, Sid paramSid, Permission paramPermission);

  @PreAuthorize("hasRole('ROLE_USER')")
  public abstract void create(Contact paramContact);

  @PreAuthorize("hasPermission(#contact, 'delete') or hasPermission(#contact, admin)")
  public abstract void delete(Contact paramContact);

  @PreAuthorize("hasRole('ROLE_USER')")
  @PostFilter("hasPermission(filterObject, 'read') or hasPermission(filterObject, admin)")
  public abstract List<Contact> getAll();

  @PreAuthorize("hasRole('ROLE_USER')")
  public abstract List<String> getAllRecipients();

  @PreAuthorize("hasPermission(#id, 'sample.contact.Contact', read) or hasPermission(#id, 'sample.contact.Contact', admin)")
  public abstract Contact getById(Long paramLong);

  public abstract Contact getRandomContact();
}

/* Location:           C:\Program Files\apache-tomcat-6.0.37\webapps\spring-security-samples-contacts-3.1.0.RELEASE\WEB-INF\classes\sample\contact\contact.zip
 * Qualified Name:     ContactManager
 * JD-Core Version:    0.6.0
 */