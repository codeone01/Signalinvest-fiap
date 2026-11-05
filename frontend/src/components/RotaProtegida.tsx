import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { Carregando } from './Carregando'

export function RotaProtegida() {
  const { usuario, carregando } = useAuth()
  const local = useLocation()
  if (carregando) return <main className="centro-tela"><Carregando texto="Conferindo sua sessão..." /></main>
  if (!usuario) return <Navigate to="/login" replace state={{ de: local.pathname }} />
  return <Outlet />
}
