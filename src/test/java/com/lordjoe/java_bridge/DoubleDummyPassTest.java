package com.lordjoe.java_bridge;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * com.lordjoe.java_bridge.DoubleDummyPassTest
 * User: Steve
 * Date: 10/3/2018
 */
public class DoubleDummyPassTest {
    public static final DoubleDummyPassTest[] EMPTY_ARRAY = {};

    public static final int[] answer = {
             8276 , //0x2054)
             18436 , //0x4804)
             8296 , //0x2068)
             8224 , //0x2020)
             5632 , //0x1600)
             264 , //0x108)
             23296 , //0x5B00)
             4736 , //0x1280)
             16520 , //0x4088)
             12416 , //0x3080)
             128 , //0x80)
              18776 , //0x4958)
             2336 , //0x920)
             1648 , //0x670)
            1044 , //0x414)
            1028 , //0x404)
    } ;

    public static final String DEAL_STRING = "N:K5.K653.AJ2.K64 Q97.AQJ98.83.QT9 AJ8643.7.KQ7.A73 T2.T42.T9654.J85";

    @Test
    public void testValues() {
        List<Hand> hands = new ArrayList<>();
        Hand[] handArray = DoubleDummyUtilities.parsePNBDeal(DEAL_STRING);
        for (int i = 0; i < handArray.length; i++) {
            Hand hand = handArray[i];
            hands.add(hand);
        }
        Deal deal = Deal.fromJavaHandList(hands);

    }
/*
tableDl.cards = {int[16]@2716}
 0 = 8276 (0x2054)
 1 = 18436 (0x4804)
 2 = 8296 (0x2068)
 3 = 8224 (0x2020)
 4 = 5632 (0x1600)
 5 = 264 (0x108)
 6 = 23296 (0x5B00)
 7 = 4736 (0x1280)
 8 = 16520 (0x4088)
 9 = 12416 (0x3080)
 10 = 128 (0x80)
 11 = 18776 (0x4958)
 12 = 2336 (0x920)
 13 = 1648 (0x670)
 14 = 1044 (0x414)
 15 = 1028 (0x404)



Morth
0 = {com.rogerpf.aabridge.model.Frag@2711}  size = 2
 0 = {com.rogerpf.aabridge.model.Card@2771} "KC"
 1 = {com.rogerpf.aabridge.model.Card@2772} "5C"
1 = {com.rogerpf.aabridge.model.Frag@2861}  size = 4
 0 = {com.rogerpf.aabridge.model.Card@2862} "KD"
 1 = {com.rogerpf.aabridge.model.Card@2906} "6D"
 2 = {com.rogerpf.aabridge.model.Card@2907} "5D"
 3 = {com.rogerpf.aabridge.model.Card@2908} "3D"
2 = {com.rogerpf.aabridge.model.Frag@3001}  size = 3
 0 = {com.rogerpf.aabridge.model.Card@3023} "AH"
 1 = {com.rogerpf.aabridge.model.Card@3026} "JH"
 2 = {com.rogerpf.aabridge.model.Card@3027} "2H"
3 = {com.rogerpf.aabridge.model.Frag@3112}  size = 4
 0 = {com.rogerpf.aabridge.model.Card@3130} "KS"
 1 = {com.rogerpf.aabridge.model.Card@3131} "6S"
 2 = {com.rogerpf.aabridge.model.Card@3132} "4S"
 3 = {com.rogerpf.aabridge.model.Card@3133} "2S"

 East
 0 = {com.rogerpf.aabridge.model.Frag@3143}  size = 3
 0 = {com.rogerpf.aabridge.model.Card@3148} "QC"
 1 = {com.rogerpf.aabridge.model.Card@3149} "9C"
 2 = {com.rogerpf.aabridge.model.Card@3150} "7C"
1 = {com.rogerpf.aabridge.model.Frag@3144}  size = 5
 0 = {com.rogerpf.aabridge.model.Card@3155} "AD"
 1 = {com.rogerpf.aabridge.model.Card@3156} "QD"
 2 = {com.rogerpf.aabridge.model.Card@3157} "JD"
 3 = {com.rogerpf.aabridge.model.Card@3158} "9D"
 4 = {com.rogerpf.aabridge.model.Card@3159} "8D"
2 = {com.rogerpf.aabridge.model.Frag@3145}  size = 2
 0 = {com.rogerpf.aabridge.model.Card@3166} "8H"
 1 = {com.rogerpf.aabridge.model.Card@3167} "3H"
3 = {com.rogerpf.aabridge.model.Frag@3146}  size = 3
 0 = {com.rogerpf.aabridge.model.Card@3171} "QS"
 1 = {com.rogerpf.aabridge.model.Card@3172} "TS"
 2 = {com.rogerpf.aabridge.model.Card@3173} "9S"

 South
 0 = {com.rogerpf.aabridge.model.Frag@3143}  size = 3
 0 = {com.rogerpf.aabridge.model.Card@3148} "QC"
 1 = {com.rogerpf.aabridge.model.Card@3149} "9C"
 2 = {com.rogerpf.aabridge.model.Card@3150} "7C"
1 = {com.rogerpf.aabridge.model.Frag@3144}  size = 5
 0 = {com.rogerpf.aabridge.model.Card@3155} "AD"
 1 = {com.rogerpf.aabridge.model.Card@3156} "QD"
 2 = {com.rogerpf.aabridge.model.Card@3157} "JD"
 3 = {com.rogerpf.aabridge.model.Card@3158} "9D"
 4 = {com.rogerpf.aabridge.model.Card@3159} "8D"
2 = {com.rogerpf.aabridge.model.Frag@3145}  size = 2
 0 = {com.rogerpf.aabridge.model.Card@3166} "8H"
 1 = {com.rogerpf.aabridge.model.Card@3167} "3H"
3 = {com.rogerpf.aabridge.model.Frag@3146}  size = 3
 0 = {com.rogerpf.aabridge.model.Card@3171} "QS"
 1 = {com.rogerpf.aabridge.model.Card@3172} "TS"
 2 = {com.rogerpf.aabridge.model.Card@3173} "9S"
 West
 0 = {com.rogerpf.aabridge.model.Frag@3220}  size = 2
 0 = {com.rogerpf.aabridge.model.Card@3225} "TC"
 1 = {com.rogerpf.aabridge.model.Card@3226} "2C"
1 = {com.rogerpf.aabridge.model.Frag@3221}  size = 3
 0 = {com.rogerpf.aabridge.model.Card@3230} "TD"
 1 = {com.rogerpf.aabridge.model.Card@3231} "4D"
 2 = {com.rogerpf.aabridge.model.Card@3232} "2D"
2 = {com.rogerpf.aabridge.model.Frag@3222}  size = 5
 0 = {com.rogerpf.aabridge.model.Card@3237} "TH"
 1 = {com.rogerpf.aabridge.model.Card@3238} "9H"
 2 = {com.rogerpf.aabridge.model.Card@3239} "6H"
 3 = {com.rogerpf.aabridge.model.Card@3240} "5H"
 4 = {com.rogerpf.aabridge.model.Card@3241} "4H"
3 = {com.rogerpf.aabridge.model.Frag@3223}  size = 3
 0 = {com.rogerpf.aabridge.model.Card@3248} "JS"
 1 = {com.rogerpf.aabridge.model.Card@3249} "8S"
 2 = {com.rogerpf.aabridge.model.Card@3250} "5S"

 */
}
