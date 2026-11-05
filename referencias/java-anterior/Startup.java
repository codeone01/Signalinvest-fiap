public class Startup {
    private Integer id;
    private String nome;
    private String setor;
    private String estagio;
    private Double valuation;
    private Double score;
    private Boolean rodadaAberta;

    public Startup() {
    }

    public Startup(Integer id, String nome, String setor, String estagio, Double valuation, Double score, Boolean rodadaAberta) {
        this.id = id;
        this.nome = nome;
        this.setor = setor;
        this.estagio = estagio;
        this.valuation = valuation;
        this.score = score;
        this.rodadaAberta = rodadaAberta;
    }

    public void atualizarDados() {
        System.out.println("Executando o método atualizarDados da startup: " + nome);
    }

    public void calcularScore() {
        System.out.println("Executando o método calcularScore da startup: " + nome);
    }

    public void abrirRodadaInvestimento() {
        System.out.println("Executando o método abrirRodadaInvestimento da startup: " + nome);
    }
}
