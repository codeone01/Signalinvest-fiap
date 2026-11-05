import type { ApiErro } from './types'

export class ErroApi extends Error {
  status: number

  constructor(status: number, mensagem: string) {
    super(mensagem)
    this.status = status
  }
}

export async function api<T>(caminho: string, opcoes: RequestInit = {}): Promise<T> {
  const resposta = await fetch(caminho, {
    ...opcoes,
    credentials: 'include',
    headers: {
      ...(opcoes.body ? { 'Content-Type': 'application/json' } : {}),
      ...opcoes.headers,
    },
  })

  if (!resposta.ok) {
    let detalhe: ApiErro = {}
    try {
      detalhe = await resposta.json() as ApiErro
    } catch {
      detalhe = {}
    }
    if (resposta.status === 401 && caminho !== '/api/auth/login') {
      window.dispatchEvent(new Event('signalinvest:sessao-expirada'))
    }
    throw new ErroApi(resposta.status, detalhe.mensagem ?? 'Não foi possível concluir a operação.')
  }

  if (resposta.status === 204) {
    return undefined as T
  }
  return resposta.json() as Promise<T>
}

export const moeda = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
})

export const dataBrasileira = (data: string) =>
  new Intl.DateTimeFormat('pt-BR', { timeZone: 'UTC' }).format(new Date(`${data}T00:00:00Z`))
