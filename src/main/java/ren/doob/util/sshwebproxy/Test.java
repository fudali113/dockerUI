package ren.doob.util.sshwebproxy;

/**
 * @author fudali
 * @package ren.doob.util.sshwebproxy
 * @class Test
 * @date 2016-1-6
 */

public class Test {
    public static void main(String[] args) {
        String[] lines =null;
        String[] lines1 = null;
        try{
            SshConnection sc = new SshConnection("",22,"root","");

            ShellChannel shell = sc.openShellChannel();

            shell.write("ls", true);
            shell.write("docker images", true);

            shell.read();
            lines = shell.getScreen();

            shell.write("docker ps", true);
            shell.read();
            lines1 = shell.getScreen();

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("chucuo");
        }
        if(lines == null){
            System.out.println("meiyouchenggong");
        }else{
            for (int i = 0; i < lines.length; i++) {
                System.out.println(lines[i]+"-------------------");
            }}

        for (int i = 0; i < lines1.length; i++) {
            System.out.println(lines1[i]+"oooooooooooooooooooooo");
        }
    }
}
