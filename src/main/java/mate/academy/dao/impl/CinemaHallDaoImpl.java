package mate.academy.dao.impl;

import java.util.List;
import mate.academy.dao.CinemaHallDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.CinemaHall;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class CinemaHallDaoImpl extends AbstractDao implements CinemaHallDao {
    public CinemaHallDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public CinemaHall add(CinemaHall cinemaHall) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(cinemaHall);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert cinema hall " + cinemaHall, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return cinemaHall;
    }

    @Override
    public CinemaHall get(Long id) {
        try (Session session = factory.openSession()) {
            Query<CinemaHall> getOrderQuery = session.createQuery(
                    "from CinemaHall o left join fetch o.movie_sessions "
                            + "where o.id = :id", CinemaHall.class
            );
            getOrderQuery.setParameter("id", id);
            return getOrderQuery.getSingleResult();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get a cinema hall by id: " + id, e);
        }
    }

    @Override
    public List<CinemaHall> getAll() {
        try (Session session = factory.openSession()) {
            Query<CinemaHall> fromMovie = session.createQuery("FROM CinemaHall", CinemaHall.class);
            return fromMovie.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all cinema halls.", e);
        }
    }
}
