import React, { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Sidebar from './components/Sidebar';
import Header from './components/Header';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Clientes from './pages/Clientes';
import Oportunidades from './pages/Oportunidades';
import NaoEncontrado from './pages/NaoEncontrado';

function LayoutPrivado({ children }) {
  const { usuario, carregando } = useAuth();
  const [sidebarAberta, setSidebarAberta] = useState(false);
  const toggleSidebar = () => setSidebarAberta(!sidebarAberta);

  if (carregando) return <div style={{ padding: '20px' }}>Carregando...</div>;
  if (!usuario) return <Navigate to="/login" />;

  return (
    <div style={{ display: 'flex', minHeight: '100vh', width: '100vw', overflowX: 'hidden' }}>
      <Sidebar isOpen={sidebarAberta} toggleSidebar={toggleSidebar} />

      <div style={{
        flex: 1,
        display: 'flex',
        flexDirection: 'column',
        minWidth: 0,
        backgroundColor: '#f4f7f6'
      }}>
        <Header toggleSidebar={toggleSidebar} />

        <main style={{
          marginTop: '60px',
          padding: '20px',
          flex: 1,
          width: '100%',
          boxSizing: 'border-box'
        }}>
          {children}
        </main>
      </div>
    </div>
  );
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={
            <LayoutPrivado><Dashboard /></LayoutPrivado>
          } />
          <Route path="/dashboard" element={
            <LayoutPrivado><Dashboard /></LayoutPrivado>
          } />
          <Route path="/clientes" element={
            <LayoutPrivado><Clientes /></LayoutPrivado>
          } />
          <Route path="/oportunidades" element={
            <LayoutPrivado><Oportunidades /></LayoutPrivado>
          } />
          <Route path="*" element={<NaoEncontrado />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;