package cibertec.blog.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptGenerator {
    public static void main(String[] args) {
        String raw = "admin123"; // cambia si quieres
        String hash = BCrypt.hashpw(raw, BCrypt.gensalt(10));
        System.out.println(hash);
    }
}
