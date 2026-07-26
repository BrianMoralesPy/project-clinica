import { Component } from '@angular/core';

@Component({
  selector: 'app-turno-list',
  standalone: true,
  template: `
    <div class="page">
      <h1>Turnos</h1>
      <p>Gestión de turnos - Próximamente</p>
    </div>
  `,
  styles: [`.page { padding: 16px; }`]
})
export class TurnoListComponent {}
