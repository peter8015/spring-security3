package sample.contact;

import java.util.List;

public abstract interface ContactDao
{
  public abstract void create(Contact paramContact);

  public abstract void delete(Long paramLong);

  public abstract List<Contact> findAll();

  public abstract List<String> findAllPrincipals();

  public abstract List<String> findAllRoles();

  public abstract Contact getById(Long paramLong);

  public abstract void update(Contact paramContact);
}

/* Location:           C:\Program Files\apache-tomcat-6.0.37\webapps\spring-security-samples-contacts-3.1.0.RELEASE\WEB-INF\classes\sample\contact\contact.zip
 * Qualified Name:     ContactDao
 * JD-Core Version:    0.6.0
 */