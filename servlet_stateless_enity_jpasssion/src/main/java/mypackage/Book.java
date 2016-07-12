package mypackage;

import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "findAllBooks", query = "SELECT b FROM Book b")
})
public class Book {

    @Id
    @GeneratedValue
    private Long id;
    private String title;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
