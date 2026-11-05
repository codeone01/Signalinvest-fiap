public class ComparacaoStartup {
    private Integer id;
    private Integer investidorId;
    private String nomeComparacao;
    private Integer primeiraStartupId;
    private Integer segundaStartupId;
    private String melhorStartup;

    public ComparacaoStartup() {
    }

    public ComparacaoStartup(Integer id, Integer investidorId, String nomeComparacao, Integer primeiraStartupId, Integer segundaStartupId, String melhorStartup) {
        this.id = id;
        this.investidorId = investidorId;
        this.nomeComparacao = nomeComparacao;
        this.primeiraStartupId = primeiraStartupId;
        this.segundaStartupId = segundaStartupId;
        this.melhorStartup = melhorStartup;
    }

    public void compararStartups() {
        System.out.println("Executando o método compararStartups da comparação: " + nomeComparacao);
    }

    public void salvarComparacao() {
        System.out.println("Executando o método salvarComparacao da comparação: " + nomeComparacao);
    }
}
