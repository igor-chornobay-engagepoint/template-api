package gov.ca.cwds.template.persistence.dao;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import gov.ca.cwds.data.BaseDaoImpl;
import gov.ca.cwds.template.inject.TemplateSessionFactory;
import gov.ca.cwds.template.persistence.model.TemplateInstance;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 * @author CWDS TPT-2 Team
 */
public class TemplateDao extends BaseDaoImpl<TemplateInstance> {

  @Inject
  public TemplateDao(@TemplateSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public List<TemplateInstance> findAll() {
    Session session = this.getSessionFactory().getCurrentSession();
    Query<TemplateInstance> query =
        session.createNamedQuery(TemplateInstance.NAMED_QUERY_FIND_ALL, TemplateInstance.class);
    ImmutableList.Builder<TemplateInstance> entities = new ImmutableList.Builder<>();
    entities.addAll(query.list());
    return entities.build();
  }

  public List<TemplateInstance> findByName(String name) {
    Session session = this.getSessionFactory().getCurrentSession();
    Query<TemplateInstance> query =
        session.createNamedQuery(TemplateInstance.NAMED_QUERY_FIND_BY_TEMPLATE_NAME, TemplateInstance.class);
    query.setParameter("templateName", name);
    ImmutableList.Builder<TemplateInstance> entities = new ImmutableList.Builder<>();
    entities.addAll(query.list());
    return entities.build();

  }
}
