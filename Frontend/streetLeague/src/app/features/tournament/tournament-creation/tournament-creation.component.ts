import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { ChampionshipMode } from 'src/app/models/championship-mode';
import { Status } from 'src/app/models/status';
import { TournamentType } from 'src/app/models/tournament-type';
import { TournamentService } from 'src/app/services/tournament.service';
import { catchError, finalize } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-tournament-creation',
  templateUrl: './tournament-creation.component.html',
  styleUrls: ['./tournament-creation.component.css'],
})
export class TournamentCreationComponent implements OnInit {
  tournamentForm!: FormGroup;
  tournamentTypes = TournamentType;
  championshipModes = ChampionshipMode;
  isSubmitting = false;

  constructor(
    private tournamentService: TournamentService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.setupFormSubscriptions();
  }

  private initializeForm(): void {
    this.tournamentForm = this.fb.group(
      {
        name: ['', [Validators.required, Validators.minLength(3)]],
        description: [''],
        startDate: [null, [Validators.required]],
        endDate: [null, [Validators.required]],
        teamRegistrationDeadline: [null, [Validators.required]],
        awards: ['', [Validators.required]],
        tournamentRules: this.fb.group({
          tournamentType: [TournamentType.CHAMPIONSHIP, [Validators.required]],
          championshipMode: [ChampionshipMode.HOME_ONLY],
          numberOfGroups: [null],
          teamsPerGroup: [null],
          numberOfTeams: [
            8,
            [Validators.required, Validators.min(4), Validators.max(32)],
          ],
          roundFrequency: [7, [Validators.required, Validators.min(1)]],
          mainRoster: [11, [Validators.required, Validators.min(1)]],
          matchLineup: [11, [Validators.required, Validators.min(1)]],
          substitutes: [5, [Validators.required, Validators.min(0)]],
          isOverTimeRequired: [false],
          overTimeDuration: [null],
          matchDuration: [90, [Validators.required, Validators.min(1)]],
          halftime: [15, [Validators.required, Validators.min(1)]],
          videoReview: [false],
        }),
      },
      { validators: this.tournamentDateValidator }
    );
  }

  private setupFormSubscriptions(): void {
    // Tournament type changes
    this.tournamentRulesForm
      ?.get('tournamentType')
      ?.valueChanges.subscribe((type) => {
        if (!this.tournamentRulesForm) return;

        if (type === TournamentType.CHAMPIONSHIP) {
          this.updateChampionshipFields();
        } else if (type === TournamentType.GROUP_STAGE) {
          this.updateGroupStageFields();
        }
      });

    // Overtime changes
    this.tournamentRulesForm
      ?.get('isOverTimeRequired')
      ?.valueChanges.subscribe((required) => {
        if (!this.tournamentRulesForm) return;

        const overTimeDurationControl =
          this.tournamentRulesForm.get('overTimeDuration');
        if (!overTimeDurationControl) return;

        if (required) {
          overTimeDurationControl.setValidators([
            Validators.required,
            Validators.min(1),
          ]);
        } else {
          overTimeDurationControl.clearValidators();
          overTimeDurationControl.setValue(null);
        }
        overTimeDurationControl.updateValueAndValidity();
      });
  }

  private updateChampionshipFields(): void {
    if (!this.tournamentRulesForm) return;

    this.tournamentRulesForm.patchValue({
      championshipMode: ChampionshipMode.HOME_ONLY,
      numberOfGroups: null,
      teamsPerGroup: null,
    });

    // Update validators
    this.tournamentRulesForm
      .get('championshipMode')
      ?.setValidators([Validators.required]);
    this.tournamentRulesForm.get('numberOfGroups')?.clearValidators();
    this.tournamentRulesForm.get('teamsPerGroup')?.clearValidators();
  }

  private updateGroupStageFields(): void {
    if (!this.tournamentRulesForm) return;

    this.tournamentRulesForm.patchValue({
      championshipMode: null,
      numberOfGroups: 4,
      teamsPerGroup: 4,
    });

    // Update validators
    this.tournamentRulesForm.get('championshipMode')?.clearValidators();
    this.tournamentRulesForm
      .get('numberOfGroups')
      ?.setValidators([
        Validators.required,
        Validators.min(2),
        Validators.max(8),
      ]);
    this.tournamentRulesForm
      .get('teamsPerGroup')
      ?.setValidators([
        Validators.required,
        Validators.min(2),
        Validators.max(8),
      ]);
  }

  private sanitizeDates(data: any): any {
    return {
      ...data,
      startDate: data.startDate
        ? new Date(data.startDate).toISOString().split('T')[0]
        : null,
      endDate: data.endDate
        ? new Date(data.endDate).toISOString().split('T')[0]
        : null,
      teamRegistrationDeadline: data.teamRegistrationDeadline
        ? new Date(data.teamRegistrationDeadline).toISOString().split('T')[0]
        : null,
    };
  }

  onSubmit(): void {
    if (this.tournamentForm.invalid || this.isSubmitting) return;

    this.isSubmitting = true;
    const tournament = this.sanitizeDates(this.tournamentForm.value);
    console.log(
      'Payload sent to /tournamentRules/add:',
      tournament.tournamentRules
    );

    this.tournamentService
      .createTournamentRules(tournament.tournamentRules)
      .pipe(
        catchError((error) => {
          console.error('Error creating tournament rules:', error);
          return of(null);
        }),
        finalize(() => (this.isSubmitting = false))
      )
      .subscribe((rules) => {
        if (!rules) return;
        delete rules.id;
        const newTournament = {
          name: tournament.name,
          description: tournament.description,
          startDate: tournament.startDate,
          endDate: tournament.endDate,
          teamRegistrationDeadline: tournament.teamRegistrationDeadline,
          awards: tournament.awards,
          tournamentRules: rules,
          status: Status.PENDING,
          participatingTeamIds: [],
          userId: 1,
        };
        console.log('Payload sent to /tournament/add:', newTournament);

        this.tournamentService
          .addTournament(newTournament)
          .pipe(
            catchError((error) => {
              console.error('Error creating tournament:', error);
              return of(null);
            })
          )
          .subscribe((response) => {
            if (response) {
              this.router.navigate(['/tournaments', response.id]);
            }
          });
      });
  }

  get tournamentRulesForm(): AbstractControl | null {
    return this.tournamentForm.get('tournamentRules');
  }

  private tournamentDateValidator: ValidatorFn = (
    group: AbstractControl
  ): ValidationErrors | null => {
    const startDate = group.get('startDate')?.value;
    const endDate = group.get('endDate')?.value;
    const deadline = group.get('teamRegistrationDeadline')?.value;

    if (!startDate || !endDate || !deadline) return null;

    const startDateTime = new Date(startDate).getTime();
    const endDateTime = new Date(endDate).getTime();
    const deadlineTime = new Date(deadline).getTime();
    const threeWeeksInMs = 21 * 24 * 60 * 60 * 1000;

    const isEndBeforeStart = endDateTime <= startDateTime;
    const isDeadlineTooLate = deadlineTime >= startDateTime;
    const isDeadlineLessThan3Weeks =
      startDateTime - deadlineTime < threeWeeksInMs;

    if (isEndBeforeStart || isDeadlineTooLate || isDeadlineLessThan3Weeks) {
      return { invalidTournamentDates: true };
    }

    return null;
  };
}
