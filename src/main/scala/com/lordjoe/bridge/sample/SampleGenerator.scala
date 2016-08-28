package com.lordjoe.bridge.sample

import java.io.{File, PrintWriter}

import com.lordjoe.bridge._
import solitarius.general.{Distribution, Precision}

import scala.collection.mutable.ArrayBuffer

/**
 * solitarius.Main
 * User: Steve
 * Date: 7/12/2015
 */


object SampleGenerator {

  def showHandsOpening(out: PrintWriter, rule: HandSatisfies, number: Int): Unit = {
    if (number > 0) {
      val h: Deal = findSatisfyingHand(rule)
      out.println(h.toAppendable)
      showHandsOpening(out, rule, number - 1)
    }
  }

  def findSatisfyingHand(rule: HandSatisfies): Deal = {
    var ret: Deal = null
    while (ret == null) {
      val deal: Deal = BridgeDealer.makeHands
      if (rule.isSatisfied(deal.declarer))
         ret = deal
      else if (rule.isSatisfied(deal.declarer.nextBidder))
         ret = deal
      else if (rule.isSatisfied(deal.declarer.partner))
         ret = deal
      else if (rule.isSatisfied(deal.declarer.previousBidder))
         ret = deal
    }
    ret
  }


  object AnyHand extends HandSatisfies {
    override def isSatisfied(x: Hand): Boolean = true
  }

  object InterestingHand extends HandSatisfies {
    override def isSatisfied(x: Hand): Boolean = {

      val s1 = OrdinaryOpen.isSatisfied(x)
      val s2 = NoPreviousOpen.isSatisfied(x)
      val s3 = x.maxMajorLength < 5
    //  val s4 = !CanOpen2Clubs.isSatisfied(x)
      val s5 = (!isFlat.isSatisfied(x)) //  || (x.cardsInSuit(Suit.Spade) == 4 && x.cardsInSuit(Suit.Heart) == 4))
      val s6 = x.cardsInSuit(Suit.Diamond) < 4

      s1 && s2 && s3 && s5 && s6  //  && s4
    }
  }



  def main(args: Array[String]) {

    val writer: PrintWriter = new PrintWriter(new File("noIInterference.txt"))

    showHandsOpening(writer, InterestingHand, 120)
  }

  def mainX(args: Array[String]) {

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
      if (Position.isNorthSouth(bidder.position)) {
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
