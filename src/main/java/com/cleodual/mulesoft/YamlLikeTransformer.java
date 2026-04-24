package com.cleodual.mulesoft;

import org.mule.encryption.exception.MuleEncryptionException;

import java.util.ArrayList;
import java.util.List;

public final class YamlLikeTransformer {
    private static final String PREFIX = "![";
    private static final String SUFFIX = "]";

    private YamlLikeTransformer() {
    }

    public static String encrypt(
            String input,
            String algorithm,
            String mode,
            String secretKey,
            boolean randomIv,
            boolean quoteOutputValues
    )
            throws MuleEncryptionException {
        if (input == null || input.isBlank()) {
            return "No properties to encrypt";
        }

        TransformResult result = transformLines(input, true, algorithm, mode, secretKey, randomIv, quoteOutputValues);
        if (result.structured) {
            return result.text;
        }

        String encryptedValue = wrap(MuleSecureSupport.encryptValue(input, algorithm, mode, secretKey, randomIv));
        return quoteOutputValues ? quote(encryptedValue) : encryptedValue;
    }

    public static String decrypt(
            String input,
            String algorithm,
            String mode,
            String secretKey,
            boolean randomIv,
            boolean quoteOutputValues
    )
            throws MuleEncryptionException {
        if (input == null || input.isBlank()) {
            return "No properties to decrypt";
        }

        TransformResult result = transformLines(input, false, algorithm, mode, secretKey, randomIv, quoteOutputValues);
        if (result.structured) {
            return result.text;
        }

        String decryptedValue = MuleSecureSupport.decryptValue(unwrap(unquote(input.trim())), algorithm, mode, secretKey, randomIv);
        return quoteOutputValues ? quote(decryptedValue) : decryptedValue;
    }

    private static TransformResult transformLines(
            String input,
            boolean encrypt,
            String algorithm,
            String mode,
            String secretKey,
            boolean randomIv,
            boolean quoteOutputValues
    ) throws MuleEncryptionException {
        String[] lines = input.split("\\R", -1);
        List<String> output = new ArrayList<>(lines.length);
        boolean structured = false;

        for (String line : lines) {
            if (isCommentOrBlank(line)) {
                output.add(line);
                continue;
            }

            String transformed = transformKeyValueLine(line, encrypt, algorithm, mode, secretKey, randomIv, quoteOutputValues);
            if (transformed != null) {
                structured = true;
                output.add(transformed);
                continue;
            }

            transformed = transformListItemLine(line, encrypt, algorithm, mode, secretKey, randomIv, quoteOutputValues);
            if (transformed != null) {
                structured = true;
                output.add(transformed);
                continue;
            }

            if (looksLikeNestedYamlKey(line)) {
                structured = true;
            }
            output.add(line);
        }

        return new TransformResult(String.join(System.lineSeparator(), output), structured);
    }

    private static String transformKeyValueLine(
            String line,
            boolean encrypt,
            String algorithm,
            String mode,
            String secretKey,
            boolean randomIv,
            boolean quoteOutputValues
    ) throws MuleEncryptionException {
        int colonIndex = line.indexOf(':');
        if (colonIndex <= -1) {
            return null;
        }

        String prefix = line.substring(0, colonIndex + 1);
        String remainder = line.substring(colonIndex + 1);
        if (remainder.isBlank()) {
            return null;
        }

        String leadingWhitespace = leadingWhitespace(remainder);
        String value = remainder.substring(leadingWhitespace.length()).trim();
        if (value.isEmpty()) {
            return null;
        }

        String transformedValue = encrypt
                ? formatOutputValue(wrap(MuleSecureSupport.encryptValue(value, algorithm, mode, secretKey, randomIv)), quoteOutputValues)
                : decryptYamlValue(value, algorithm, mode, secretKey, randomIv, quoteOutputValues);

        if (!encrypt && transformedValue == null) {
            return line;
        }

        return prefix + leadingWhitespace + transformedValue;
    }

    private static String transformListItemLine(
            String line,
            boolean encrypt,
            String algorithm,
            String mode,
            String secretKey,
            boolean randomIv,
            boolean quoteOutputValues
    ) throws MuleEncryptionException {
        String trimmed = line.trim();
        if (!trimmed.startsWith("- ")) {
            return null;
        }

        int index = line.indexOf("- ");
        String prefix = line.substring(0, index + 2);
        String value = line.substring(index + 2).trim();
        if (value.isEmpty()) {
            return null;
        }

        String transformedValue = encrypt
                ? formatOutputValue(wrap(MuleSecureSupport.encryptValue(value, algorithm, mode, secretKey, randomIv)), quoteOutputValues)
                : decryptYamlValue(value, algorithm, mode, secretKey, randomIv, quoteOutputValues);

        if (!encrypt && transformedValue == null) {
            return line;
        }

        return prefix + transformedValue;
    }

    private static String decryptYamlValue(
            String value,
            String algorithm,
            String mode,
            String secretKey,
            boolean randomIv,
            boolean quoteOutputValues
    ) throws MuleEncryptionException {
        String normalizedValue = unquote(value);
        if (!isWrapped(normalizedValue)) {
            return null;
        }

        String decryptedValue = MuleSecureSupport.decryptValue(unwrap(normalizedValue), algorithm, mode, secretKey, randomIv);
        return formatOutputValue(decryptedValue, quoteOutputValues);
    }

    private static boolean looksLikeNestedYamlKey(String line) {
        String trimmed = line.trim();
        return !trimmed.startsWith("- ")
                && !trimmed.startsWith("#")
                && trimmed.endsWith(":")
                && trimmed.length() > 1;
    }

    private static boolean isCommentOrBlank(String line) {
        String trimmed = line.trim();
        return trimmed.isEmpty() || trimmed.startsWith("#");
    }

    private static String leadingWhitespace(String value) {
        int index = 0;
        while (index < value.length() && Character.isWhitespace(value.charAt(index))) {
            index++;
        }
        return value.substring(0, index);
    }

    private static String wrap(String encryptedValue) {
        return PREFIX + encryptedValue + SUFFIX;
    }

    private static String quote(String value) {
        return "\"" + value + "\"";
    }

    private static String formatOutputValue(String value, boolean quoteOutputValues) {
        return quoteOutputValues ? quote(value) : value;
    }

    private static String unquote(String value) {
        if (value == null || value.length() < 2) {
            return value;
        }

        boolean doubleQuoted = value.startsWith("\"") && value.endsWith("\"");
        boolean singleQuoted = value.startsWith("'") && value.endsWith("'");
        if (doubleQuoted || singleQuoted) {
            return value.substring(1, value.length() - 1);
        }

        return value;
    }

    private static String unwrap(String value) {
        if (isWrapped(value)) {
            return value.substring(PREFIX.length(), value.length() - SUFFIX.length());
        }
        return value;
    }

    private static boolean isWrapped(String value) {
        return value != null && value.startsWith(PREFIX) && value.endsWith(SUFFIX);
    }

    private record TransformResult(String text, boolean structured) {
    }
}
