import { Component, ChangeDetectionStrategy } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatRippleModule } from '@angular/material/core';

@Component({
  selector: 'app-bienvenida-page',
  standalone: true,
  imports: [
    RouterLink,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatRippleModule // Para el efecto de click en las tarjetas
  ],
  templateUrl: './bienvenida-page.component.html',
  styleUrl: './bienvenida-page.component.css',
  // OnPush mejora el rendimiento al no re-renderizar a menos que cambien sus inputs
  changeDetection: ChangeDetectionStrategy.OnPush 
})
export class BienvenidaComponent {
}