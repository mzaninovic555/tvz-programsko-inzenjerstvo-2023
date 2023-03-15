import React from 'react';
import ErrorBoundary from './components/ErrorBoundary';
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import ToastWrapper from './components/ToastWrapper';
import {ConfirmDialog} from 'primereact/confirmdialog';
import Homepage from './views/homepage/Homepage';
import Login from './views/login/Login';
import Register from './views/register/Register';
import useAuth from './common/hooks/useAuth';
import Navbar from './components/navbar/Navbar';
import Builds from './views/builds/Builds';
import Wishlist from './views/wishlist/Wishlist';
import Settings from './views/settings/Settings';

function App() {
  const {authenticated} = useAuth();
  return (
    <ErrorBoundary>
      <ToastWrapper>
        <BrowserRouter>
          <>{authenticated && <Navbar/>}</>
          <div className="container">
            <Routes>
              <Route path="/login" element={<Login/>}/>
              <Route path="/register" element={<Register/>}/>
              <Route path="/builds" element={<Builds/>}/>
              <Route path="/wishlist" element={<Wishlist/>}/>
              <Route path="/settings" element={<Settings/>}/>
              <Route path="/" element={<Homepage/>}/>
              <Route path="*" element={<Navigate to="/"/>}/>
            </Routes>
          </div>
        </BrowserRouter>
        <ConfirmDialog/>
      </ToastWrapper>
    </ErrorBoundary>
  );
}

export default App;
