import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import Swal from 'sweetalert2';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { NavbarComponent } from './navbar/navbar.component';
import { SharingDataService } from '../services/sharing-data.service';
import { AuthService } from '../services/auth.service';
import { Survey } from '../models/survey';
import { User } from '../models/user';
import { SurveyService } from '../services/survey.service';

@Component({
  selector: 'user-app',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  templateUrl: './user-app.component.html',
  styleUrls: ['./user-app.component.scss']
})
export class UserAppComponent implements OnInit {
  users: User[] = [];
  surveys: Survey[] = [];
  paginator: any = {};
  paginatorSurveys: any = {};

  constructor(
    private router: Router,
    private service: UserService,
    private serviceSurvey: SurveyService,
    private sharingData: SharingDataService,
    private authService: AuthService,
    private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    // this.service.findAll().subscribe(users => this.users = users);

    // this.route.paramMap.subscribe(params => {
    //   const page = +(params.get('page') || '0');
    //   console.log(page)
    //   // this.service.findAllPageable(page).subscribe(pageable => this.users = pageable.content as User[]);
    // })
    this.addUser();
    this.removeUser();
    this.findUserById();
    this.pageUsersEvent();
    this.handlerLogin();
    this.pageSurveyEvent();
    this.findSurveyById();
    this.addSurvey();
    this.removeSurvey();
  }

  handlerLogin() {
    this.sharingData.handlerLoginEventEmitter.subscribe(({ username, password }) => {

      this.authService.loginUser({ username, password }).subscribe({
        next: response => {
          const token = response.token;
          const payload = this.authService.getPayload(token);

          const user = { username: payload.sub };
          const login = {
            user,
            isAuth: true,
            isAdmin: payload.isAdmin
          };

          this.authService.token = token;
          this.authService.user = login;
          if (this.authService.isAdmin()) this.router.navigate(['/users/page/0']);
          this.router.navigate(['users/surveys']);
        },
        error: error => {
          if (error.status == 401) {
            Swal.fire('Error en el Login', error.error.message, 'error')
          } else {
            throw error;
          }
        }
      })
    })
  }

  pageUsersEvent() {
    this.sharingData.pageUsersEventEmitter.subscribe(pageable => {
      this.users = pageable.users;
      this.paginator = pageable.paginator;
    });
  }

  findUserById() {
    this.sharingData.findUserByIdEventEmitter.subscribe(id => {

      const user = this.users.find(user => user.id == id);

      this.sharingData.selectUserEventEmitter.emit(user);
    })
  }

  addUser() {
    this.sharingData.newUserEventEmitter.subscribe(user => {
      if (user.id > 0) {
        this.service.update(user).subscribe(
          {
            next: (userUpdated) => {
              this.users = this.users.map(u => (u.id == userUpdated.id) ? { ...userUpdated } : u);
              this.router.navigate(['/users'], {
                state: {
                  users: this.users,
                  paginator: this.paginator
                }
              });

              Swal.fire({
                title: "Actualizado!",
                text: "Usuario editado con exito!",
                icon: "success"
              });
            },
            error: (err) => {
              // console.log(err.error)
              if (err.status == 400) {
                this.sharingData.errorsUserFormEventEmitter.emit(err.error);
              }
            }
          })

      } else {
        this.service.create(user).subscribe({
          next: userNew => {
            console.log(user)
            this.users = [... this.users, { ...userNew }];

            this.router.navigate(['/users'], {
              state: {
                users: this.users,
                paginator: this.paginator
              }
            });

            Swal.fire({
              title: "Creado nuevo usuario!",
              text: "Usuario creado con exito!",
              icon: "success"
            });
          },
          error: (err) => {
            // console.log(err.error)
            // console.log(err.status)
            if (err.status == 400) {
              this.sharingData.errorsUserFormEventEmitter.emit(err.error);
            }

          }
        })
      }

    })
  }

  removeUser(): void {
    this.sharingData.idUserEventEmitter.subscribe(id => {
      Swal.fire({
        title: "Seguro que quiere eliminar?",
        text: "Cuidado el usuario sera eliminado del sistema!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Si"
      }).then((result) => {
        if (result.isConfirmed) {

          this.service.remove(id).subscribe(() => {
            this.users = this.users.filter(user => user.id != id);
            this.router.navigate(['/users/create'], { skipLocationChange: true }).then(() => {
              this.router.navigate(['/users'], {
                state: {
                  users: this.users,
                  paginator: this.paginator
                }
              });
            });
          })


          Swal.fire({
            title: "Eliminado!",
            text: "Usuario eliminado con exito.",
            icon: "success"
          });
        }
      });
    });
  }
  ///////////

  pageSurveyEvent() {
    this.sharingData.pageSurveysEventEmitter.subscribe(pageable => {
      this.surveys = pageable.surveys;
      this.paginatorSurveys = pageable.paginator;
    });
  }

  findSurveyById() {
    this.sharingData.findSurveyByIdEventEmitter.subscribe(id => {

      const survey = this.surveys.find(survey => survey.id == id);

      this.sharingData.selectSurveyEventEmitter.emit(survey);
    })
  }

  addSurvey() {
    this.sharingData.newSurveyEventEmitter.subscribe(survey => {
      if (survey.survey.id > 0) {
        this.serviceSurvey.update(survey.survey).subscribe(
          {
            next: (surveyUpdated) => {
              this.surveys = this.surveys.map(s => (s.id == surveyUpdated.id) ? { ...surveyUpdated } : s);
              this.router.navigate(['/users/surveys'], {
                state: {
                  users: this.surveys,
                  paginator: this.paginatorSurveys
                }
              });

              Swal.fire({
                title: "Actualizada!",
                text: "Encuesta editada con exito!",
                icon: "success"
              });
            },
            error: (err) => {
              // console.log(err.error)
              if (err.status == 400) {
                this.sharingData.errorsSurveyFormEventEmitter.emit(err.error);
              }
            }
          })

      } else {
        this.serviceSurvey.create(survey.idUser, survey.survey).subscribe({
          next: surveyNew => {
            this.surveys = [... this.surveys, { ...surveyNew }];

            this.router.navigate(['/users/surveys'], {
              state: {
                surveys: this.surveys,
                paginator: this.paginatorSurveys
              }
            });

            Swal.fire({
              title: "Creado nueva Encuesta!",
              text: "Encuesta creada con exito!",
              icon: "success"
            });
          },
          error: (err) => {
            // console.log(err.error)
            // console.log(err.status)
            if (err.status == 400) {
              this.sharingData.errorsSurveyFormEventEmitter.emit(err.error);
            }

          }
        })
      }

    })
  }

  removeSurvey(): void {
    this.sharingData.idSurveyEventEmitter.subscribe(({idUser, survey}) => {
      Swal.fire({
        title: "Seguro que quiere eliminar?",
        text: "Cuidado la encuesta sera eliminada del sistema!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Si"
      }).then((result) => {
        if (result.isConfirmed) {

          this.serviceSurvey.delete(idUser, survey).subscribe(() => {
            this.surveys = this.surveys.filter(survey => survey.id != idUser);
            this.router.navigate(['/users/surveys'], { skipLocationChange: true }).then(() => {
              this.router.navigate(['/users'], {
                state: {
                  surveys: this.surveys,
                  paginator: this.paginatorSurveys
                }
              });
            });
          })


          Swal.fire({
            title: "Eliminada!",
            text: "Encuesta eliminada con exito.",
            icon: "success"
          });
        }
      });
    });
  }
}
