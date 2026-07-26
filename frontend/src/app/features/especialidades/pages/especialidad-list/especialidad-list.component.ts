import { Component } from '@angular/core';

@Component({
  selector: 'app-especialidad-list',
  standalone: true,
  template: `
    <div class="page">
      <h1>Especialidades</h1>
      <p>Gestión de especialidades - Próximamente</p>
    </div>
  `,
  styles: [`.page { padding: 16px; }`]
})
export class EspecialidadListComponent {}
