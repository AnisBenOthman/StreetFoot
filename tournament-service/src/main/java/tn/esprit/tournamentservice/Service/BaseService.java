package tn.esprit.tournamentservice.Service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    T add(T entity);
    T update(T entity, ID id);
    T retrieveById(ID id);
    void delete(ID id);
    List<T> getAll();
}
