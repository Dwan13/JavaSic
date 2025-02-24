import { Injectable } from '@angular/core';
import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthStoreService {
  constructor(private store: Store<{ auth: any }>) {}

  getUserId(): Observable<number> {
    return this.store.pipe(
      select(state => state.auth.userId),
      map(userId => userId ?? 0) // Asegurar que siempre devuelva un número
    );
  }

}
