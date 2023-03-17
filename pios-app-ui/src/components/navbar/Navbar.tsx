import React, {SyntheticEvent, useRef, useState} from 'react';
import {Menubar} from 'primereact/menubar';
import {appName, menuItems} from '../../common/const';
import {Image} from 'primereact/image';
import {useNavigate} from 'react-router-dom';
import {MenuItem, MenuItemCommandEvent} from 'primereact/menuitem';
import {Menu} from 'primereact/menu';
import {Button} from 'primereact/button';

const Navbar = () => {
  const navigate = useNavigate();
  const userMenu = useRef<Menu>(null);
  const userButton = useRef<Button>(null);
  const [username, setUsername] = useState<string>('N/A');

  const items = menuItems.map(((x) => ({...x})));

  const navigateToUrl = (e: MenuItemCommandEvent, url: string) => {
    e.originalEvent.preventDefault();
    navigate(url);
  };

  items.forEach((menuItem) => {
    if (!menuItem.url) {
      console.warn('MenuItem without url', menuItem);
      return;
    }

    menuItem.command = (e: MenuItemCommandEvent) => navigateToUrl(e, menuItem.url!);
    const path = '/' + (window.location.pathname.split('/').filter((str) => str)[0] || '');
    menuItem.className = path == menuItem.url ? 'nav-current-item' : 'nav-item';
  });

  const header = (
    <div className="flex justify-content-center flex-wrap">
      {/* <Image src={image as string}/> */}
      <div>
        <h1 className="mt-0 mb-1 color-primary">{appName}</h1>
      </div>
    </div>
  );

  const logOut = () => {
    // TODO
  };

  const userMenuItems: MenuItem[] = [
    {
      label: 'User Settings',
      icon: 'pi pi-cog',
      url: '/settings',
      command: (e: MenuItemCommandEvent) => navigateToUrl(e, '/settings')
    },
    {label: 'Logout', icon: 'pi pi-sign-out', command: logOut}
  ];

  const showUserMenu = (event: React.FormEvent) => {
    userMenu?.current?.toggle?.(event as unknown as SyntheticEvent);
    event.preventDefault();
  };

  const authDiv = (
    <div className="flex justify-content-center">
      <>
        <Menu model={userMenuItems} popup ref={userMenu}/>
        <Button className="p-button-text" ref={userButton} label={username} icon="pi pi-user"
          onClick={showUserMenu} aria-haspopup/>
      </>
    </div>
  );

  return (
    <div className="card shadow-1 mb-3 p-menubar p-component flex-column border-noround-top">
      <div className="container">
        <div className="flex w-full justify-content-between align-items-center flex-wrap">
          {header}
          {authDiv}
        </div>
        <div className="w-full">
          <Menubar model={items} className="border-none mt-0 pb-0 p-0"/>
        </div>
      </div>
    </div>
  );
};

export default Navbar;
