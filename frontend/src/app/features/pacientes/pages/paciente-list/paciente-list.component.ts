import { Component } from '@angular/core';

@Component({
  selector: 'app-paciente-list',
  standalone: true,
  template: `
    <div class="page">
      <h1>Pacientes</h1>
      <p>Gestión de pacientes - Próximamente</p>
    </div>
  `,
  styles: [`.page { padding: 16px; }`]
})
export class PacienteListComponent {}
