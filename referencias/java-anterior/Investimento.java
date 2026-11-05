public class Investimento {
    private Integer id;
    private Integer investidorId;
    private Integer startupId;
    private Double valor;
    private String dataInvestimento;
    private String status;

    public Investimento() {
    }

    public Investimento(Integer id, Integer investidorId, Integer startupId, Double valor, String dataInvestimento, String status) {
        this.id = id;
        this.investidorId = investidorId;
        this.startupId = startupId;
        this.valor = valor;
        this.dataInvestimento = dataInvestimento;
        this.status = status;
    }

    public void registrarInvestimento() {
        System.out.println("Executando o método registrarInvestimento do investimento: " + id);
    }

    public void atualizarStatus() {
        System.out.println("Executando o método atualizarStatus do investimento: " + id);
    }

    public void consultarInvestimento() {
        System.out.println("Executando o método consultarInvestimento do investimento: " + id);
    }
}
