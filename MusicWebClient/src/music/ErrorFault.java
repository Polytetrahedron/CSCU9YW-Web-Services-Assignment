/**
 * ErrorFault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.4  Built on : Dec 28, 2015 (10:03:39 GMT)
 */
package music;

public class ErrorFault extends java.lang.Exception {
    private static final long serialVersionUID = 1553389067077L;
    private music.MusicServiceStub.ServiceError faultMessage;

    public ErrorFault() {
        super("ErrorFault");
    }

    public ErrorFault(java.lang.String s) {
        super(s);
    }

    public ErrorFault(java.lang.String s, java.lang.Throwable ex) {
        super(s, ex);
    }

    public ErrorFault(java.lang.Throwable cause) {
        super(cause);
    }

    public void setFaultMessage(music.MusicServiceStub.ServiceError msg) {
        faultMessage = msg;
    }

    public music.MusicServiceStub.ServiceError getFaultMessage() {
        return faultMessage;
    }
}
