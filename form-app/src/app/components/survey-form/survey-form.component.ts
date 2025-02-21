import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Survey } from '../../models/survey';
import { SurveyService } from '../../services/survey.service';
import { SharingDataService } from '../../services/sharing-data.service';

@Component({
  selector: 'app-survey-form',
  imports: [FormsModule],
  standalone: true,
  templateUrl: './survey-form.component.html', // Asegúrate de que la ruta sea correcta
})
export class SurveyFormComponent implements OnInit {

  survey: Survey;
  errors: any = {};

  constructor(
    private surveyService: SurveyService,
    private sharingData: SharingDataService,
    private route: ActivatedRoute,
  ){
    this.survey = new Survey();
  }

  ngOnInit(): void {
    this.sharingData.findSurveyByIdEventEmitter.subscribe(errors => this.errors = errors);

    this.sharingData.selectSurveyEventEmitter.subscribe((survey: Survey) => {
      if (survey) {
        this.survey = survey;
      }
    });   

    this.route.paramMap.subscribe(params => {
      const id: number = +(params.get('id') || '0');

      if (id > 0) {
        this.sharingData.findSurveyByIdEventEmitter.emit(id);
      }
    });
  }
 onSubmit(surveyForm: NgForm): void {
      this.sharingData.newSurveyEventEmitter.emit(this.survey);
  }

  onClear(surveyForm: NgForm): void {
    this.survey = new Survey();
    surveyForm.reset();
    surveyForm.resetForm();
  }
}