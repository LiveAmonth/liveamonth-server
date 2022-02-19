package teamproject.lam_server.app.member.exception;

import static teamproject.lam_server.app.member.exception.messages.UserExceptionMessages.CORRESPOND_REFRESH_TOKEN_MESSAGE;
import static teamproject.lam_server.app.member.exception.messages.UserExceptionMessages.VALID_REFRESH_TOKEN_MESSAGE;

public class TokenException extends RuntimeException{

    public TokenException(String message) {
        super(message);
    }

    private static class Valid extends TokenException{
        public Valid() {
            super(VALID_REFRESH_TOKEN_MESSAGE.getMessage());
        }
    }

    private static class Correspond  extends RuntimeException{
        public Correspond() {
            super(CORRESPOND_REFRESH_TOKEN_MESSAGE.getMessage());
        }
    }
}