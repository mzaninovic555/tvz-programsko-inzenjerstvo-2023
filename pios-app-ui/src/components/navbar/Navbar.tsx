import React, {SyntheticEvent, useRef} from 'react';
import {Menubar} from 'primereact/menubar';
import {appName, menuItems} from '../../common/const';
import {useLocation, useNavigate} from 'react-router-dom';
import {MenuItem, MenuItemCommandEvent} from 'primereact/menuitem';
import {Menu} from 'primereact/menu';
import {Button} from 'primereact/button';
import useAuthContext from '../../context/AuthContext';
import useToastContext from '../../context/ToastContext';
import {apiToToast} from '../../common/messages/messageHelper';
import {logoutSuccessMessage} from '../../common/messages/LocalMessages';
import logo from '../../../static/logo.svg';

const Navbar = () => {
  const navigate = useNavigate();
  const userMenu = useRef<Menu>(null);
  const {auth, setToken} = useAuthContext();
  const userButton = useRef<Button>(null);
  const location = useLocation();
  const {toast} = useToastContext();

  const items = menuItems.map(((x) => ({...x})));

  const navigateToUrl = (e: MenuItemCommandEvent, url: string) => {
    e?.originalEvent?.preventDefault();
    navigate(url);
  };

  items.forEach((menuItem) => {
    if (!menuItem.url) {
      console.warn('MenuItem without url', menuItem);
      return;
    }

    menuItem.command = (e: MenuItemCommandEvent) => navigateToUrl(e, menuItem.url!);
    const path = '/' + (location.pathname.split('/').filter((str) => str)[0] || '');
    menuItem.className = path == menuItem.url ? 'nav-current-item' : 'nav-item';
  });

  const header = (
    <div className="flex justify-content-center flex-wrap align-items-center">
      <div className="w-4rem mr-2">
        <img src={logo as string} width="100%"/>
      </div>
      <div>
        <h1 style={{cursor: 'pointer', fontSize: 'xxx-large'}} onClick={() => navigate('/')}
          className="mt-0 mb-1 color-primary">{appName}</h1>
      </div>
    </div>
  );

  const logOut = () => {
    setToken(undefined);
    toast.current?.show(apiToToast(logoutSuccessMessage));
  };

  const userMenuItems: MenuItem[] = [
    {
      label: 'User Settings',
      icon: 'pi pi-cog',
      url: '/settings',
      command: (e: MenuItemCommandEvent) => navigateToUrl(e, '/settings')
    },
    {
      label: 'Wishlist',
      icon: 'pi pi-list',
      url: '/wishlist',
      command: (e: MenuItemCommandEvent) => navigateToUrl(e, '/wishlist')
    },
    {label: 'Logout', icon: 'pi pi-sign-out', command: logOut}
  ];

  const showUserMenu = (event: React.FormEvent) => {
    userMenu?.current?.toggle?.(event as unknown as SyntheticEvent);
    event.preventDefault();
  };

  const authDiv = (
    <div className="flex justify-content-center">
      {auth.authenticated && <>
        <>
          <Menu model={userMenuItems} popup ref={userMenu}/>
          <Button className="p-button-text" ref={userButton} label={auth.info?.username} icon="pi pi-user"
            onClick={showUserMenu} aria-haspopup/>
        </>
      </>}
      {!auth.authenticated && location.pathname != '/login' && location.pathname != '/register' && <>
        <Button label="Sign in" className="p-button-text" icon="pi pi-sign-in"
          onClick={(e) => navigateToUrl(e as any, '/login')} aria-haspopup/>
      </>}
    </div>
  );

  return (
    <div>
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
    </div>
  );
};

export default Navbar;
