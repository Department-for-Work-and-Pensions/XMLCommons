package com.dwp.carers.s2.xml.signing;

/**
 * Exception raised by a signature generator when an error occurred and cannot sign XML.
 *
 * @author Jorge Migueis
 *         Date: 28/06/2013
 */
public class SigningException extends RuntimeException {


    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public SigningException(String message, Throwable cause) {
        super(message, cause);
    }

}
