import { createContext, useContext, useState, useEffect } from 'react';

// Contexto global de autenticação — qualquer componente pode acessar
const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [usuario, setUsuario] = useState(null);
    const [token, setToken] = useState(null);
    const [carregando, setCarregando] = useState(true);

    // Ao iniciar, verifica se já tem sessão salva no localStorage
    useEffect(() => {
        const tokenSalvo = localStorage.getItem('token');
        const usuarioSalvo = localStorage.getItem('usuario');
        if (tokenSalvo && usuarioSalvo) {
            setToken(tokenSalvo);
            setUsuario(JSON.parse(usuarioSalvo));
        }
        setCarregando(false);
    }, []);

    // Chamado após login bem sucedido
    function login(dadosUsuario, dadosToken) {
        setToken(dadosToken);
        setUsuario(dadosUsuario);
        localStorage.setItem('token', dadosToken);
        localStorage.setItem('usuario', JSON.stringify(dadosUsuario));
    }

    // Chamado ao clicar em sair
    function logout() {
        setToken(null);
        setUsuario(null);
        localStorage.removeItem('token');
        localStorage.removeItem('usuario');
    }

    return (
        <AuthContext.Provider value={{ usuario, token, login, logout, carregando }}>
            {children}
        </AuthContext.Provider>
    );
}

// Hook personalizado para acessar o contexto facilmente
export function useAuth() {
    return useContext(AuthContext);
}