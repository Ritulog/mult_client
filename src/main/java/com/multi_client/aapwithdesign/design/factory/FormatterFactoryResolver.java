package com.multi_client.aapwithdesign.design.factory;

public class FormatterFactoryResolver {

    // 🔹 For MOBILE & WEB
    public static MessageFormatterFactory resolve(String clientType) {

        if ("MOBILE".equalsIgnoreCase(clientType)) {
            return new MobileFormatterFactory();

        } else if ("WEB".equalsIgnoreCase(clientType)) {
            return new WebFormatterFactory();

        } else {
            throw new RuntimeException(
                    "Unsupported client type or missing partner format"
            );
        }
    }

    // 🔹 For PARTNER (config based)
    public static MessageFormatterFactory resolve(
            String clientType,
            String responseFormatFromPartner) {

        if ("PARTNER".equalsIgnoreCase(clientType)) {
            return new PartnerFormatterFactory(
                    responseFormatFromPartner.trim()
            );
        }

        throw new RuntimeException("Unsupported client type");
    }
}
