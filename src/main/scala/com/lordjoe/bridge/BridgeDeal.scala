package com.lordjoe.bridge

/**
 * solitarius.general.BridgeDeal 
 * User: Steve
 * Date: 7/11/2015
 */

import com.lordjoe.scala.CanAppend
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import scala.collection.breakOut

// represents North,South,East,West
sealed abstract class Position {

  def asInt: Int;

  def partner: Position

  def next: Position

  def prev: Position = next.partner

}

/**
 * N S E W
 */
object Position {

  object East extends Position {

    def asInt = 0

    def partner = West

    def next = South

    override def toString = "East"
  }

  object South extends Position {
    def asInt = 1

    def partner = North

    def next = West

    override def toString = "South"
  }

  object West extends Position {
    def asInt = 2

    def partner = East

    def next = North

    override def toString = "West"
  }

  object North extends Position {
    def asInt = 3

    def partner = South

    def next = East

    override def toString = "North"
  }

  val positions = {
    List(East, South, West, North)
  }
  val NSPositions = List(North, South)
  val EWpositions = List(East, West)

  def isNorthSouth(position: Position): Boolean = position == North || position == South

  def isEastWest(position: Position): Boolean = position == East || position == West

  def random: Position = positions(Random.nextInt(positions.length));

}

/**
 * represents vulnerability of a hand   None, EW, NS, All
 */
sealed abstract class Vulnerability {

  def isVulnerable(h: Hand): Boolean = isVulnerable(h.position)

  def isVulnerable(p: Position): Boolean;
}


object Vulnerability {

  case object None extends Vulnerability {
    def isVulnerable(p: Position): Boolean = false
  }

  case object EW extends Vulnerability {
    def isVulnerable(p: Position): Boolean = Position.isEastWest(p)
  }

  case object NS extends Vulnerability {
    def isVulnerable(p: Position): Boolean = Position.isNorthSouth(p)
  }

  case object All extends Vulnerability {
    def isVulnerable(p: Position): Boolean = true
  }

  val posibilities: List[Vulnerability] = List(None, EW, NS, All)

  def random: Vulnerability = posibilities(Random.nextInt(posibilities.length));
}


object Hand {
  def apply(pos: Position,deal : Deal, cards: List[Card]) = new Hand(pos,deal,cards)
  def apply( deal : Deal, hand: Hand) = new Hand(hand.position,deal,hand.cards)
}

/**
 * represents a dealt hand
 * @param position   NSEW
 * @param cards   dealt cards
 */
class Hand(val position: Position,deal : Deal, val cards: List[Card]) extends CanAppend {
  val ofSuit: Map[Suit,List[Card]] = Suit.suits.map(s => (s,cards.filter(c => c.suit == s).sortBy(_.rank).reverse)).toMap
// //  val byBuit: Map(Suit,List(Card))  = Suit.suits.map(f -> (f,))
//  val hearts = ofSuit(Suit.Heart)
//  val spades = ofSuit(Suit.Spade)
//  val diamonds = ofSuit(Suit.Diamond)
//  val clubs = ofSuit(Suit.Spade)

    def hcp: Int =  { cards.map(_.points).sum }

    def isVulnerable: Boolean = {
     if(deal == null)
       false
     deal.vulnerability.isVulnerable(this)
  }

  def handDeal: Deal = deal

  def isDealer: Boolean = {
      if(deal == null)
        false
      deal.declarer == this
   }

  def nextBidder: Hand  = deal.handAt(position.next)

  def partner: Hand  = deal.handAt(position.partner)

  def previousBidder: Hand  = deal.handAt(position.next)


  def cardsInSuit(testSuit: Suit): Int = {
    ofSuit(testSuit).length
  }

  def distribution: List[Int] = Suit.suits.map(s => cardsInSuit(s))


  def maxSuitLength: Int =  Suit.suits.map(cardsInSuit).reduceLeft ( _ max _ )

  def minSuitLength: Int = Suit.suits.map(cardsInSuit).reduceLeft ( _ min _ )

  def maxMajorLength: Int = Suit.majors.map(cardsInSuit).reduceLeft ( _ max _ )

  def maxMinorLength: Int = Suit.minors.map(cardsInSuit).reduceLeft ( _ max _ )


  def showSuit(suit: Suit, sb: Appendable) {
    val suit1: List[Card] = ofSuit(suit)
    if (!suit1.isEmpty) {
      sb.append(suit.toString + "-");
      suit1.foreach {
        c: Card => sb.append(c.rank + ",")
      }
    }
  }

  def distributionString : String = {
      var ret : String = ""
     Suit.suits.map(f => ret = ret +  cardsInSuit(f) + "-")
     ret.substring(0,ret.length - 1)
   }


  override def toString: String = {
    val sb: java.lang.StringBuffer = new java.lang.StringBuffer()
    sb.append("hcp=" + hcp + " ")
    sb.append(distributionString+ " ")
     Suit.suits.foreach(suit => showSuit(suit, sb))
    sb.toString
  }

  //   // override if toString is inappropriate
  //  override def toAppendable: String = {
  //   val sb:  java.lang.StringBuilder  = new java.lang.StringBuilder()
  //     Suit.suits.foreach(suit => showSuit(suit, sb))
  //      sb.toString()
  //  }

}

/**
 * a brighe deal of four hands
 * @param dealer  position of the dealer
 * @param vul  vulnerability
 * @param ahands   four hands with deal unset
 */
class Deal(dealer: Position,vul: Vulnerability, ahands: List[Hand]) extends CanAppend {
  // rebuild ot add  the deal
   val hands: Map[Position, Hand]  = ahands.map(f =>(f.position,Hand(this,f))).toMap

  def handAt(position: Position): Hand = {
    hands(position)
  }


  def partner(me: Hand): Hand = handAt(me.position.partner)

  def declarerPosition: Position = dealer

  def vulnerability: Vulnerability = vul

  def declarer: Hand = handAt(dealer)

  def validate = {
    var total = 0;
    Suit.suits.foreach(s => validateCardsInSuit(s))
    hands.values.map(f => total += f.hcp)
    if (total != 40)
      throw new IllegalStateException("bad total points " + total)

  }


  def validateCardsInSuit(s: Suit) = {

    var total = 0;
    hands.values.map(f => total += f.cardsInSuit(s))
    if (total != 13)
      throw new IllegalStateException("bad total cards " + total)

  }

  // override if toString is inappropriate
  override def toAppendable: String = {
    val sb: java.lang.StringBuilder = new java.lang.StringBuilder()
    sb.append("Dealer " + declarerPosition)
    sb.append("\n")
    val nsTab: String = "\t\t\t\t\t"
    sb.append(nsTab)
    sb.append("\t\t")
    handAt(Position.North).append(sb)
    sb.append("\n")
    handAt(Position.East).append(sb)
    sb.append(nsTab)
    sb.append(nsTab)
    handAt(Position.West).append(sb)
    sb.append("\n")

    sb.append(nsTab)
    sb.append("\t\t")
    handAt(Position.South).append(sb)
    sb.append("\n")

    sb.toString()
  }
}

object BridgeDealer {
  def handSize = 13

  def numberHands: Int = Position.positions.length

  def validate(cards: List[Card]) = {
    assert(cards.length == 52)
    var total = 0
    Suit.suits.foreach(s => validateCardsInSuit(cards, s))
    cards.map(f => total += f.points)
    if (total != 40)
      throw new IllegalStateException("bad total points " + total)

  }


  def validateCardsInSuit(cards: List[Card], s: Suit) = {
    var total = 0
    cards.map(c =>
      if (c.suit == s)
        total += 1
    )
    if (total != 13)
      throw new IllegalStateException("bad total cards " + total)

  }

  /**
   * deal cards and make a deal with 4 hands, dealer and vulnerability
   * @return
   */
  def makeHands: Deal = {

    val dealer: Position = Position.random
    val vulnerability: Vulnerability = Vulnerability.random
    val positionToHand: collection.mutable.Map[Position, Hand] = scala.collection.mutable.Map[Position, Hand]()
    val cards: List[Card] = Deck.shuffledCards;
    validate(cards)

    val hands: List[Hand] = Position.positions.map(f => Hand(f,null,dealHand(cards,f)))
    val ret: Deal = new Deal(dealer,vulnerability, hands)

    ret.validate
    return ret
  }

  def dealHand(cards: List[Card],pos: Position) : List[Card]  = {
       cards.slice(13 * pos.asInt,13 * (1 + pos.asInt))
  }
}

