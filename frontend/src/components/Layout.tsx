import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useState } from 'react'
import { useAuth } from '../auth/AuthContext'
import { Marca } from './Marca'

const itens = [
  { destino: '/', rotulo: 'Início', icone: 'inicio' },
  { destino: '/contas', rotulo: 'Contas', icone: 'contas' },
  { destino: '/receitas', rotulo: 'Receitas', icone: 'receitas' },
  { destino: '/despesas', rotulo: 'Despesas', icone: 'despesas' },
]

function Icone({ nome }: { nome: string }) {
  if (nome === 'inicio') return <svg viewBox="0 0 24 24" aria-hidden="true"><path d="m4 10 8-6 8 6v9a1 1 0 0 1-1 1h-5v-6h-4v6H5a1 1 0 0 1-1-1v-9Z" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinejoin="round" /></svg>
  if (nome === 'contas') return <svg viewBox="0 0 24 24" aria-hidden="true"><rect x="3.5" y="5" width="17" height="14" rx="2" fill="none" stroke="currentColor" strokeWidth="1.8" /><path d="M3.5 9h17M7 14h3" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" /></svg>
  if (nome === 'receitas') return <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M5 17 17.5 4.5M10 4.5h7.5V12" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" /></svg>
  return <svg viewBox="0 0 24 24" aria-hidden="true"><path d="m5 7 12.5 12.5M12 19.5h5.5V14" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" /></svg>
}

export function Layout() {
  const { usuario, sair } = useAuth()
  const navegar = useNavigate()
  const [menuAberto, setMenuAberto] = useState(false)

  const encerrar = async () => {
    await sair()
    navegar('/login')
  }

  return (
    <div className="app-shell">
      <button className="menu-mobile" type="button" onClick={() => setMenuAberto(!menuAberto)}
        aria-expanded={menuAberto} aria-label="Abrir navegação">☰</button>
      <aside className={`sidebar ${menuAberto ? 'sidebar-aberta' : ''}`}>
        <NavLink className="marca-link" to="/" aria-label="Ir para o início" onClick={() => setMenuAberto(false)}>
          <Marca />
        </NavLink>
        <nav aria-label="Navegação principal">
          {itens.map((item) => (
            <NavLink key={item.destino} to={item.destino} end={item.destino === '/'}
              onClick={() => setMenuAberto(false)}>
              <span className="icone-navegacao"><Icone nome={item.icone} /></span>{item.rotulo}
            </NavLink>
          ))}
        </nav>
        <div className="sidebar-usuario">
          <strong>{usuario?.nome}</strong>
          <span>{usuario?.email}</span>
          <button type="button" className="botao botao-secundario" onClick={() => void encerrar()}>Sair</button>
        </div>
      </aside>
      {menuAberto && <button className="menu-fundo" type="button" aria-label="Fechar navegação" onClick={() => setMenuAberto(false)} />}
      <main className="conteudo"><Outlet /></main>
    </div>
  )
}
