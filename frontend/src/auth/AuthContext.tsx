import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from 'react'
import { api, ErroApi } from '../api'
import type { UsuarioSessao } from '../types'

type AuthContexto = {
  usuario: UsuarioSessao | null
  carregando: boolean
  entrar: (email: string, senha: string) => Promise<void>
  sair: () => Promise<void>
}

const AuthContext = createContext<AuthContexto | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [usuario, setUsuario] = useState<UsuarioSessao | null>(null)
  const [carregando, setCarregando] = useState(true)

  const conferirSessao = useCallback(async () => {
    try {
      setUsuario(await api<UsuarioSessao>('/api/auth/sessao'))
    } catch (erro) {
      if (!(erro instanceof ErroApi) || erro.status !== 401) {
        console.error(erro)
      }
      setUsuario(null)
    } finally {
      setCarregando(false)
    }
  }, [])

  useEffect(() => {
    void conferirSessao()
  }, [conferirSessao])

  useEffect(() => {
    const expirar = () => setUsuario(null)
    window.addEventListener('signalinvest:sessao-expirada', expirar)
    return () => window.removeEventListener('signalinvest:sessao-expirada', expirar)
  }, [])

  const entrar = async (email: string, senha: string) => {
    const sessao = await api<UsuarioSessao>('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, senha }),
    })
    setUsuario(sessao)
  }

  const sair = async () => {
    await api<void>('/api/auth/logout', { method: 'POST' })
    setUsuario(null)
  }

  const valor = useMemo(() => ({ usuario, carregando, entrar, sair }), [usuario, carregando])
  return <AuthContext.Provider value={valor}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const contexto = useContext(AuthContext)
  if (!contexto) throw new Error('AuthProvider não encontrado')
  return contexto
}
