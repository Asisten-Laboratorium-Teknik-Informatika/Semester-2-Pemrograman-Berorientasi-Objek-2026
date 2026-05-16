package service;

import model.User;
import repository.UserRepository;
import session.LoginSession;

public class AuthService {

    UserRepository userRepository
            = new UserRepository();

    /*
     * =========================
     * REGISTER
     * =========================
     */
    public boolean register(

            String nama,
            String email,
            String password,
            String phone

    ) {

        return userRepository.registerUser(

                nama,
                email,
                password,
                phone
        );
    }

    /*
     * =========================
     * LOGIN
     * =========================
     */
    public User login(

            String email,
            String password

    ) {

        User user =
                userRepository.login(
                        email,
                        password
                );

        if (user != null) {

            LoginSession.login(

                    user.getUserId(),

                    user.getNama(),

                    user.getRole()
            );
        }

        return user;
    }
}