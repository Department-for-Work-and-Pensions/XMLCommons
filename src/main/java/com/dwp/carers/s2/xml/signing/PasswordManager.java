package com.dwp.carers.s2.xml.signing;

/**
 * Utility class that manages passwords for the application.
 * Hard coded passwords are not saved as a string for security reason (light obfuscation).
 *
 * @author Jorge Migueis
 *         Date: 28/06/2013
 */
class PasswordManager {

    /** Password used to check integrity of key store. */
    private static String keyStorePassword = null;
    /** Password used to encrypt entries in the key store. */
    private static String keyStoreEntryPassword = null;
    /** Characters allowed for our passwords */
    public static final String CHAR_ALLOWED = "QWbvEI=OPLK-JHGF+DSAZ%sVBMm%cUNcxz!sdfghjklpoiuytrewq%dRTY,a@#";

    /** Key Store password. */
    private static final int[] KEY_STORE_PASSWORD = {34,38,49,48,49,47,43,27,40,49,46};
    /** Key Store entry password. */
    private static final int[] KEY_STORE_ENTRY_PASSWORD = {34,38,49,48,49,47,43,27,40,49,46,53};

    /**
     * Ensure nobody can create an instance.
     */
    private PasswordManager() {}

    /**
     * Obfuscation of the application key store's password.
     * @return the application key store's password.
     */
    public static synchronized String getKeyStorePassword() {
        if (null == keyStorePassword) {
            keyStorePassword = buildPassword(KEY_STORE_PASSWORD);
        }
        return keyStorePassword;
    }

    /**
     * Obfuscation of the application key store entries' password.
     * @return the application key store entries' password.
     */
    public static synchronized String getKeyStoreEntryPassword() {
        if (null == keyStoreEntryPassword) {
            keyStoreEntryPassword = buildPassword(KEY_STORE_ENTRY_PASSWORD);
        }
        return keyStoreEntryPassword;
    }

    /**
     * @param passwordSequence index of the characters to use.
     * @return the password built using the sequence provided.
     */
    private static synchronized String buildPassword(final int[] passwordSequence) {
        final StringBuffer password = new StringBuffer();
        final String charsAllowed =String.format(CHAR_ALLOWED,"XC",'n',123456);
        for (int aPasswordSequence : passwordSequence) {
            password.append(charsAllowed.toCharArray()[aPasswordSequence]);
        }
        return password.toString();
    }
}
