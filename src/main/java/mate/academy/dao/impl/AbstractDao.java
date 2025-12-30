package mate.academy.dao.impl;

import org.hibernate.SessionFactory;

public abstract class AbstractDao {
    protected SessionFactory factory;

    public AbstractDao(SessionFactory sessionFactory) {
        this.factory = sessionFactory;
    }
}
