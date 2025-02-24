import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { SurveyService } from '../../services/survey.service';
import { SharingDataService } from '../../services/sharing-data.service';
import { PaginatorComponent } from '../paginator/paginator.component';
import { Survey } from '../../models/survey';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'survey',
  standalone: true,
  imports: [PaginatorComponent, CommonModule, RouterModule],
  templateUrl: './survey.component.html'
})
export class SurveyComponent implements OnInit {

  title: string = 'Listado de encuestas!';
  surveys: Survey[] = [];
  paginator: any = {};
  userId: number = 0; // Almacena el userId como número

  constructor(
    private service: SurveyService,
    private sharingData: SharingDataService,
    private router: Router,
    private route: ActivatedRoute,
    private authStore: AuthService
  ) {
    if (this.router.getCurrentNavigation()?.extras.state) {
      this.surveys = this.router.getCurrentNavigation()?.extras.state!['surveys'];
      this.paginator = this.router.getCurrentNavigation()?.extras.state!['paginator'];
    }
  }

  ngOnInit(): void {
    this.userId = this.authStore.getUserIdFromToken(); // Usa el método del token
    console.log('id user ngOnInit', this.userId);
  
    if (!this.surveys || this.surveys.length === 0) {
      this.route.paramMap.subscribe(params => {
        const page = +(params.get('page') || '0');
        this.service.findAllByUserId(this.userId, page).subscribe({
          next: (pageable) => {
            this.surveys = pageable.content.map((survey: any) => {
              survey.response_date = new Date(survey.response_date);
              return survey;
            });
            console.log('surveys ', this.surveys);
  
            this.paginator = pageable;
            this.sharingData.pageSurveysEventEmitter.emit({ surveys: this.surveys, paginator: this.paginator });
          },
          error: (err) => {
            console.error('Error al cargar las encuestas', err);
          }
        });
      });
    }
  }
  
  

  onRemoveSurvey(surveyId: Survey): void {
    this.service.delete(this.userId, surveyId).subscribe({
      next: () => {
        this.surveys = this.surveys.filter(survey => survey.id !== this.userId);
        this.sharingData.idSurveyEventEmitter.emit({idUser:this.userId, survey: surveyId});
      },
      error: (err) => {
        console.error('Error al eliminar la encuesta', err);
      }
    });
  }
}
