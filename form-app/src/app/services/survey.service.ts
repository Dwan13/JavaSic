import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Survey } from '../models/survey';
import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class SurveyService {

  private baseUrl: string = `${environment.url_survays}`;

  constructor(private http: HttpClient) {}

  findAll(): Observable<Survey[]> {
    return this.http.get<Survey[]>(`${this.baseUrl}`);
  }

  findAllPageable(page: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/page/${page}`);
  }

  findById(id: number): Observable<Survey> {
    return this.http.get<Survey>(`${this.baseUrl}/${id}`);
  }

  create(survey: Survey): Observable<Survey> {
    return this.http.post<Survey>(this.baseUrl, survey);
  }

  update(survey: Survey): Observable<Survey> {
    return this.http.put<Survey>(`${this.baseUrl}/${survey.id}`, survey);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
