package mypackage;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;

@Stateless
public class ItemEJB {

    @PersistenceContext
    private EntityManager em;

    public Book createBook(Book book) {
        em.persist(book);
        return book;
    }

    @Asynchronous
    public void createBookAsynch(Book book) {
        em.persist(book);
    }

    @Asynchronous
    public Future<Book> createBookAsynchUsingFuture(Book book) {
        em.persist(book);
        return new AsyncResult<Book>(book);
    }

    public List<Book> findAllBooks() {
        return em.createNamedQuery("findAllBooks").getResultList();
    }
}
