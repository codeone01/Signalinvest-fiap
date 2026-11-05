public class AvaliacaoStartup {
    private Integer id;
    private Integer startupId;
    private Double notaMercado;
    private Double notaEquipe;
    private Double notaProduto;
    private Double notaFinanceira;
    private Double scoreFinal;

    public AvaliacaoStartup() {
    }

    public AvaliacaoStartup(Integer id, Integer startupId, Double notaMercado, Double notaEquipe, Double notaProduto, Double notaFinanceira, Double scoreFinal) {
        this.id = id;
        this.startupId = startupId;
        this.notaMercado = notaMercado;
        this.notaEquipe = notaEquipe;
        this.notaProduto = notaProduto;
        this.notaFinanceira = notaFinanceira;
        this.scoreFinal = scoreFinal;
    }

    public void calcularNotaFinal() {
        System.out.println("Executando o método calcularNotaFinal da avaliação: " + id);
    }

    public void exibirResultado() {
        System.out.println("Executando o método exibirResultado da avaliação: " + id);
    }
}
