package org.luizleiteoliveira.entity;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class UserTest {

    @Test
    public void createUserWithSomeFields() {
        User user = new User("name", "fullName", "DOC12355", "email");
        assertTrue("name".contentEquals(user.getName()));
        assertTrue("fullName".contentEquals(user.getFullName()));
    }

}
