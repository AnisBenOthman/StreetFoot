package tn.esprit.scedulingservice.ServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.scedulingservice.Entities.Round;
import tn.esprit.scedulingservice.Repositories.RoundRepository;
import tn.esprit.scedulingservice.Service.RoundService;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class RoundServiceImpl implements RoundService {
    RoundRepository roundRepository;
    @Override
    public Optional<Round> retrieveById(String s) {
        return Optional.empty();
    }

    @Override
    public List<Round> retrieveAll() {
        return null;
    }

    @Override
    public Round add(Round object) {
        return null;
    }

    @Override
    public Round update(Round object, String s) {
        return null;
    }

    @Override
    public void delete(String s) {

    }
}
