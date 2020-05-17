package aensina;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import aensina.entidades.Usuario;

public class AssertTest {

    @Test
    public void testAssert() {

        assertTrue(true);
        assertFalse(false);
        assertEquals(0, 0);
        assertEquals(Math.PI, 3.14, 0.01); // Em caso de double ou float, deve-se colocar um delta de verificação de quantas casas decimais o teste verificará.

        int i = 5;
        Integer ii = 5;
        assertEquals(i, ii.intValue());
        assertEquals(Integer.valueOf(i), ii);

        assertNotEquals("oi", "tchau");

        assertTrue("bola".equalsIgnoreCase("Bola"));
        assertTrue("bola".startsWith("bo"));

        Usuario user1 = new Usuario("Alan");
        Usuario user2 = new Usuario("Alan");
        Usuario user3 = user1;
        Usuario user4 = null;

        assertEquals(user1, user2); // Só é possível pois o método equals foi implementado na entidade.
        assertSame(user1, user3); // Verifica se é a mesma instância
        assertNotSame(user1, user2);

        assertNull(user4);
        assertNotNull(user1);

    }

}
