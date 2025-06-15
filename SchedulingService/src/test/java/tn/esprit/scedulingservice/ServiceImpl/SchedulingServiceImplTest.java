package tn.esprit.scedulingservice.ServiceImpl;

import esprit.tn.shared.config.EventEnvelop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import tn.esprit.scedulingservice.DTO.SchedulingRequest;
import tn.esprit.scedulingservice.Entities.MatchSchedule;
import tn.esprit.scedulingservice.Entities.Round;
import tn.esprit.scedulingservice.Entities.Status;
import tn.esprit.scedulingservice.Repositories.MatchSceduleRepository;
import tn.esprit.scedulingservice.Repositories.RoundRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SchedulingServiceImplTest {
    @Mock
    private RoundRepository roundRepository;

    @Mock
    private MatchSceduleRepository matchSceduleRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private SchedulingServiceImpl schedulingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private SchedulingRequest sampleChampionshipRequest() {
        return new SchedulingRequest(
                1L,
                "CHAMPIONSHIP",
                "HOME_AWAY",
                4,
                0,
                0,
                Arrays.asList(1, 2, 3, 4),
                2,
                LocalDate.now(),
                LocalDate.now().plusDays(10)
        );
    }

    private SchedulingRequest sampleGroupStageRequest() {
        return new SchedulingRequest(
                2L,
                "GROUP_STAGE",
                "",
                4,
                2,
                2,
                Arrays.asList(1, 2, 3, 4),
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );
    }

    @Test
    void testGenerateChampionshipSchedule() {
        SchedulingRequest request = sampleChampionshipRequest();

        Round savedRound = new Round();
        savedRound.setId("round-1");
        when(roundRepository.save(any(Round.class))).thenReturn(savedRound);

        schedulingService.generateChampionshipSchedule(request);

        verify(roundRepository, atLeastOnce()).save(any(Round.class));
        verify(matchSceduleRepository, atLeastOnce()).saveAll(anyList());
    }

    @Test
    void testGenerateGroupStageSchedule() {
        SchedulingRequest request = sampleGroupStageRequest();

        Round savedRound = new Round();
        savedRound.setId("round-1");
        when(roundRepository.save(any(Round.class))).thenReturn(savedRound);

        schedulingService.generateGroupStageSchedule(request);

        verify(roundRepository, atLeastOnce()).save(any(Round.class));
        verify(matchSceduleRepository, atLeastOnce()).saveAll(anyList());
    }

    @Test
    void testGenerateSchedulingWithChampionship() {
        SchedulingRequest request = sampleChampionshipRequest();

        when(roundRepository.findByTournamentId(request.tournamentId()))
                .thenReturn(List.of(new Round()));

        schedulingService.generateScheduling(request);

        verify(kafkaTemplate, times(1)).send(eq("schedule.generated"), any(EventEnvelop.class));
    }

    @Test
    void testGenerateSchedulingWithGroupStage() {
        SchedulingRequest request = sampleGroupStageRequest();

        when(roundRepository.findByTournamentId(request.tournamentId()))
                .thenReturn(List.of(new Round()));

        schedulingService.generateScheduling(request);

        verify(kafkaTemplate, times(1)).send(eq("schedule.generated"), any(EventEnvelop.class));
    }

    @Test
    void testGenerateSchedulingWithInvalidType() {
        SchedulingRequest invalidRequest = new SchedulingRequest(
                99L,
                "KNOCKOUT",
                "",
                4,
                0,
                0,
                Arrays.asList(1, 2, 3, 4),
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(10)
        );

        assertThrows(IllegalArgumentException.class,
                () -> schedulingService.generateScheduling(invalidRequest));
    }
}