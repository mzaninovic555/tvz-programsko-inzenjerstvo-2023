import React from 'react';
import ErrorBoundary from './components/ErrorBoundary';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import ToastWrapper from './components/ToastWrapper';
import {ConfirmDialog} from 'primereact/confirmdialog';
import Homepage from './views/Homepage';
import Test from './views/Test';

function App() {
  return (
    <ErrorBoundary>
      <ToastWrapper>
        <h1>Title</h1>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<Homepage/>}/>
            <Route path="/test" element={<Test/>}/>
          </Routes>
        </BrowserRouter>
        <ConfirmDialog/>
      </ToastWrapper>
    </ErrorBoundary>
  );
}

export default App;
