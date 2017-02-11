import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Student {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private int id;
  private String name;

  @ManyToMany 
  private Collection<Department> departments;
  public Student() {
    departments = new ArrayList<Department>();
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

  public void setName(String name) {
    this.name = name;
  }
  public void addDepartment(Department department) {
    if (!getDepartments().contains(department)) {
        getDepartments().add(department);
    }
    if (!department.getStudents().contains(this)) {
      department.getStudents().add(this);
    }
  }


  public Collection<Department> getDepartments() {
    return departments;
  }

  public void setDepartment(Collection<Department> departments) {
    this.departments = departments;
  }

  public String toString() {
    return "\n\nID:" + id + "\nName:" + name ;
  }
}
