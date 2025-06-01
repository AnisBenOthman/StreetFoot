package tn.esprit.tournamentservice.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tournamentservice.Entities.Team;
import tn.esprit.tournamentservice.Repositories.TeamRepository;
import tn.esprit.tournamentservice.Service.TeamService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamImpl implements TeamService {
    final TeamRepository teamRepository;

    @Override
    public Team add(Team entity) {
        Team saved = teamRepository.save(entity);
        return saved;
    }

    @Override
    public Team update(Team entity, Long id) {
        return teamRepository.findById(id).map(existedTeam -> {
            existedTeam.setName(entity.getName());
            return teamRepository.save(existedTeam);
        }).orElseThrow(() -> new EntityNotFoundException("Team with id" + id + "not found"));

    }

    @Override
    public Team retrieveById(Long id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team with id " + id + "not found"));
        return team;
    }

    @Override
    public void delete(Long id) {
    teamRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        teamRepository.deleteAll();

    }

    @Override
    public List<Team> getAll() {
        return teamRepository.findAll();
    }

    @Override
    public Optional<Team>  findbyname(String name) {
        return teamRepository.findByName(name);
    }
}
