import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

// Adicionamos { isOpen, toggleSidebar } para receber os dados do App.js
const Sidebar = ({ isOpen, toggleSidebar }) => {
    const navigate = useNavigate();

    const logout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    const styles = {
        sidebar: {
            width: '250px',
            height: '100vh',
            backgroundColor: '#1a1a2e',
            color: 'white',
            display: 'flex',
            flexDirection: 'column',
            padding: '20px',
            position: 'fixed',
            top: 0,
            // AQUI ESTÁ A MÁGICA: 
            // Se isOpen for true, fica em 0 (visível). Se false, -250px (escondido).
            left: isOpen ? '0' : '-250px',
            transition: '0.3s ease', // Animação suave para deslizar
            zIndex: 1000,
            boxSizing: 'border-box'
        },
        logo: {
            fontSize: '24px',
            fontWeight: 'bold',
            marginBottom: '40px',
            textAlign: 'center',
            color: '#4e54c8'
        },
        navLink: {
            color: '#bdc3c7',
            textDecoration: 'none',
            padding: '15px 10px',
            fontSize: '18px',
            display: 'block',
            borderRadius: '5px',
            transition: '0.3s',
            marginBottom: '10px'
        },
        logoutBtn: {
            marginTop: 'auto',
            padding: '10px',
            backgroundColor: '#e74c3c',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
        },
        // Camada escura para fechar o menu ao clicar fora (opcional, mas recomendado)
        overlay: {
            display: isOpen ? 'block' : 'none',
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100vw',
            height: '100vh',
            backgroundColor: 'rgba(0,0,0,0.5)',
            zIndex: 999
        }
    };

    return (
        <>
            {/* Overlay: se clicar aqui, o menu fecha */}
            <div style={styles.overlay} onClick={toggleSidebar}></div>

            <div style={styles.sidebar}>
                <div style={styles.logo}>CRM PRO</div>

                <nav>
                    {/* onClick={toggleSidebar} faz o menu fechar quando você escolhe uma página */}
                    <Link to="/dashboard" style={styles.navLink} onClick={toggleSidebar}>📊 Dashboard</Link>
                    <Link to="/clientes" style={styles.navLink} onClick={toggleSidebar}>👥 Clientes</Link>
                    <Link to="/oportunidades" style={styles.navLink} onClick={toggleSidebar}>💰 Oportunidades</Link>
                </nav>

                <button onClick={logout} style={styles.logoutBtn}>Sair</button>
            </div>
        </>
    );
};

export default Sidebar;