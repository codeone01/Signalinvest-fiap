package br.com.signalinvest.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.stereotype.Component;

@Component
public class Pbkdf2Senha {
    private static final String PREFIXO = "pbkdf2_sha256";

    public boolean confere(char[] senha, String hashArmazenado) {
        if (senha == null || hashArmazenado == null) {
            return false;
        }
        String[] partes = hashArmazenado.split("\\$", -1);
        if (partes.length != 4 || !PREFIXO.equals(partes[0])) {
            return false;
        }
        try {
            int iteracoes = Integer.parseInt(partes[1]);
            byte[] salt = Base64.getDecoder().decode(partes[2]);
            byte[] esperado = Base64.getDecoder().decode(partes[3]);
            byte[] calculado = derivar(senha, salt, iteracoes, esperado.length);
            return MessageDigest.isEqual(esperado, calculado);
        } catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeySpecException erro) {
            return false;
        } finally {
            java.util.Arrays.fill(senha, '\0');
        }
    }

    private byte[] derivar(char[] senha, byte[] salt, int iteracoes, int tamanho)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec especificacao = new PBEKeySpec(senha, salt, iteracoes, tamanho * 8);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(especificacao).getEncoded();
        } finally {
            especificacao.clearPassword();
        }
    }
}
