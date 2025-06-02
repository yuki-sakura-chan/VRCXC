package com.github.vrcxc.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;

public class RsaKeyGenerator {
    public static void main(String[] args) {
        try {
            // 创建密钥对生成器
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048); // 设置密钥大小

            // 生成密钥对
            KeyPair keyPair = keyGen.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 保存公钥
            saveKeyToFile("public_key.pem", publicKey.getEncoded());
            // 保存私钥
            saveKeyToFile("private_key.pem", privateKey.getEncoded());

            System.out.println("公钥和私钥已保存到文件。");
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveKeyToFile(String fileName, byte[] key) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN ").append(fileName.contains("public") ? "PUBLIC" : "PRIVATE").append(" KEY-----\n");
        String base64Encoded = Base64.getEncoder().encodeToString(key);
        for (int i = 0; i < base64Encoded.length(); i += 64) {
            sb.append(base64Encoded, i, Math.min(i + 64, base64Encoded.length())).append("\n");
        }
        sb.append("-----END ").append(fileName.contains("public") ? "PUBLIC" : "PRIVATE").append(" KEY-----\n");

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            writer.write(sb.toString());
        }
    }
}
