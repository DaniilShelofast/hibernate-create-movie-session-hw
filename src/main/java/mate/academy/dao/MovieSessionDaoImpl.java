package mate.academy.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.MovieSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class MovieSessionDaoImpl extends AbstractDao implements MovieSessionDao {
    public MovieSessionDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public MovieSession add(MovieSession movieSession) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(movieSession);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert movie session " + movieSession, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return movieSession;
    }

    @Override
    public Optional<MovieSession> get(Long id) {
        try (Session session = factory.openSession()) {
            Query<MovieSession> getOrderQuery = session.createQuery(
                    "from MovieSession o left join fetch o.movie "
                            + "where o.id = :id", MovieSession.class
            );
            getOrderQuery.setParameter("id", id);
            return Optional.ofNullable(getOrderQuery.getSingleResult());
        } catch (Exception e) {
            throw new DataProcessingException("Can't get a movie session by id: " + id, e);
        }
    }

    @Override
    public List<MovieSession> findAvailableSessions(Long movieId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        try (Session session = factory.openSession()) {
            Query<MovieSession> getOrderQuery = session.createQuery(
                    "from MovieSession "
                            + "o left join fetch o.movie "
                            + "where o.id = :id "
                            + "and o.showTime >= :startOfDay "
                            + "and o.showTime < :endOfDay", MovieSession.class
            );
            getOrderQuery.setParameter("id", movieId);
            getOrderQuery.setParameter("startOfDay", startOfDay);
            getOrderQuery.setParameter("endOfDay", endOfDay);
            return getOrderQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get a movie session by id and by date : "
                    + movieId, e);
        }
    }
}
