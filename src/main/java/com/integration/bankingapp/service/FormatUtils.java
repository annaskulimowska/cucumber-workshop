package com.integration.bankingapp.service;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;

class FormatUtils {

    static String formatIban(String iban) {
        return formatNumber("??## #### #### #### #### #### ####", iban);
    }

    static String formatNumber(String mask, String iban) {
        try {
            MaskFormatter maskFormatter = new MaskFormatter(mask);
            maskFormatter.setValueContainsLiteralCharacters(false);
            return maskFormatter.valueToString(iban);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    static String formatAccountNumber(String number) {
        return formatNumber("## #### #### #### #### #### ####", number);
    }
}
