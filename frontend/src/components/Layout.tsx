import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useState } from 'react'
import { useAuth } from '../auth/AuthContext'
import { Marca } from './Marca'

const itens = [
  { destino: '/', rotulo: 'Início', icone: '⌂' },
  { destino: '/contas', rotulo: 'Contas', icone: '▣' },
  { destino: '/receitas', rotulo: 'Receitas', icone: '↗' },
  { destino: '/despesas', rotulo: 'Despesas', icone: '↘' },
]

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
        <Marca />
        <nav aria-label="Navegação principal">
          {itens.map((item) => (
            <NavLink key={item.destino} to={item.destino} end={item.destino === '/'}
              onClick={() => setMenuAberto(false)}>
              <span aria-hidden="true">{item.icone}</span>{item.rotulo}
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
