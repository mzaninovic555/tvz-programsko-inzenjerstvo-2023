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
    label: 'Component Search',
    icon: 'pi pi-search',
    url: '/component-search'
  }
];

export const appName = 'PIOS-App';
