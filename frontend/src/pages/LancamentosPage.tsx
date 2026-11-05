import { FormEvent, useCallback, useEffect, useMemo, useState } from 'react'
import { api, dataBrasileira, ErroApi, moeda } from '../api'
import { Carregando } from '../components/Carregando'
import { Mensagem } from '../components/Mensagem'
import type { Conta, Lancamento, LancamentoEntrada } from '../types'

type Props = {
  recurso: 'receitas' | 'despesas'
  titulo: string
  singular: string
  descricao: string
  tipos: string[]
}

const hoje = () => new Date().toISOString().slice(0, 10)

export function LancamentosPage({ recurso, titulo, singular, descricao, tipos }: Props) {
  const entradaInicial = useMemo<LancamentoEntrada>(() => ({ contaId: 0, descricao: '', valor: 0, data: hoje(), tipo: tipos[0] }), [tipos])
  const [itens, setItens] = useState<Lancamento[]>([])
  const [contas, setContas] = useState<Conta[]>([])
  const [formulario, setFormulario] = useState<LancamentoEntrada>(entradaInicial)
  const [editando, setEditando] = useState<number | null>(null)
  const [detalhe, setDetalhe] = useState<Lancamento | null>(null)
  const [formAberto, setFormAberto] = useState(false)
  const [carregando, setCarregando] = useState(true)
  const [salvando, setSalvando] = useState(false)
  const [mensagem, setMensagem] = useState<{ tipo: 'erro' | 'sucesso'; texto: string } | null>(null)

  const carregar = useCallback(async () => {
    setCarregando(true)
    try {
      const [lancamentos, contasUsuario] = await Promise.all([
        api<Lancamento[]>(`/api/${recurso}`), api<Conta[]>('/api/contas'),
      ])
      setItens(lancamentos)
      setContas(contasUsuario)
    } catch (erro) {
      setMensagem({ tipo: 'erro', texto: erro instanceof ErroApi ? erro.message : `Não foi possível carregar ${titulo.toLowerCase()}.` })
    } finally {
      setCarregando(false)
    }
  }, [recurso, titulo])

  useEffect(() => {
    document.title = `${titulo} | Signalinvest`
    void carregar()
  }, [carregar, titulo])

  const novo = () => {
    if (contas.length === 0) {
      setMensagem({ tipo: 'erro', texto: `Cadastre uma conta bancária antes de criar ${singular.toLowerCase()}.` })
      return
    }
    setEditando(null)
    setFormulario({ ...entradaInicial, contaId: contas[0].id })
    setFormAberto(true)
  }

  const consultar = async (id: number, modo: 'ver' | 'editar') => {
    try {
      const item = await api<Lancamento>(`/api/${recurso}/${id}`)
      if (modo === 'ver') setDetalhe(item)
      else {
        setEditando(id)
        setFormulario({ contaId: item.contaId, descricao: item.descricao, valor: item.valor, data: item.data, tipo: item.tipo })
        setFormAberto(true)
      }
    } catch (erro) {
      setMensagem({ tipo: 'erro', texto: erro instanceof ErroApi ? erro.message : `Não foi possível consultar ${singular.toLowerCase()}.` })
    }
  }

  const salvar = async (evento: FormEvent) => {
    evento.preventDefault()
    setSalvando(true)
    setMensagem(null)
    try {
      if (editando) await api<Lancamento>(`/api/${recurso}/${editando}`, { method: 'PUT', body: JSON.stringify(formulario) })
      else await api<Lancamento>(`/api/${recurso}`, { method: 'POST', body: JSON.stringify(formulario) })
      setFormAberto(false)
      setMensagem({ tipo: 'sucesso', texto: `${singular} ${editando ? 'atualizada' : 'criada'} com sucesso.` })
      await carregar()
    } catch (erro) {
      setMensagem({ tipo: 'erro', texto: erro instanceof ErroApi ? erro.message : `Não foi possível salvar ${singular.toLowerCase()}.` })
    } finally {
      setSalvando(false)
    }
  }

  const excluir = async (item: Lancamento) => {
    if (!window.confirm(`Excluir ${singular.toLowerCase()} "${item.descricao}"?`)) return
    try {
      await api<void>(`/api/${recurso}/${item.id}`, { method: 'DELETE' })
      setMensagem({ tipo: 'sucesso', texto: `${singular} excluída com sucesso.` })
      await carregar()
    } catch (erro) {
      setMensagem({ tipo: 'erro', texto: erro instanceof ErroApi ? erro.message : `Não foi possível excluir ${singular.toLowerCase()}.` })
    }
  }

  const total = itens.reduce((soma, item) => soma + Number(item.valor), 0)
  const classe = recurso === 'receitas' ? 'entrada' : 'saida'

  return (
    <>
      <header className="cabecalho-pagina"><div><span className="sobrelinha">Lançamentos</span><h1>{titulo}</h1><p>{descricao}</p></div><button className="botao botao-primario" type="button" onClick={novo}>+ Nova {singular.toLowerCase()}</button></header>
      {mensagem && <Mensagem {...mensagem} aoFechar={() => setMensagem(null)} />}
      <article className={`cartao resumo-lista ${classe}`}><div><span>Total registrado</span><strong>{moeda.format(total)}</strong></div><span>{itens.length} {itens.length === 1 ? 'lançamento' : 'lançamentos'}</span></article>
      {formAberto && <form className="cartao formulario" onSubmit={salvar}><div className="formulario-titulo"><div><span className="etiqueta">{editando ? 'Edição' : 'Cadastro'}</span><h2>{editando ? `Editar ${singular.toLowerCase()}` : `Nova ${singular.toLowerCase()}`}</h2></div><button className="botao-icone" type="button" onClick={() => setFormAberto(false)} aria-label="Fechar formulário">×</button></div><div className="grade-formulario">
        <label>Conta bancária<select value={formulario.contaId} onChange={(e) => setFormulario({ ...formulario, contaId: Number(e.target.value) })} required>{contas.map((conta) => <option value={conta.id} key={conta.id}>{conta.banco} • {conta.numero}</option>)}</select></label>
        <label>Descrição<input value={formulario.descricao} maxLength={200} onChange={(e) => setFormulario({ ...formulario, descricao: e.target.value })} required /></label>
        <label>Valor<input type="number" min="0.01" step="0.01" value={formulario.valor || ''} onChange={(e) => setFormulario({ ...formulario, valor: Number(e.target.value) })} required /></label>
        <label>Data<input type="date" value={formulario.data} onChange={(e) => setFormulario({ ...formulario, data: e.target.value })} required /></label>
        <label>Tipo<select value={formulario.tipo} onChange={(e) => setFormulario({ ...formulario, tipo: e.target.value })}>{tipos.map((tipo) => <option key={tipo}>{tipo}</option>)}</select></label>
      </div><div className="acoes-formulario"><button className="botao botao-secundario" type="button" onClick={() => setFormAberto(false)}>Cancelar</button><button className="botao botao-primario" disabled={salvando}>{salvando ? 'Salvando...' : `Salvar ${singular.toLowerCase()}`}</button></div></form>}
      {carregando ? <Carregando texto={`Carregando ${titulo.toLowerCase()}...`} /> : itens.length === 0 ? <div className="cartao estado-vazio"><span>{recurso === 'receitas' ? '↗' : '↘'}</span><h2>Nenhum lançamento</h2><p>Use o botão acima para cadastrar {singular.toLowerCase()}.</p><button className="botao botao-primario" type="button" onClick={novo}>Cadastrar {singular.toLowerCase()}</button></div> : <div className="cartao tabela-container"><table><thead><tr><th>Descrição</th><th>Conta</th><th>Data</th><th>Tipo</th><th>Valor</th><th><span className="sr-only">Ações</span></th></tr></thead><tbody>{itens.map((item) => <tr key={item.id}><td><strong>{item.descricao}</strong><small>#{item.id}</small></td><td>{item.conta}</td><td>{dataBrasileira(item.data)}</td><td><span className="chip">{item.tipo.replaceAll('_', ' ')}</span></td><td className={`valor-${classe}`}>{moeda.format(item.valor)}</td><td><div className="acoes-tabela"><button type="button" onClick={() => void consultar(item.id, 'ver')}>Ver</button><button type="button" onClick={() => void consultar(item.id, 'editar')}>Editar</button><button className="perigo" type="button" onClick={() => void excluir(item)}>Excluir</button></div></td></tr>)}</tbody></table></div>}
      {detalhe && <div className="modal-fundo" role="presentation" onMouseDown={() => setDetalhe(null)}><article className="cartao modal" role="dialog" aria-modal="true" aria-labelledby="detalhe-lancamento" onMouseDown={(e) => e.stopPropagation()}><button className="botao-icone modal-fechar" type="button" onClick={() => setDetalhe(null)} aria-label="Fechar">×</button><span className="etiqueta">{singular} #{detalhe.id}</span><h2 id="detalhe-lancamento">{detalhe.descricao}</h2><dl><div><dt>Conta</dt><dd>{detalhe.conta}</dd></div><div><dt>Data</dt><dd>{dataBrasileira(detalhe.data)}</dd></div><div><dt>Tipo</dt><dd>{detalhe.tipo}</dd></div><div><dt>Valor</dt><dd>{moeda.format(detalhe.valor)}</dd></div></dl></article></div>}
    </>
  )
}
