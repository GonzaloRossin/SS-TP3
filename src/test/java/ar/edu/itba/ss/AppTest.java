package ar.edu.itba.ss;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        double module = 0.01, ang = Math.PI;
        System.out.println(Math.cos(ang) * module);
        System.out.println(Math.round(Math.sin(ang) * module));
    }
}
