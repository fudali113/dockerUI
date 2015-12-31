/**
 * 自定义一个关于ssh的异常
 */
package ren.doob.sshwebproxy;

public class SshConnectException extends Exception {

    public SshConnectException( String message )
    {
        super( message );
    }
}
