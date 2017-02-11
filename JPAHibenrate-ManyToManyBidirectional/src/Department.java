
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Department {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToMany(mappedBy="departments")
    private Collection<Student> students;
    
    public Department(){
      students = new ArrayList<Student>();
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String deptName) {
        this.name = deptName;
    }
    public void addStudent(Student student) {
      if (!getStudents().contains(student)) {
          getStudents().add(student);
      }
      if (!student.getDepartments().contains(this)) {
          student.getDepartments().add(this);
      }
    }
    public Collection<Student> getStudents() {
      return students;
    }

    public void setStudent(Collection<Student> students) {
      this.students = students;
    }

    public String toString() {
        return "Department id: " + getId() + 
               ", name: " + getName();
    }
}
