package mate.academy.dao;

import org.hibernate.SessionFactory;

public abstract class AbstractDao {
    protected SessionFactory factory;

    public AbstractDao(SessionFactory sessionFactory) {
        this.factory = sessionFactory;
    }
}
