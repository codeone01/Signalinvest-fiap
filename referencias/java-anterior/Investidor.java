public class Investidor {
    private Integer id;
    private String nome;
    private String email;
    private String perfilRisco;
    private Double saldoDisponivel;

    public Investidor() {
    }

    public Investidor(Integer id, String nome, String email, String perfilRisco, Double saldoDisponivel) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfilRisco = perfilRisco;
        this.saldoDisponivel = saldoDisponivel;
    }

    public void analisarStartup() {
        System.out.println("Executando o método analisarStartup para o investidor: " + nome);
    }

    public void realizarInvestimento() {
        System.out.println("Executando o método realizarInvestimento para o investidor: " + nome);
    }

    public void consultarCarteira() {
        System.out.println("Executando o método consultarCarteira para o investidor: " + nome);
    }
}
