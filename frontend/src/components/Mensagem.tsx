type Props = {
  tipo: 'erro' | 'sucesso'
  texto: string
  aoFechar?: () => void
}

export function Mensagem({ tipo, texto, aoFechar }: Props) {
  return (
    <div className={`mensagem mensagem-${tipo}`} role={tipo === 'erro' ? 'alert' : 'status'}>
      <span>{texto}</span>
      {aoFechar && <button type="button" onClick={aoFechar} aria-label="Fechar mensagem">×</button>}
    </div>
  )
}
