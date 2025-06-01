import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-prediction-dialog',
  templateUrl: './prediction-dialog.component.html',
  styleUrls: ['./prediction-dialog.component.css'],
})
export class PredictionDialogComponent implements OnInit {
  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: {
      predictedLabel: string;
      probabilities: { [key: string]: number };
    },
    private dialogRef: MatDialogRef<PredictionDialogComponent>
  ) {}

  ngOnInit(): void {
    console.log('Prediction Dialog Data:', this.data);
    console.log(
      'Probabilities Keys:',
      this.objectKeys(this.data.probabilities)
    );
    console.log(
      'Probabilities Values:',
      Object.values(this.data.probabilities)
    );
  }

  objectKeys(obj: any): string[] {
    if (!obj) {
      console.error('Object is undefined or null:', obj);
      return [];
    }
    return Object.keys(obj);
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
