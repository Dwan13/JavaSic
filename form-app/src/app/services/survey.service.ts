import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Survey } from '../models/survey';
import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class SurveyService {

  private baseUrl: string = `${environment.url_users}`;
  private domino: string = `${environment.url}`;

  constructor(private http: HttpClient) {}

  // Obtener todas las surveys de un usuario específico
  findAllByUserId(userId: number, page: number): Observable<Survey> {
    console.log('userId', userId);
    
    return this.http.get<Survey>(`${this.baseUrl}/${userId}/surveys`);
  }

  // Obtener una survey por su ID y el ID del usuario
  findAllPageable(page: number): Observable<Survey> {
    return this.http.get<Survey>(`${this.baseUrl}/surveys?page=${page}`);
  }

  // Crear una nueva survey para un usuario específico
  create(userId: number, survey: Survey): Observable<Survey> {
    
    const { id, ...userWithoutId } = survey; 
    console.log('userWithoutId', survey, 'userId', userId);

    return this.http.post<Survey>(`${this.baseUrl}/${userId}/surveys`, userWithoutId);
  }

  // Actualizar una survey existente para un usuario específico
  update(survey: Survey): Observable<Survey> {
    return this.http.put<Survey>(`${this.baseUrl}/${survey.user_id}/surveys/${survey.id}`, survey);
  }

  // Eliminar una survey por su ID y el ID del usuario
  delete(userId: number ,survey: Survey): Observable<void> {    
    return this.http.delete<void>(`${this.baseUrl}/${userId}/surveys/${survey.id}`);
  }
  
  getBrands(): Observable<{ id: number, name: string }[]> {
    return this.http.get<{ id: number, name: string }[]>(`${this.domino}api/brands`);
  }
  
  
}