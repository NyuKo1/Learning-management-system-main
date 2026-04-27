import { Component } from '@angular/core';

@Component({
  selector: 'crm-shell',
  templateUrl: './shell.component.html',
  styleUrls: ['./shell.component.scss']
})
export class ShellComponent {
  sidenavOpen = true;

  toggleSidenav(): void {
    this.sidenavOpen = !this.sidenavOpen;
  }
}
