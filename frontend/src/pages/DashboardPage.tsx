import { useCallback, useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api, ErroApi, moeda } from '../api'
import { Carregando } from '../components/Carregando'
import { Mensagem } from '../components/Mensagem'
import type { Painel } from '../types'

export function DashboardPage() {
  const [painel, setPainel] = useState<Painel | null>(null)
  const [erro, setErro] = useState('')
  const [carregando, setCarregando] = useState(true)

  const carregar = useCallback(async () => {
    setCarregando(true)
    setErro('')
    try {
      setPainel(await api<Painel>('/api/painel'))
    } catch (falha) {
      setErro(falha instanceof ErroApi ? falha.message : 'Não foi possível carregar o painel.')
    } finally {
      setCarregando(false)
    }
  }, [])

  useEffect(() => {
    document.title = 'Início | Signalinvest'
    void carregar()
  }, [carregar])

  return (
    <>
      <header className="cabecalho-pagina">
        <div><span className="sobrelinha">Visão geral</span><h1>Painel financeiro</h1><p>Valores calculados somente com os dados do usuário autenticado.</p></div>
        <button className="botao botao-secundario" type="button" onClick={() => void carregar()}>Atualizar</button>
      </header>
      {erro && <Mensagem tipo="erro" texto={erro} aoFechar={() => setErro('')} />}
      {carregando ? <Carregando texto="Calculando seus totais..." /> : painel && (
        <>
          <section className="grade-metricas" aria-label="Resumo financeiro">
            <article className="cartao metrica roxa"><span>Saldo cadastrado</span><strong>{moeda.format(painel.saldoCadastrado)}</strong><small>Soma dos saldos informados nas contas</small></article>
            <article className="cartao metrica verde"><span>Total de receitas</span><strong>{moeda.format(painel.totalReceitas)}</strong><small>Entradas registradas</small></article>
            <article className="cartao metrica vermelha"><span>Total de despesas</span><strong>{moeda.format(painel.totalDespesas)}</strong><small>Saídas registradas</small></article>
            <article className="cartao metrica azul"><span>Resultado dos lançamentos</span><strong>{moeda.format(painel.resultadoLancamentos)}</strong><small>Receitas menos despesas</small></article>
          </section>
          <section className="grade-dashboard">
            <article className="cartao destaque-painel">
              <div><span className="etiqueta">Contas conectadas</span><h2>{painel.quantidadeContas} {painel.quantidadeContas === 1 ? 'conta cadastrada' : 'contas cadastradas'}</h2><p>O saldo cadastrado é independente dos lançamentos e pode ser ajustado na edição da conta.</p></div>
              <Link className="botao botao-primario" to="/contas">Gerenciar contas</Link>
            </article>
            <article className="cartao atalhos">
              <h2>Ações rápidas</h2>
              <Link to="/receitas"><span className="atalho-icone verde">↗</span><div><strong>Registrar receita</strong><small>Adicione uma entrada financeira</small></div><span>›</span></Link>
              <Link to="/despesas"><span className="atalho-icone vermelha">↘</span><div><strong>Registrar despesa</strong><small>Adicione uma saída financeira</small></div><span>›</span></Link>
            </article>
          </section>
        </>
      )}
    </>
  )
}
