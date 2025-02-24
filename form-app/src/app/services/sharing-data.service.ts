import { EventEmitter, Injectable } from '@angular/core';
import { User } from '../models/user';
import { Survey } from '../models/survey';

@Injectable({
  providedIn: 'root'
})
export class SharingDataService {

  // Usuarios
  private _newUserEventEmitter: EventEmitter<User> = new EventEmitter();
  private _idUserEventEmitter = new EventEmitter<number>();
  private _findUserByIdEventEmitter = new EventEmitter<number>();
  private _selectUserEventEmitter = new EventEmitter<User>();
  private _errorsUserFormEventEmitter = new EventEmitter<string[]>();
  private _pageUsersEventEmitter = new EventEmitter<{ users: User[], paginator: any }>();
  private _handlerLoginEventEmitter = new EventEmitter();

  // Encuestas
  private _newSurveyEventEmitter = new EventEmitter<{ idUser: number, survey: Survey }>();
  private _idSurveyEventEmitter = new EventEmitter<{ idUser: number, survey: Survey }>();
  private _findSurveyByIdEventEmitter = new EventEmitter<number>();
  private _selectSurveyEventEmitter = new EventEmitter<Survey>();
  private _errorsSurveyFormEventEmitter = new EventEmitter<string[]>();
  private _pageSurveysEventEmitter = new EventEmitter<{ surveys: Survey[], paginator: any }>();

  constructor() { }

  // Eventos de autenticación
  get handlerLoginEventEmitter() {
    return this._handlerLoginEventEmitter;
  }

  // Eventos de Usuarios
  get pageUsersEventEmitter() {
    return this._pageUsersEventEmitter;
  }

  get errorsUserFormEventEmitter() {
    return this._errorsUserFormEventEmitter;
  }
  
  get selectUserEventEmitter() {
    return this._selectUserEventEmitter;
  }
  
  get findUserByIdEventEmitter() {
    return this._findUserByIdEventEmitter;
  }

  get newUserEventEmitter(): EventEmitter<User> {
    return this._newUserEventEmitter;
  }

  get idUserEventEmitter(): EventEmitter<number> {
    return this._idUserEventEmitter;
  }

  // Eventos de Encuestas
  get pageSurveysEventEmitter() {
    return this._pageSurveysEventEmitter;
  }
  get errorsSurveyFormEventEmitter() {
    return this._errorsSurveyFormEventEmitter;
  }
  
  get selectSurveyEventEmitter() {
    return this._selectSurveyEventEmitter;
  }
  
  get findSurveyByIdEventEmitter() {
    return this._findSurveyByIdEventEmitter;
  }

  get newSurveyEventEmitter() {
    return this._newSurveyEventEmitter;
  }

  get idSurveyEventEmitter() {
    return this._idSurveyEventEmitter;
  }
}

