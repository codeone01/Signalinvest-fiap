public class Main {
    public static void main(String[] args) {
        Investidor investidor = new Investidor(1, "Eduardo", "eduardo@email.com", "Moderado", 10000.0);
        Startup startup = new Startup(1, "TechStart", "Tecnologia", "Seed", 1500000.0, 8.7, true);
        AvaliacaoStartup avaliacao = new AvaliacaoStartup(1, 1, 8.5, 9.0, 8.0, 8.7, 8.55);
        Investimento investimento = new Investimento(1, 1, 1, 5000.0, "30/08/2026", "Registrado");
        ComparacaoStartup comparacao = new ComparacaoStartup(1, 1, "Comparação Tecnologia", 1, 2, "TechStart");
        RodadaInvestimento rodada = new RodadaInvestimento(1, 1, "Seed 2026", 500000.0, 250000.0, "Aberta");

        investidor.analisarStartup();
        startup.calcularScore();
        avaliacao.calcularNotaFinal();
        investimento.registrarInvestimento();
        comparacao.compararStartups();
        rodada.registrarAporte();
    }
}
