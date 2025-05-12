import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Match } from 'src/app/models/additional-models';

@Component({
  selector: 'app-match-update-dialog',
  template: `
    <h2 mat-dialog-title>Mettre à jour le score</h2>
    <mat-dialog-content>
      <form [formGroup]="scoreForm">
        <div class="score-inputs">
          <mat-form-field appearance="outline">
            <mat-label>Score équipe locale</mat-label>
            <input
              matInput
              type="number"
              formControlName="homeScore"
              required
              min="0"
            />
            <mat-error *ngIf="scoreForm.get('homeScore')?.hasError('required')">
              Le score est requis
            </mat-error>
            <mat-error *ngIf="scoreForm.get('homeScore')?.hasError('min')">
              Le score ne peut pas être négatif
            </mat-error>
          </mat-form-field>

          <span class="score-separator">-</span>

          <mat-form-field appearance="outline">
            <mat-label>Score équipe visiteur</mat-label>
            <input
              matInput
              type="number"
              formControlName="awayScore"
              required
              min="0"
            />
            <mat-error *ngIf="scoreForm.get('awayScore')?.hasError('required')">
              Le score est requis
            </mat-error>
            <mat-error *ngIf="scoreForm.get('awayScore')?.hasError('min')">
              Le score ne peut pas être négatif
            </mat-error>
          </mat-form-field>
        </div>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Annuler</button>
      <button
        mat-raised-button
        color="primary"
        (click)="onSubmit()"
        [disabled]="scoreForm.invalid"
      >
        Mettre à jour
      </button>
    </mat-dialog-actions>
  `,
  styles: [
    `
      .score-inputs {
        display: flex;
        align-items: center;
        gap: 16px;
        padding: 16px 0;
      }
      .score-separator {
        font-size: 20px;
        font-weight: bold;
      }
      mat-form-field {
        flex: 1;
      }
    `,
  ],
})
export class MatchUpdateDialogComponent {
  scoreForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<MatchUpdateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { match: Match }
  ) {
    this.scoreForm = this.fb.group({
      homeScore: [null, [Validators.required, Validators.min(0)]],
      awayScore: [null, [Validators.required, Validators.min(0)]],
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (this.scoreForm.valid) {
      this.dialogRef.close(this.scoreForm.value);
    }
  }
}
