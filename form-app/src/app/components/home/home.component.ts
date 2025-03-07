import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [FormsModule, CommonModule],
  standalone: true,
  templateUrl: './home.component.html', // Asegúrate de que la ruta sea correcta
})
export class HomeComponent  {
  
}
