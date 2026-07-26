import { Component } from '@angular/core';

@Component({
  selector: 'app-usuario-list',
  standalone: true,
  template: `
    <div class="page">
      <h1>Usuarios</h1>
      <p>Gestión de usuarios - Próximamente</p>
    </div>
  `,
  styles: [`.page { padding: 16px; }`]
})
export class UsuarioListComponent {}
