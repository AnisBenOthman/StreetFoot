import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface ConfirmDialogData {
  title: string;
  message: string;
}

@Component({
  selector: 'app-confirm-dialog',
  template: `
    <div class="dialog-container">
      <h2 mat-dialog-title>{{ data.title }}</h2>
      <mat-dialog-content>
        <p>{{ data.message }}</p>
      </mat-dialog-content>
      <mat-dialog-actions align="end">
        <button mat-button mat-dialog-close>Annuler</button>
        <button mat-raised-button color="warn" [mat-dialog-close]="true">
          Confirmer
        </button>
      </mat-dialog-actions>
    </div>
  `,
  styles: [
    `
      .dialog-container {
        padding: 24px;
        min-width: 350px;
      }
      h2 {
        margin: 0;
        font-size: 20px;
        font-weight: 500;
        color: rgba(0, 0, 0, 0.87);
      }
      mat-dialog-content {
        margin: 20px 0;
        color: rgba(0, 0, 0, 0.6);
        font-size: 16px;
      }
      mat-dialog-actions {
        padding: 8px 0 0;
        margin-bottom: 0;
        gap: 8px;
      }
      button {
        text-transform: uppercase;
        font-weight: 500;
      }
    `,
  ],
})
export class ConfirmDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData
  ) {}
}
