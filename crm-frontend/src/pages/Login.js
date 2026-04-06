import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

export default function Login() {
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [erro, setErro] = useState('');
    const [carregando, setCarregando] = useState(false);

    const { login } = useAuth();
    const navigate = useNavigate();

    async function handleSubmit(e) {
        e.preventDefault();
        setErro('');
        setCarregando(true);

        try {
            const resposta = await api.post('/api/auth/login', { email, senha });
            const { token, nome, role } = resposta.data;
            login({ nome, email, role }, token);
            navigate('/');
        } catch (err) {
            // Adicione essas duas linhas:
            console.log('Erro completo:', err);
            console.log('Response:', err.response);
            setErro('Email ou senha incorretos. Tente novamente.');
        } finally {
            setCarregando(false);
        }
    }

    const styles = {
        container: { height: '100vh', width: '100vw', display: 'flex', justifyContent: 'center', alignItems: 'center', backgroundColor: '#1a1a2e' },
        card: { backgroundColor: 'white', padding: '40px', borderRadius: '15px', width: '100%', maxWidth: '400px', boxShadow: '0 10px 25px rgba(0,0,0,0.3)', textAlign: 'center' },
        input: { width: '100%', padding: '12px', margin: '10px 0', border: '1px solid #ddd', borderRadius: '8px', boxSizing: 'border-box' },
        button: { width: '100%', padding: '12px', backgroundColor: '#4e54c8', color: 'white', border: 'none', borderRadius: '8px', cursor: 'pointer', fontWeight: 'bold', fontSize: '16px', marginTop: '10px' }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <div style={{ backgroundColor: '#4e54c8', color: 'white', padding: '10px 20px', borderRadius: '8px', display: 'inline-block', fontWeight: 'bold', marginBottom: '20px' }}>CRM</div>
                <h2 style={{ color: '#333', marginBottom: '30px' }}>Bem-vindo de volta</h2>
                <form onSubmit={handleSubmit}>
                    <input style={styles.input} type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                    <input style={styles.input} type="password" placeholder="Senha" value={senha} onChange={(e) => setSenha(e.target.value)} required />
                    {erro && <div style={{ color: '#e74c3c', margin: '10px 0', fontSize: '14px' }}>{erro}</div>}
                    <button style={styles.button} type="submit" disabled={carregando}>{carregando ? 'Entrando...' : 'Entrar'}</button>
                </form>
            </div>
        </div>
    );
}