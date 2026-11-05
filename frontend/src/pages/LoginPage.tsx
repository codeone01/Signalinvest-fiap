import { FormEvent, useEffect, useState } from 'react'
import { Navigate, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { ErroApi } from '../api'
import { Marca } from '../components/Marca'
import { Mensagem } from '../components/Mensagem'

export function LoginPage() {
  const { usuario, carregando, entrar } = useAuth()
  const [email, setEmail] = useState('')
  const [senha, setSenha] = useState('')
  const [erro, setErro] = useState('')
  const [enviando, setEnviando] = useState(false)
  const navegar = useNavigate()
  const local = useLocation()

  useEffect(() => {
    document.title = 'Entrar | Signalinvest'
  }, [])

  if (!carregando && usuario) return <Navigate to="/" replace />

  const submeter = async (evento: FormEvent) => {
    evento.preventDefault()
    setErro('')
    setEnviando(true)
    try {
      await entrar(email, senha)
      const destino = (local.state as { de?: string } | null)?.de ?? '/'
      navegar(destino, { replace: true })
    } catch (falha) {
      setErro(falha instanceof ErroApi ? falha.message : 'Não foi possível entrar.')
    } finally {
      setEnviando(false)
    }
  }

  return (
    <main className="login">
      <section className="login-apresentacao">
        <Marca clara />
        <div>
          <span className="sobrelinha">Gestão financeira com clareza</span>
          <h1>Suas contas e lançamentos em uma visão simples.</h1>
          <p>Acompanhe receitas, despesas e o resultado real de cada movimentação sem alterar o saldo cadastrado das contas.</p>
        </div>
        <div className="login-beneficios">
          <span>✓ Dados separados por usuário</span>
          <span>✓ Persistência na Oracle FIAP</span>
          <span>✓ Controle de ponta a ponta</span>
        </div>
      </section>
      <section className="login-formulario-area">
        <form className="cartao login-formulario" onSubmit={submeter}>
          <div className="login-marca-mobile"><Marca /></div>
          <span className="etiqueta">Área do investidor</span>
          <h2>Acessar sua conta</h2>
          <p>Use o usuário demonstrativo preparado no Oracle ou suas credenciais compatíveis com PBKDF2.</p>
          {erro && <Mensagem tipo="erro" texto={erro} aoFechar={() => setErro('')} />}
          <label>
            E-mail
            <input type="email" autoComplete="username" value={email}
              onChange={(e) => setEmail(e.target.value)} required autoFocus />
          </label>
          <label>
            Senha
            <input type="password" autoComplete="current-password" value={senha}
              onChange={(e) => setSenha(e.target.value)} required />
          </label>
          <button className="botao botao-primario botao-cheio" disabled={enviando}>
            {enviando ? 'Entrando...' : 'Entrar'}
          </button>
          <small>As credenciais Oracle nunca são enviadas ao navegador.</small>
        </form>
      </section>
    </main>
  )
}
