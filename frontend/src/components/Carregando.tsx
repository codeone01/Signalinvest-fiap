export function Carregando({ texto = 'Carregando...' }: { texto?: string }) {
  return <div className="estado estado-carregando" role="status"><span className="spinner" />{texto}</div>
}
