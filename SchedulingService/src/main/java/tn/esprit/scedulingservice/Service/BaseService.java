package tn.esprit.scedulingservice.Service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T,ID> {
    Optional<T> retrieveById(ID id);
    List<T> retrieveAll();
    T add(T object);
    T update(T object, ID id);
    void delete(ID id);
    void deleteAll();
}
