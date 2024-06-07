import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class PasswordUtils {

    // 솔트 생성
    public static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // 비밀번호 + 솔트를 해시
    public static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return hashedPassword;
    }

    // 비밀번호 비교
    public static boolean comparePasswords(String inputPassword, byte[] salt, byte[] storedHashedPassword) throws NoSuchAlgorithmException {
        byte[] inputHashedPassword = hashPassword(inputPassword, salt);
        return Arrays.equals(inputHashedPassword, storedHashedPassword);
    }

    public static void main(String[] args) {
        try {
            // 사용자 비밀번호와 솔트 생성 (예제용)
            String password = "userPassword123";
            byte[] salt = generateSalt();

            // 비밀번호 해싱
            byte[] hashedPassword = hashPassword(password, salt);
            System.out.println("storedHashedPassword: " + Arrays.toString(hashedPassword));
            // 예제: 저장된 해시와 솔트는 DB에 저장되어 있다고 가정
            // 사용자가 입력한 비밀번호를 가져와 비교
            String inputPassword = "userPassword123"; // 사용자가 입력한 비밀번호
            // 비교
            boolean isMatch = comparePasswords(inputPassword, salt, hashedPassword);

            System.out.println("passwordCorrespond: " + isMatch);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}