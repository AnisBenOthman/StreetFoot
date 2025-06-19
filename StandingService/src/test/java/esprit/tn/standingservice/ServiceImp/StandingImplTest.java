package esprit.tn.standingservice.ServiceImp;

import esprit.tn.shared.config.DTO.MatchScoreUpdateEvent;
import esprit.tn.shared.config.DTO.ScheduleGeneratedEvent;
import esprit.tn.shared.config.EventEnvelop;
import esprit.tn.standingservice.Entities.Standings;
import esprit.tn.standingservice.Repositories.StandingRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StandingImplTest {

    @Mock
    private StandingRepo standingRepo;

    @InjectMocks
    private StandingImpl standingService;

    private Standings standings1;
    private Standings standings2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


    standings1= new Standings(1L,101L,5,3,1,1,15,10,10);
    standings2= new Standings(1L,102L,5,2,2,1,12,12,8);
}
    @Test
    void retrieveById_ShouldReturnStandings_WhenExists() {
        when(standingRepo.findById("1")).thenReturn(Optional.of(standings1));

        Optional<Standings> result = standingService.retrieveById("1");

        assertTrue(result.isPresent());
        assertEquals(standings1, result.get());
    }

    @Test
    void retrieveById_ShouldReturnEmpty_WhenNotExists() {
        when(standingRepo.findById("999")).thenReturn(Optional.empty());

        Optional<Standings> result = standingService.retrieveById("999");

        assertFalse(result.isPresent());
    }

    @Test
    void retrieveAll_ShouldReturnAllStandings() {
        when(standingRepo.findAll()).thenReturn(Arrays.asList(standings1, standings2));

        List<Standings> result = standingService.retrieveAll();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(standings1, standings2)));
    }

    @Test
    void add_ShouldSaveAndReturnStandings() {
        when(standingRepo.save(any(Standings.class))).thenReturn(standings1);

        Standings result = standingService.add(standings1);

        assertNotNull(result);
        assertEquals(standings1, result);
        verify(standingRepo).save(standings1);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        doNothing().when(standingRepo).deleteById("1");

        standingService.delete("1");

        verify(standingRepo).deleteById("1");
    }

    @Test
    void deleteAll_ShouldCallRepositoryDeleteAll() {
        doNothing().when(standingRepo).deleteAll();

        standingService.deleteAll();

        verify(standingRepo).deleteAll();
    }

    @Test
    void findByTournamentId_ShouldReturnStandings_WhenExists() {
        when(standingRepo.findByTournamentId(1L)).thenReturn(Optional.of(Arrays.asList(standings1, standings2)));

        List<Standings> result = standingService.findByTournamentId(1L);

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(standings1, standings2)));
    }

    @Test
    void findByTournamentId_ShouldThrowException_WhenNotFound() {
        when(standingRepo.findByTournamentId(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> standingService.findByTournamentId(999L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Tournament with id 999not found"));
    }

    @Test
    void findByTournamentIdAndTeamId_ShouldReturnStandings_WhenExists() {
        when(standingRepo.findByTournamentIdAndTeamId(1L, 101L)).thenReturn(Optional.of(standings1));

        Standings result = standingService.findByTournamentIdAndTeamId(1L, 101L);

        assertEquals(standings1, result);
    }

    @Test
    void findByTournamentIdAndTeamId_ShouldThrowException_WhenNotFound() {
        when(standingRepo.findByTournamentIdAndTeamId(1L, 999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> standingService.findByTournamentIdAndTeamId(1L, 999L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Team with id999not found"));
    }



    @Test
    void updateStandingsForMatch_ShouldUpdateBothTeamsStandings() {
        MatchScoreUpdateEvent payload = new MatchScoreUpdateEvent(1L, "101L", "102L", 101L, 102L, 2, 1, 101L);

        EventEnvelop<MatchScoreUpdateEvent> event = new EventEnvelop<>();
        event.setPayload(payload);

        when(standingRepo.findByTournamentIdAndTeamId(1L, 101L)).thenReturn(Optional.of(standings1));
        when(standingRepo.findByTournamentIdAndTeamId(1L, 102L)).thenReturn(Optional.of(standings2));

        standingService.updateStandingsForMatch(event);

        // Verify home team (winner) stats

        assertEquals(7, standings1.getGoalScored()); // original 15 + 2
        assertEquals(4, standings1.getGoalConceded()); // original 10 + 1


        // Verify away team (loser) stats
        assertEquals(3, standings2.getMatchesPlayed()); // original 5 + 1
        assertEquals(13, standings2.getLosses()); // original 1 + 1
        assertEquals(6, standings2.getGoalScored()); // original 12 + 1
        assertEquals(4, standings2.getGoalConceded()); // original 12 + 2


        verify(standingRepo).save(standings1);
        verify(standingRepo).save(standings2);
    }

    @Test
    void applyResult_ShouldUpdateStatsCorrectly_ForWin() {
        Standings standings = new Standings(1L, 101L, 5, 3, 1, 1, 15, 10, 10);

        standingService.applyResult(standings, 2, 1);

        assertEquals(2, standings.getMatchesPlayed());
        assertEquals(2, standings.getWins());
        assertEquals(10, standings.getLosses());
        assertEquals(15, standings.getDraws());
        assertEquals(7, standings.getGoalScored());
        assertEquals(4, standings.getGoalConceded());
        
    }

}



