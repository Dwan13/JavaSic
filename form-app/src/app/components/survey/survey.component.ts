import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { SurveyService } from '../../services/survey.service';
import { SharingDataService } from '../../services/sharing-data.service';
import { PaginatorComponent } from '../paginator/paginator.component';
import { Survey } from '../../models/survey';

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

  constructor(
    private service: SurveyService,
    private sharingData: SharingDataService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    if (this.router.getCurrentNavigation()?.extras.state) {
      this.surveys = this.router.getCurrentNavigation()?.extras.state!['surveys'];
      this.paginator = this.router.getCurrentNavigation()?.extras.state!['paginator'];
    }
  }

  ngOnInit(): void {
    if (!this.surveys || this.surveys.length === 0) {
      this.route.paramMap.subscribe(params => {
        const page = +(params.get('page') || '0');
        this.service.findAllPageable(page).subscribe({
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

  onRemoveSurvey(id: number): void {
    this.service.delete(id).subscribe({
      next: () => {
        this.surveys = this.surveys.filter(survey => survey.id !== id);
        this.sharingData.idSurveyEventEmitter.emit(id);
      },
      error: (err) => {
        console.error('Error al eliminar la encuesta', err);
      }
    });
  }


  onSelectedSurvey(survey: Survey): void {
    this.router.navigate(['/surveys/edit', survey.id]);
  }
}