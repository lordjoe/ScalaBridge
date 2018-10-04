package com.lordjoe.java_bridge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * com.lordjoe.java_bridge.DoubleDummyUtilities
 * User: Steve
 * Date: 10/3/2018
 */
public class DoubleDummyUtilities {

    public static final int  NUMBER_POSITIONS = 4;

    public static int positionToInt( Position p)  {
        return p.asInt();
    }

    public static int fromSuit( Suit p)  {
        if(p == null)
            return 4;
        if(p == Suit.clubs())
            return 3;
        if(p == Suit.diamonds())
            return 2;
        if(p == Suit.hearts())
            return 1;
        if(p == Suit.spades())
            return 0;
        throw new UnsupportedOperationException("Fail");
    }

    public static int getTricks( int s, int pos, int[] results) {

        int index = s * NUMBER_POSITIONS +  pos ;
        return results[index];

    }

    /**
     * "N:.63.AKQ987.A9732 A8654.KQ5.T.QJT6 J973.J98742.3.K4 KQT2.AT.J6542.85"
     * @param deal
     * @return
     */
    public static Hand[] parsePNBDeal(String deal)  {

        if(!deal.startsWith("N:"))  {
            throw new IllegalArgumentException("deals start N: " + deal);
        }
        String[] hands = deal.substring(2).split(" ");
        if(hands.length != 4)  {
            throw new IllegalArgumentException("deals have 4 hands " + deal);
        }
        Hand[] ret = new Hand[4];
        for (int i = 0; i < hands.length; i++) {
            ret[i] = parsePNBHand(i,hands[i]);

        }
        return ret;
      }

    private static Hand parsePNBHand(int position,String hand) {
        String[] cards = hand.split("\\.");
        if(cards.length != 4)  {
            throw new IllegalArgumentException("hands have 4 suits " + hand);
        }
        List<Card> holding = new ArrayList<>();
        for (int i = 0; i < cards.length; i++) {
            String card = cards[i];
            Card[] cards1 = parsePNBCards(i, card);
            for (int j = 0; j < cards1.length; j++) {
                Card card1 = cards1[j];
                 holding.add(card1);
            }
         }
        return handFromHolding(position,holding);
    }

    private static Hand handFromHolding(int position,List<Card> holding) {

             Position p = Position.fromInt(position);
             return Hand.fromCardJavaList(p,holding);
        }

    private static Card[] parsePNBCards(int suit, String card) {
        Card[] ret = new Card[card.length()];
        for (int j = 0; j < ret.length; j++) {
           char c = card.charAt(j);
           Rank r = Rank.fromChar(c);
           Suit s = Suit.fromInt(suit);
           ret[j] =  Card.apply(r,s);
        }

        return ret;
    }
}
