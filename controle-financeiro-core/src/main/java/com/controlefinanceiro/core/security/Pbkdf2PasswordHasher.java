package com.controlefinanceiro.core.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Implementação de {@link PasswordHasher} usando PBKDF2WithHmacSHA256.
 *
 * Escolhido por ser parte da biblioteca padrão do JDK (javax.crypto), sem
 * necessidade de adicionar nenhuma dependência externa ao módulo core —
 * mantendo-o, como definido desde a Etapa 6, livre de dependências além do
 * JUnit (usado só em teste). O hash armazenado é auto-descritivo (contém o
 * número de iterações e o salt), então o número de iterações pode ser
 * aumentado no futuro sem invalidar senhas já cadastradas.
 *
 * Formato armazenado: {@code iteracoes:saltBase64:hashBase64}
 */
public class Pbkdf2PasswordHasher implements PasswordHasher {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int TAMANHO_SALT_BYTES = 16;
    private static final int TAMANHO_CHAVE_BITS = 256;
    private static final int ITERACOES_PADRAO = 120_000;

    private final int iteracoes;
    private final SecureRandom gerador = new SecureRandom();

    public Pbkdf2PasswordHasher() {
        this(ITERACOES_PADRAO);
    }

    public Pbkdf2PasswordHasher(int iteracoes) {
        this.iteracoes = iteracoes;
    }

    @Override
    public String hash(String senhaPlana) {
        byte[] salt = new byte[TAMANHO_SALT_BYTES];
        gerador.nextBytes(salt);

        byte[] hashBytes = derivar(senhaPlana, salt, iteracoes);

        return iteracoes + ":" + codificar(salt) + ":" + codificar(hashBytes);
    }

    @Override
    public boolean matches(String senhaPlana, String hashArmazenado) {
        if (senhaPlana == null || hashArmazenado == null) {
            return false;
        }

        String[] partes = hashArmazenado.split(":");
        if (partes.length != 3) {
            return false;
        }

        int iteracoesArmazenadas = Integer.parseInt(partes[0]);
        byte[] salt = decodificar(partes[1]);
        byte[] hashEsperado = decodificar(partes[2]);

        byte[] hashCalculado = derivar(senhaPlana, salt, iteracoesArmazenadas);

        return MessageDigest.isEqual(hashEsperado, hashCalculado);
    }

    private byte[] derivar(String senhaPlana, byte[] salt, int iteracoes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                    senhaPlana.toCharArray(), salt, iteracoes, TAMANHO_CHAVE_BITS);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITMO);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Falha ao gerar hash de senha.", e);
        }
    }

    private String codificar(byte[] dados) {
        return Base64.getEncoder().encodeToString(dados);
    }

    private byte[] decodificar(String texto) {
        return Base64.getDecoder().decode(texto);
    }
}
