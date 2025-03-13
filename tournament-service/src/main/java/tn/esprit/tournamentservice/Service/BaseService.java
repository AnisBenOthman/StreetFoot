package tn.esprit.tournamentservice.Service;

import java.util.Optional;

public interface BaseService<T, ID> {
    T add(T object);
    T update(T object, ID id);
    Optional<T> retrieveById(ID id);
    void delete(ID id);
}
