package tn.esprit.tournamentservice.ServiceImpl;

import esprit.tn.shared.config.EventEnvelop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import tn.esprit.tournamentservice.DTO.SchedulingRequest;
import tn.esprit.tournamentservice.Entities.*;
import tn.esprit.tournamentservice.Exception.TournamentException;
import tn.esprit.tournamentservice.FeignClient.SchedulingClient;
import tn.esprit.tournamentservice.Repositories.TournamentRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TournamentImplTest {
    @InjectMocks
    private TournamentImpl tournamentService;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private SchedulingClient schedulingClient;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    private Tournament sampleTournament;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        TournamentRules rules = new TournamentRules();
        rules.setTournamentType(TournamentType.CHAMPIONSHIP);
        rules.setChampionshipMode(ChampionshipMode.HOME_AWAY);
        rules.setNumberOfTeams(4);
        rules.setNumberOfGroups(0);
        rules.setTeamsPerGroup(0);
        rules.setRoundFrequency(7);

        sampleTournament = new Tournament();
        sampleTournament.setName("Test Tournament");
        sampleTournament.setTournamentRules(rules);
        sampleTournament.setParticipatingTeamIds(new ArrayList<>());
        sampleTournament.setStatus(Status.PENDING);
        sampleTournament.setStartDate(LocalDate.now());
        sampleTournament.setEndDate(LocalDate.of(2025, 1, 5));
    }

    @Test
    void testAddTournament_Success() {
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(sampleTournament);

        Tournament result = tournamentService.add(sampleTournament);

        assertNotNull(result);
        verify(kafkaTemplate, times(1)).send(eq("tournament.generated"), any(EventEnvelop.class));
    }

    @Test
    void testAddTournament_Failure() {
        when(tournamentRepository.save(any())).thenThrow(new RuntimeException("DB error"));

        TournamentException exception = assertThrows(TournamentException.class,
                () -> tournamentService.add(sampleTournament));

        assertTrue(exception.getMessage().contains("Failed to save tournament"));
    }

    @Test
    void testUpdateTournament_Success() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(sampleTournament));
        when(tournamentRepository.save(any())).thenReturn(sampleTournament);

        Tournament updated = tournamentService.update(sampleTournament, 1L);

        assertNotNull(updated);
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }

    @Test
    void testRetrieveById_Success() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(sampleTournament));

        Tournament result = tournamentService.retrieveById(1L);
        assertEquals("Test Tournament", result.getName());
    }

    @Test
    void testDelete_Success() {
        when(tournamentRepository.existsById(1L)).thenReturn(true);

        tournamentService.delete(1L);
        verify(tournamentRepository, times(1)).deleteById(1L);
    }



    @Test
    void testParticipateInTournament_AlreadyRegistered() {
        sampleTournament.setParticipatingTeamIds(new ArrayList<>(List.of(5)));
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(sampleTournament));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> tournamentService.participateINTournament(1L, 5));
        assertTrue(exception.getMessage().contains("déja inscrite"));
    }
}