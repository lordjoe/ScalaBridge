package com.lordjoe.java_bridge;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * com.lordjoe.java_bridge.DoubleDummySolutionTest
 * User: Steve
 * Date: 10/3/2018
 */
public class DoubleDummySolutionTest {
    public static final int[] real_values = {
              8, 5 , 6, 5,
            5, 8, 4, 8,
            11, 2 , 10, 2 ,
            11, 2, 10, 2 ,
            8 , 2  , 7 , 2
    } ;

    public static final int EAST = 0;
    public static final int NORTH = 1;
    public static final int WEST = 2;
    public static final int SOUTH = 3;


    @Test
    public void testValues()
    {
        assertEquals( 11,getTricks(Suit.clubs(),Position.fromInt(NORTH))) ;
        assertEquals( 10,getTricks(Suit.clubs(),Position.fromInt(SOUTH))) ;
        assertEquals( 2,getTricks(Suit.clubs(),Position.fromInt(EAST))) ;
        assertEquals( 2,getTricks(Suit.clubs(),Position.fromInt(WEST))) ;

        assertEquals( 11,getTricks(Suit.diamonds(),Position.fromInt(NORTH))) ;
        assertEquals( 10,getTricks(Suit.diamonds(),Position.fromInt(SOUTH))) ;
        assertEquals( 2,getTricks(Suit.diamonds(),Position.fromInt(EAST))) ;
        assertEquals( 2,getTricks(Suit.diamonds(),Position.fromInt(WEST))) ;

        assertEquals( 5,getTricks(Suit.hearts(),Position.fromInt(NORTH))) ;
        assertEquals( 4,getTricks(Suit.hearts(),Position.fromInt(SOUTH))) ;
        assertEquals( 8,getTricks(Suit.hearts(),Position.fromInt(EAST))) ;
        assertEquals( 8,getTricks(Suit.hearts(),Position.fromInt(WEST))) ;

        assertEquals( 8,getTricks(Suit.spades(),Position.fromInt(NORTH))) ;
        assertEquals( 6,getTricks(Suit.spades(),Position.fromInt(SOUTH))) ;
        assertEquals( 5,getTricks(Suit.spades(),Position.fromInt(EAST))) ;
        assertEquals( 5,getTricks(Suit.spades(),Position.fromInt(WEST))) ;

        assertEquals( 8,getTricks(null,Position.fromInt(NORTH))) ;
        assertEquals( 7,getTricks(null,Position.fromInt(SOUTH))) ;
        assertEquals( 2,getTricks(null,Position.fromInt(EAST))) ;
        assertEquals( 2,getTricks(null,Position.fromInt(WEST))) ;

    }

    public static int getTricks(Suit s,Position p)  {
        int pos = DoubleDummyUtilities.positionToInt(p);
        int trump = DoubleDummyUtilities.fromSuit(s);
        return DoubleDummyUtilities.getTricks(trump,pos,real_values);
    }

}
