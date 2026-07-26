import { Component } from '@angular/core';

@Component({
  selector: 'app-medico-list',
  standalone: true,
  template: `
    <div class="page">
      <h1>Médicos</h1>
      <p>Gestión de médicos - Próximamente</p>
    </div>
  `,
  styles: [`.page { padding: 16px; }`]
})
export class MedicoListComponent {}
