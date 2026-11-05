export type UsuarioSessao = {
  id: number
  nome: string
  email: string
}

export type Conta = {
  id: number
  banco: string
  agencia: string
  numero: string
  tipo: string
  saldo: number
}

export type ContaEntrada = Omit<Conta, 'id'>

export type Lancamento = {
  id: number
  contaId: number
  conta: string
  descricao: string
  valor: number
  data: string
  tipo: string
}

export type LancamentoEntrada = {
  contaId: number
  descricao: string
  valor: number
  data: string
  tipo: string
}

export type Painel = {
  saldoCadastrado: number
  totalReceitas: number
  totalDespesas: number
  resultadoLancamentos: number
  quantidadeContas: number
}

export type ApiErro = {
  status?: number
  mensagem?: string
}
