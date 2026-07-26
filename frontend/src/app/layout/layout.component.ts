import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, HeaderComponent],
  template: `
    <app-sidebar />
    <app-header />
    <main class="content">
      <router-outlet />
    </main>
  `,
  styles: [`
    .content {
      margin-left: 250px;
      margin-top: 64px;
      padding: 24px;
      min-height: calc(100vh - 64px);
      background-color: #fafafa;
    }
  `]
})
export class LayoutComponent {}
