package tn.esprit.scedulingservice.ServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.scedulingservice.Entities.MatchSchedule;
import tn.esprit.scedulingservice.Repositories.MatchSceduleRepository;
import tn.esprit.scedulingservice.Service.MatchSceduleService;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class MatchSceduleServiceImp implements MatchSceduleService {
    MatchSceduleRepository matchSceduleRepository;
    @Override
    public Optional<MatchSchedule> retrieveById(String s) {
        return Optional.empty();
    }

    @Override
    public List<MatchSchedule> retrieveAll() {
        return null;
    }

    @Override
    public MatchSchedule add(MatchSchedule object) {
        return null;
    }

    @Override
    public MatchSchedule update(MatchSchedule object, String s) {
        return null;
    }

    @Override
    public void delete(String s) {

    }
}
