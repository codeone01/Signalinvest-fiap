public class RodadaInvestimento {
    private Integer id;
    private Integer startupId;
    private String nomeRodada;
    private Double metaCaptacao;
    private Double valorCaptado;
    private String status;

    public RodadaInvestimento() {
    }

    public RodadaInvestimento(Integer id, Integer startupId, String nomeRodada, Double metaCaptacao, Double valorCaptado, String status) {
        this.id = id;
        this.startupId = startupId;
        this.nomeRodada = nomeRodada;
        this.metaCaptacao = metaCaptacao;
        this.valorCaptado = valorCaptado;
        this.status = status;
    }

    public void abrirRodada() {
        System.out.println("Executando o método abrirRodada: " + nomeRodada);
    }

    public void registrarAporte() {
        System.out.println("Executando o método registrarAporte da rodada: " + nomeRodada);
    }

    public void encerrarRodada() {
        System.out.println("Executando o método encerrarRodada: " + nomeRodada);
    }
}
