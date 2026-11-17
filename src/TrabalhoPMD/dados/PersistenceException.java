package TrabalhoPMD.dados;

public class PersistenceException extends Exception {

    private String operacao; //Salvar, atualizar, apagar, buscar etc.
    private String problema; //	Descrição do erro ocorrido.
    private Object valor; //Objeto ou ID que provocou o erro.

    public PersistenceException(
            String operacao,
            String problema,
            Object valor) {

        super("Operação: " + operacao
                + "\nProblema: " + problema
                + "\nValor: " + valor);

        this.operacao = operacao;
        this.problema = problema;
        this.valor = valor;
    }

    public PersistenceException(
            String operacao,
            String problema,
            Object valor,
            Throwable causa) {

        super("Operação: " + operacao
                + "\nProblema: " + problema
                + "\nValor: " + valor, causa);

        this.operacao = operacao;
        this.problema = problema;
        this.valor = valor;
    }

    public String getOperacao() {
        return operacao;
    }

    public String getProblema() {
        return problema;
    }

    public Object getValor() {
        return valor;
    }
}