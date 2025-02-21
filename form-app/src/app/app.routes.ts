import { Routes } from '@angular/router';
import { UserComponent } from './components/user/user.component';
import { UserFormComponent } from './components/user-form/user-form.component';
import { SurveyComponent } from './components/survey/survey.component';
import { SurveyFormComponent } from './components/survey-form/survey-form.component';
import { AuthComponent } from './components/auth/auth.component';
import { Forbidden403Component } from './components/forbidden403/forbidden403.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: '/users/create'
    },
    // Rutas para Usuarios
    {
        path: 'users',
        component: UserComponent,
    },
    {
        path: 'users/page/:page',
        component: UserComponent,
    },
    {
        path: 'users/create', 
        component: UserFormComponent,
        canActivate: [authGuard]
    },
    {
        path: 'users/edit/:id',
        component: UserFormComponent,
        canActivate: [authGuard]
    },

    // Rutas para Encuestas
    {
        path: 'surveys',
        component: SurveyComponent,
    },
    {
        path: 'surveys/page/:page',
        component: SurveyComponent,
    },
    {
        path: 'surveys/create', 
        component: SurveyFormComponent,
    },
    {
        path: 'surveys/edit/:id',
        component: SurveyFormComponent,
    },

    // Rutas de autenticación y errores
    {
        path: 'login',
        component: AuthComponent
    },
    {
        path: 'forbidden',
        component: Forbidden403Component
    }
];
