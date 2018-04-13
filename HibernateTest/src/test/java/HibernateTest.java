import com.sct.hibernatetest.entity.UserInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Test;

import java.util.Date;

public class HibernateTest {
    @Test
    public void testNativeHibernate() {
        try {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

            Session session = sessionFactory.openSession();

            Transaction transaction = session.beginTransaction();
            UserInfo userInfo = new UserInfo();
            userInfo.setId("1");
            userInfo.setName("test");
            userInfo.setRegisterDate(new Date());

            session.save(userInfo);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
