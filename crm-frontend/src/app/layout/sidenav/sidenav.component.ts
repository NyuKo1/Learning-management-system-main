import { Component, Input } from '@angular/core';
import { environment } from '../../../environments/environment';

interface NavItem {
  label: string;
  icon: string;
  route: string;
}

@Component({
  selector: 'crm-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent {
  @Input() open = true;

  lmsUrl = environment.lmsUrl;

  navItems: NavItem[] = [
    { label: 'Дашборд',  icon: 'dashboard',  route: '/dashboard' },
    { label: 'Лиды',     icon: 'person_add', route: '/leads' },
    { label: 'Клиенты',  icon: 'group',      route: '/clients' },
    { label: 'Курсы',    icon: 'school',     route: '/courses' },
    { label: 'Платежи',  icon: 'payments',   route: '/payments' }
  ];
}
