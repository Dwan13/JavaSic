import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { StoreModule } from '@ngrx/store';
import { authReducer } from './store/auth/auth.reducer';

@NgModule({
  declarations: [],
  imports: [
    BrowserModule,
    StoreModule.forRoot({ auth: authReducer }),
  ],
  providers: [],
  bootstrap: []
})
export class AppModule { }
