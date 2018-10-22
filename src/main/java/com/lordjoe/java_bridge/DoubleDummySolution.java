package com.lordjoe.java_bridge;

import com.rogerpf.aabridge.dds.ComRogerpfAabridgeDdsLibrary;
import com.rogerpf.aabridge.dds.Z_bothResults;
import com.rogerpf.aabridge.dds.ddTableDeal;

import java.io.IOException;

/**
 * com.lordjoe.bridge.DoubleDummySolution
 * User: Steve
 * Date: 10/1/2018
 */
public class DoubleDummySolution {

    public static final int NUMBER_CONTRACTS = 5;
    public static final int NUMBER_POSITIONS = 4;



    public final com.lordjoe.java_bridge.Deal scalaDeal;
//    private final com.rogerpf.aabridge.model.Deal deal;
    private final int[] results = new int[ NUMBER_CONTRACTS * NUMBER_POSITIONS];

    public DoubleDummySolution(com.lordjoe.java_bridge.Deal scalaDealX) {
        scalaDeal = scalaDealX;
  //      deal = DoubleDummySolverMachine.fromScalaDeal(scalaDeal);
        analyzeDeal();
        showTricks();
    }

    public void analyzeDeal() {
        ComRogerpfAabridgeDdsLibrary dds = ComRogerpfAabridgeDdsLibrary.INSTANCE;

        Z_bothResults rtn = new Z_bothResults();

        ddTableDeal.ByValue tableDl = DoubleDummySolverMachine.toDDDeal(scalaDeal);


        rtn.resp = dds.CalcDDtable(tableDl, rtn.ddTableRes);

        int[] res = rtn.ddTableRes.resTable;
        System.arraycopy(res,0,results,0,res.length);

        if (rtn.resp != 1) { // should not happen
            rtn.errStr = rtn.resp + ""; // Error string lookup may be added
        }

//        rtn.resp = dds.DealerParBin(rtn.ddTableRes, rtn.parResMaster, deal.contractCompass.v, deal.getDdsVulnerability()); // bug fix 2826
//
//        if (rtn.resp != 1) { // should not happen
//            rtn.errStr = rtn.resp + ""; // Error string lookup may be added
//        }
    }


    public int getTricks(Suit trump, Position declarer) {
        int pos = declarer.asIndex();
          return(getTricks(trump,pos));

    }

    public int getTricks(Suit trump, int pos) {
        int s =   DoubleDummyUtilities.fromSuit(trump);
        return(DoubleDummyUtilities.getTricks(s,pos,results));

    }


    public void showTricks() {
        try {
            showTricks(System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }


    public static final String[] positions = { "East","North","West","South"};
    public static final String[] suitesStrs = { "Clubs","Diamonds","Hearts","Spades","NoTrump"};
    public static final Suit[] suites = { Suit.clubs(),Suit.diamonds(),Suit.hearts(),Suit.spades(),null};

    public void showTricks(Appendable out) throws IOException {


        out.append("\t\t");
        for (int i = 0; i < suitesStrs.length; i++) {
               out.append(suites[i] + "\t");

        }
        out.append("\n");

        for (int position = 0; position < 4; position++) {
            Position position1 = Position.fromInt(position);
            String posStr = position1.toString();
            out.append(posStr + "\t");
            for (int k = 0; k < suites.length; k++) {
                Suit s = suites[k];
                Integer tricks = getTricks(s,position ) ;
                 out.append(tricksToString(tricks) + "\t");

            }
            out.append("\n");
        }
    }

    public String tricksToString(int tricks)    {
        if(tricks < 7)   {
            return "(" + tricks + ")";
        }
        else
            return "" + (tricks - 6) ;
    }

}
