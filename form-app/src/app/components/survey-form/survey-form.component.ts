import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Survey } from '../../models/survey';
import { SharingDataService } from '../../services/sharing-data.service';
import { SurveyService } from '../../services/survey.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-survey-form',
  imports: [FormsModule, CommonModule],
  standalone: true,
  templateUrl: './survey-form.component.html', // Asegúrate de que la ruta sea correcta
})
export class SurveyFormComponent implements OnInit {
  survey: Survey;
  errors: any = {};
  userId: number = 0; // Almacena el userId como número
  computerTypes: { id: number; name: string }[] = [];
  hasId: boolean = false;
  constructor(
    private service: SurveyService,
    private sharingData: SharingDataService,
    private route: ActivatedRoute,
    private authStore: AuthService

  ) {
    this.survey = new Survey();
  }

  ngOnInit(): void {
    this.userId = this.authStore.getUserIdFromToken(); // Usa el método del token

    this.sharingData.findSurveyByIdEventEmitter.subscribe((errors) => (this.errors = errors));

    this.sharingData.selectSurveyEventEmitter.subscribe((survey: Survey) => {
      if (survey) {
        this.survey = survey;
      }
    });

    this.route.paramMap.subscribe((params) => {
      const id: number = +(params.get('id') || '0');

      if (id > 0) {
        this.hasId = true;
        this.sharingData.findSurveyByIdEventEmitter.emit(id);
      }
    });

    this.getComputerTypes();
  }

  onSubmit(surveyForm: NgForm): void {
    console.log('surveyForm', surveyForm.form.value);
    let surveyNew = surveyForm.form.value
    let formattedSurvey = {
      ...this.survey,
      brand: { id: surveyNew.computer_type, name: surveyNew.email.toString() },
    };

    this.sharingData.newSurveyEventEmitter.emit({ idUser: this.userId, survey: formattedSurvey });
  }

  onClear(surveyForm: NgForm): void {
    this.survey = new Survey();
    surveyForm.reset();
    surveyForm.resetForm();
  }

  getComputerTypes() {
    this.service.getBrands().subscribe({
      next: (data) => {
        console.log('Data from service:', data);
        if (Array.isArray(data)) {
          this.computerTypes = data;
          console.log('Updated computerTypes:', this.computerTypes);
        } else {
          console.error('Data received is not an array:', data);
        }
      },
      error: (err) => {
        console.error('Error fetching computer types', err);
      },
    });
  }
}
