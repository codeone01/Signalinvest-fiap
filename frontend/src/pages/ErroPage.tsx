import { Link } from 'react-router-dom'
import { Marca } from '../components/Marca'

export function ErroPage() {
  return <main className="pagina-erro"><Marca /><span>404</span><h1>Página não encontrada</h1><p>O endereço acessado não existe ou foi movido.</p><Link className="botao botao-primario" to="/">Voltar ao painel</Link></main>
}
