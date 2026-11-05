import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { AuthProvider } from './auth/AuthContext'
import { Layout } from './components/Layout'
import { RotaProtegida } from './components/RotaProtegida'
import { ContasPage } from './pages/ContasPage'
import { DashboardPage } from './pages/DashboardPage'
import { ErroPage } from './pages/ErroPage'
import { LancamentosPage } from './pages/LancamentosPage'
import { LoginPage } from './pages/LoginPage'

export default function App() {
  return <BrowserRouter><AuthProvider><Routes>
    <Route path="/login" element={<LoginPage />} />
    <Route element={<RotaProtegida />}>
      <Route element={<Layout />}>
        <Route index element={<DashboardPage />} />
        <Route path="contas" element={<ContasPage />} />
        <Route path="receitas" element={<LancamentosPage recurso="receitas" titulo="Receitas" singular="Receita" descricao="Entradas vinculadas às suas contas, sem alterar o saldo cadastrado." tipos={['SALARIO', 'SERVICOS', 'RENDIMENTOS', 'VENDAS', 'OUTROS']} />} />
        <Route path="despesas" element={<LancamentosPage recurso="despesas" titulo="Despesas" singular="Despesa" descricao="Saídas vinculadas às suas contas, sem alterar o saldo cadastrado." tipos={['MORADIA', 'ALIMENTACAO', 'TRANSPORTE', 'SAUDE', 'LAZER', 'OUTROS']} />} />
      </Route>
    </Route>
    <Route path="*" element={<ErroPage />} />
  </Routes></AuthProvider></BrowserRouter>
}
