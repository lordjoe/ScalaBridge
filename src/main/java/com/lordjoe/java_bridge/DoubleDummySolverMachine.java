package com.lordjoe.java_bridge;


import com.rogerpf.aabridge.dds.ddTableDeal;

import scala.Option;

import java.util.ArrayList;
import java.util.List;




/**
 * com.lordjoe.bridge.DoubleDummySolver
 * User: Steve
 * Date: 10/1/2018
 */
public class DoubleDummySolverMachine {

    public static final Suit[] suites = { Suit.clubs(), Suit.diamonds(), Suit.hearts(), Suit.spades()} ;

 //   public static final Trump[] TRUMP_ALTERNATIVES = { Trump.NOTRUMP, Trump.CLUBS, Trump.DIAMONDS, Trump.HEARTS, Trump.SPADES };

    public static DoubleDummySolution solveDoubleDummy(com.lordjoe.java_bridge.Deal scalaDeal)
    {

           return new DoubleDummySolution(scalaDeal );
    }

//    public static  com.rogerpf.aabridge.model.Deal fromScalaDeal(com.lordjoe.java_bridge.Deal scalaDeal) {
//        com.rogerpf.aabridge.model.Deal ret = new  com.rogerpf.aabridge.model.Deal( );
//        for (int i = 0; i < 4; i++) {
//           Position p =  Position.fromInt(i) ;
//            Option<com.lordjoe.java_bridge.Hand> handOption = scalaDeal.hands().get(p);
//            com.lordjoe.java_bridge.Hand hand = handOption.get();
//            com.rogerpf.aabridge.model.Hand aaHand = new  com.rogerpf.aabridge.model.Hand(ret,Dir.dirFromInt(i));
//            setFromHand( hand, aaHand );
//            ret.hands[i] = aaHand;
//        }
//        return ret;
//    }

    public static ddTableDeal.ByValue toDDDeal(com.lordjoe.java_bridge.Deal scalaDeal)   {

        ddTableDeal.ByValue tableDl = new ddTableDeal.ByValue();

        for (int i = 0; i < 4; i++) {
            Position p =  Position.fromInt(i) ;
            Option<com.lordjoe.java_bridge.Hand> handOption = scalaDeal.hands().get(p);
            com.lordjoe.java_bridge.Hand hand = handOption.get();
             setFromHand(i, hand, tableDl );
          }

        return tableDl;
    }

    private static void setFromHand(int dir,com.lordjoe.java_bridge.Hand hand,ddTableDeal.ByValue tableDl) {
        int[] suits = setValuesFromHand( hand) ;
        for (int i = 0; i < suits.length; i++) {
            int suit = suits[i];
            tableDl.cards[dir * DoubleDummySolution.NUMBER_POSITIONS + i] = suits[i];
        }
    }

    private static int[] setValuesFromHand( com.lordjoe.java_bridge.Hand hand ) {

        int[] ret = new int[4];
        for (int i = 0; i < 13; i++) {
            Card c = hand.getCard(i);
            int sRank = c.suit().suitRank();
            int rank = c.value();
            ret[sRank - 1] |= 1 <<  rank;

        }
        return ret;
    }


//
//    private static void setFromHand(com.lordjoe.java_bridge.Hand hand,com.rogerpf.aabridge.model.Hand aaHand ) {
//
//          for (int i = 0; i < 13; i++) {
//            Card c = hand.getCard(i);
//            com.rogerpf.aabridge.model.Card aaC = fromScalaCard(c);
//            aaHand.addDeltCard(aaC);
//        }
//      }
//
//
//    private static com.rogerpf.aabridge.model.Card fromScalaCard(com.lordjoe.java_bridge.Card card) {
//        StringBuilder sb = new StringBuilder();
//        Rank rank = card.rank();
//        String added = rank.shortName();
//        if(added.equals("10"))
//            added = "T";
//        Suit s = card.suit();
//        String suitStr = s.name().substring(0,1);
//         sb.append(suitStr);
//        sb.append(added);
//
//
//        return  com.rogerpf.aabridge.model.Card.singleCardFromLinStr(sb.toString());
//
//    }

}
