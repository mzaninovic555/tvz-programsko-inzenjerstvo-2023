import {MenuItem} from 'primereact/menuitem';

export const menuItems: MenuItem[] = [
  {
    label: 'Home',
    icon: 'pi pi-home',
    url: '/'
  },
  {
    label: 'Builds',
    icon: 'pi pi-server',
    url: '/builds'
  },
  {
    label: 'Wishlist',
    icon: 'pi pi-list',
    url: '/wishlist'
  }
];

export const appName = 'PIOS-App';
