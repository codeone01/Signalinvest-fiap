import { FormEvent, useCallback, useEffect, useState } from 'react'
import { api, ErroApi, moeda } from '../api'
import { Carregando } from '../components/Carregando'
import { Mensagem } from '../components/Mensagem'
import type { Conta, ContaEntrada } from '../types'

const inicial: ContaEntrada = { banco: '', agencia: '', numero: '', tipo: 'CORRENTE', saldo: 0 }

export function ContasPage() {
  const [contas, setContas] = useState<Conta[]>([])
  const [formulario, setFormulario] = useState<ContaEntrada>(inicial)
  const [editando, setEditando] = useState<number | null>(null)
  const [detalhe, setDetalhe] = useState<Conta | null>(null)
  const [formAberto, setFormAberto] = useState(false)
  const [carregando, setCarregando] = useState(true)
  const [salvando, setSalvando] = useState(false)
  const [mensagem, setMensagem] = useState<{ tipo: 'erro' | 'sucesso'; texto: string } | null>(null)

  const carregar = useCallback(async () => {
    setCarregando(true)
    try {
      setContas(await api<Conta[]>('/api/contas'))
    } catch (erro) {
      setMensagem({ tipo: 'erro', texto: erro instanceof ErroApi ? erro.message : 'Não foi possível carregar as contas.' })
    } finally {
      setCarregando(false)
    }
  }, [])

  useEffect(() => {
    document.title = 'Contas bancárias | Signalinvest'
    void carregar()
  }, [carregar])

  const nova = () => {
    setEditando(null)
    setFormulario(inicial)
    setFormAberto(true)
  }

  const editar = async (id: number) => {
    try {
      const conta = await api<Conta>(`/api/contas/${id}`)
      setEditando(id)
      setFormulario({ banco: conta.banco, agencia: conta.agencia, numero: conta.numero, tipo: conta.tipo, saldo: conta.saldo })
      setFormAberto(true)
    } catch (erro) {
      setMensagem({ tipo: 'erro', texto: erro instanceof ErroApi ? erro.message : 'Não foi possível consultar a conta.' })
    }
  }

  const ver = async (id: number) => {
    try {
      setDetalhe(await api<Conta>(`/api/contas/${id}`))
    } catch (erro) {
      setMensagem({ tipo: 'erro', texto: erro instanceof ErroApi ? erro.message : 'Não foi possível consultar a conta.' })
    }
  }

  const salvar = async (evento: FormEvent) => {
    evento.preventDefault()
    setSalvando(true)
    setMensagem(null)
    try {
      if (editando) {
        await api<Conta>(`/api/contas/${editando}`, { method: 'PUT', body: JSON.stringify(formulario) })
      } else {
        await api<Conta>('/api/contas', { method: 'POST', body: JSON.stringify(formulario) })
      }
      setFormAberto(false)
      setMensagem({ tipo: 'sucesso', texto: editando ? 'Conta atualizada com sucesso.' : 'Conta criada com sucesso.' })
      await carregar()
    } catch (erro) {
      setMensagem({ tipo: 'erro', texto: erro instanceof ErroApi ? erro.message : 'Não foi possível salvar a conta.' })
    } finally {
      setSalvando(false)
    }
  }

  const excluir = async (conta: Conta) => {
    if (!window.confirm(`Excluir a conta ${conta.banco} • ${conta.numero}?`)) return
    try {
      await api<void>(`/api/contas/${conta.id}`, { method: 'DELETE' })
      setMensagem({ tipo: 'sucesso', texto: 'Conta excluída com sucesso.' })
      await carregar()
    } catch (erro) {
      setMensagem({ tipo: 'erro', texto: erro instanceof ErroApi ? erro.message : 'Não foi possível excluir a conta.' })
    }
  }

  return (
    <>
      <header className="cabecalho-pagina"><div><span className="sobrelinha">Seu patrimônio</span><h1>Contas bancárias</h1><p>Cadastre o saldo informado por você. Lançamentos não alteram esse valor.</p></div><button className="botao botao-primario" type="button" onClick={nova}>+ Nova conta</button></header>
      {mensagem && <Mensagem {...mensagem} aoFechar={() => setMensagem(null)} />}
      {formAberto && (
        <form className="cartao formulario" onSubmit={salvar}>
          <div className="formulario-titulo"><div><span className="etiqueta">{editando ? 'Edição' : 'Cadastro'}</span><h2>{editando ? 'Editar conta' : 'Nova conta bancária'}</h2></div><button className="botao-icone" type="button" onClick={() => setFormAberto(false)} aria-label="Fechar formulário">×</button></div>
          <div className="grade-formulario">
            <label>Banco<input value={formulario.banco} maxLength={100} onChange={(e) => setFormulario({ ...formulario, banco: e.target.value })} required /></label>
            <label>Agência<input value={formulario.agencia} maxLength={20} onChange={(e) => setFormulario({ ...formulario, agencia: e.target.value })} required /></label>
            <label>Número da conta<input value={formulario.numero} maxLength={30} onChange={(e) => setFormulario({ ...formulario, numero: e.target.value })} required /></label>
            <label>Tipo<select value={formulario.tipo} onChange={(e) => setFormulario({ ...formulario, tipo: e.target.value })}><option>CORRENTE</option><option>POUPANCA</option><option>INVESTIMENTO</option><option>CARTEIRA_DIGITAL</option></select></label>
            <label>Saldo cadastrado<input type="number" step="0.01" value={formulario.saldo} onChange={(e) => setFormulario({ ...formulario, saldo: Number(e.target.value) })} required /></label>
          </div>
          <div className="acoes-formulario"><button className="botao botao-secundario" type="button" onClick={() => setFormAberto(false)}>Cancelar</button><button className="botao botao-primario" disabled={salvando}>{salvando ? 'Salvando...' : 'Salvar conta'}</button></div>
        </form>
      )}
      {carregando ? <Carregando texto="Carregando contas..." /> : contas.length === 0 ? (
        <div className="cartao estado-vazio"><span>▣</span><h2>Nenhuma conta cadastrada</h2><p>Crie uma conta para começar a registrar receitas e despesas.</p><button className="botao botao-primario" type="button" onClick={nova}>Cadastrar primeira conta</button></div>
      ) : (
        <section className="grade-contas">
          {contas.map((conta) => <article className="cartao conta-card" key={conta.id}><div className="conta-topo"><span className="conta-icone">▣</span><span className="chip">{conta.tipo.replace('_', ' ')}</span></div><h2>{conta.banco}</h2><p>Ag. {conta.agencia} • Conta {conta.numero}</p><div className="saldo"><span>Saldo cadastrado</span><strong>{moeda.format(conta.saldo)}</strong></div><div className="acoes-linha"><button type="button" onClick={() => void ver(conta.id)}>Ver</button><button type="button" onClick={() => void editar(conta.id)}>Editar</button><button className="perigo" type="button" onClick={() => void excluir(conta)}>Excluir</button></div></article>)}
        </section>
      )}
      {detalhe && <div className="modal-fundo" role="presentation" onMouseDown={() => setDetalhe(null)}><article className="cartao modal" role="dialog" aria-modal="true" aria-labelledby="detalhe-conta" onMouseDown={(e) => e.stopPropagation()}><button className="botao-icone modal-fechar" type="button" onClick={() => setDetalhe(null)} aria-label="Fechar">×</button><span className="etiqueta">Conta #{detalhe.id}</span><h2 id="detalhe-conta">{detalhe.banco}</h2><dl><div><dt>Agência</dt><dd>{detalhe.agencia}</dd></div><div><dt>Número</dt><dd>{detalhe.numero}</dd></div><div><dt>Tipo</dt><dd>{detalhe.tipo}</dd></div><div><dt>Saldo cadastrado</dt><dd>{moeda.format(detalhe.saldo)}</dd></div></dl></article></div>}
    </>
  )
}
