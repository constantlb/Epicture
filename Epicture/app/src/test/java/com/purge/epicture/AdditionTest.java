package com.purge.epicture;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AdditionTest {

        private Addition addition;

        @Before
        public void setUp() {
            addition = new Addition();
        }


        @Test
        public void calculerTest() {
            assertEquals(6, addition.calculer(3,3));
        }
}

