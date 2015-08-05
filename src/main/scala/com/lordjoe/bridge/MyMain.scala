package com.lordjoe.bridge

import java.io.{File, PrintWriter}

import solitarius.general.{Distribution, Precision}

import scala.collection.mutable.ArrayBuffer

/**
  * solitarius.Main
  * User: Steve
  * Date: 7/12/2015
  */


object MyMain {


   def main(args: Array[String]) {

     val writer: PrintWriter = new PrintWriter(new File("noIInterference.txt"))

     val numberHands: Int = 1000000
     var bidding1C = 0
     var number4441 = 0
     var positiveHands: ArrayBuffer[Deal] = new ArrayBuffer[Deal]()
     for (i <- 0 until numberHands) {
       var deal: Deal = BridgeDealer.makeHands
       if (Distribution.is4441(deal.declarer)) {
 //        deal.append(System.out)
 //        System.out.println
         number4441 += 1

       }

       if (Precision.willOpen1ClubWithPositiveResponseIgnoreInterference(deal)) // (Precision.willOpen1ClubWithPositiveResponse(deal))
         positiveHands += deal
     }
     var positiveHands4441: ArrayBuffer[Deal] = new ArrayBuffer[Deal]()
     for (deal: Deal <- positiveHands) {
       val bidder: Hand = Precision.oneClubBidder(deal);
       if (Position.isNorthSouth(bidder.pos)) {
         val responder: Hand = deal.partner(bidder)
         if (Distribution.is4441(responder)) {
           positiveHands4441 += deal

           deal.append(writer)
           writer.println
           writer.println
           writer.println
         }
       }
     }

     writer.println("Hands " + numberHands + " Positive " + positiveHands.size + " chances " + (0.5 * positiveHands.size) / numberHands + " 4441 " + positiveHands4441.size)
     writer.close()

     print("Hands " + numberHands + " Positive " + positiveHands.size + " chances " + (0.5 * positiveHands.size) / numberHands + " 4441 " + positiveHands4441.size)
   }


 }
