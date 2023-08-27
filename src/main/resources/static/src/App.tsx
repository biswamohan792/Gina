import React from 'react';
import { Route, Routes } from 'react-router-dom';
import User from './components/User';
import Service from './components/Service';
import Welcome from './components/Welcome';
import NotificationContext from './context/NotificationContext';
import ServiceDetailsContext from './context/ServiceDetailsContext';

const App = () => {
  return (
    <ServiceDetailsContext>
    <NotificationContext>
      <Routes>
        <Route path='/' element = {<User/>}/>
        <Route path='/user' element = {<User/>}/>
        <Route path='/service' element = {<Service/>}/>
        <Route path='/welcome' element = {<Welcome/>}/>
      </Routes>
    </NotificationContext>
    </ServiceDetailsContext>
  );
};

export default App;