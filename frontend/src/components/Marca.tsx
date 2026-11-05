import logo from '../../../referencias/marca/logonome-signalinvest-transp.png'

export function Marca({ clara = false }: { clara?: boolean }) {
  return <img className={`marca ${clara ? 'marca-clara' : ''}`} src={logo} alt="Signalinvest" />
}
