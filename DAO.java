package Final_project;

import java.util.List;

public interface DAO<T> {
    boolean add(T t);

    boolean delete(T t);

    boolean edit(T t);

    List<T> getBase();
}
