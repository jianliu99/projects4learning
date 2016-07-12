package mypackage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;

@WebServlet(loadOnStartup = 1, urlPatterns = "/BookServlet")
public class BookServlet extends HttpServlet {

    // Inject ItemEJB instance
    @EJB
    private ItemEJB itemEJB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Creates an instance of book
        Book book = new Book();
        book.setTitle(request.getParameter("title"));

        // Call EJB method asynchronously
        itemEJB.createBookAsynch(book);
        try {
            // Sleep for 2000 ms to give async. thread to finish the task
            Thread.sleep(2000);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            // Displays the books
            out.println("<h1>====== All books ======</h1>");
            //List<Book> books = em.createNamedQuery("findAllBooks").getResultList();
            List<Book> books = itemEJB.findAllBooks();
            for (int i = 0; i < books.size(); i++) {
                Book b = books.get(i);
                out.println("Book title: " + b.getTitle() + "<br/>");
            }
        } catch (Exception ie) {
        }

    }
}
