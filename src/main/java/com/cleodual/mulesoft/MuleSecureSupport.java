package com.cleodual.mulesoft;

import com.mulesoft.modules.configuration.properties.api.EncryptionAlgorithm;
import com.mulesoft.modules.configuration.properties.api.EncryptionMode;
import org.mule.encryption.Encrypter;
import org.mule.encryption.exception.MuleEncryptionException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class MuleSecureSupport {
    private MuleSecureSupport() {
    }

    public static String encryptValue(String value, String algorithm, String mode, String secretKey, boolean randomIv)
            throws MuleEncryptionException {
        Encrypter encrypter = createEncrypter(algorithm, mode, secretKey, randomIv);
        byte[] encrypted = encrypter.encrypt(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decryptValue(String value, String algorithm, String mode, String secretKey, boolean randomIv)
            throws MuleEncryptionException {
        Encrypter encrypter = createEncrypter(algorithm, mode, secretKey, randomIv);
        byte[] decrypted = encrypter.decrypt(Base64.getDecoder().decode(value));
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static Encrypter createEncrypter(String algorithm, String mode, String secretKey, boolean randomIv) {
        return EncryptionAlgorithm.valueOf(algorithm)
                .getBuilder()
                .forKey(secretKey)
                .using(EncryptionMode.valueOf(mode))
                .useRandomIVs(randomIv)
                .build();
    }
}
