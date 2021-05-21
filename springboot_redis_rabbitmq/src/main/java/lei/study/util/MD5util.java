package lei.study.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;


import java.security.DigestException;

/**
 * Created by lei on 2021/5/21.
 */
@Component
public class MD5util {
    public static String md5(String string){
        return DigestUtils.md5Hex(string);
    }
    private static final String salt = "1a2b3c4d";
    public static String inputPassToFromPass(String inputPass){
        String str = salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }
    public static String formPassToDBPass(String formPass,String salt){
        String str = salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }
    public static String inputPassToDBPass(String inputPass,String salt){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = formPassToDBPass(fromPass,salt);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFromPass("123456"));
        System.out.println(formPassToDBPass("ce21b747de5af71ab5c2e20ff0a60eea","1a2b3c4d"));
    }
}
