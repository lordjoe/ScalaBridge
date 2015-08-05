package com.lordjoe.bridge

/**
 * solitarius.general.BridgeDeal 
 * User: Steve
 * Date: 7/11/2015
 */

import com.lordjoe.scala.CanAppend
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

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

  def isVulnerable(h: Hand): Boolean = isVulnerable(h.pos)

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


/**
 * represents a dealt hand
 * @param position  NSEW
 * @param cards   dealt cards
 */
class Hand(position: Position, cards: List[Card]) extends CanAppend {
  var deal: Deal = null
  val hearts = ofSuit(Suit.Heart)
  val spades = ofSuit(Suit.Spade)
  val diamonds = ofSuit(Suit.Diamond)
  val clubs = ofSuit(Suit.Spade)

  def setDeal(adeal: Deal) = {
    deal  = adeal
  }

  def hcp: Int = {
    var total = 0;
    cards.map(f => total += f.points)
    return total
  }

  def isVulnerable = {
     if(deal == null)
       false
     deal.vulnerability.isVulnerable(this)
  }

  def isDealer = {
      if(deal == null)
        false
      deal.declarer == this
   }


  def pos: Position = position

  def ofSuit(testSuit: Suit): List[Card] = {
    cards.filter {
      c: Card => c.suit == testSuit
    }
  }


  def distribution: Array[Int] = {
    val holder: ArrayBuffer[Int] = new ArrayBuffer[Int]()
    for (suit: Suit <- Suit.suits) {
      holder += cardsInSuit(suit)
    }
    holder.toArray.sorted;
  }


  def cardsInSuit(testSuit: Suit): Int = {
    ofSuit(testSuit).length
  }

  def maxSuitLength: Int = {
    var maxLength = 0;
    Suit.suits.foreach(suit => maxLength = Math.max(maxLength, ofSuit(suit).length))
    return maxLength
  }

  def maxMajorLength: Int = {
    var maxLength = 0;
    Suit.majors.foreach(suit => maxLength = Math.max(maxLength, ofSuit(suit).length))
    return maxLength
  }

  def maxMinorLength: Int = {
    var maxLength = 0;
    Suit.minors.foreach(suit => maxLength = Math.max(maxLength, ofSuit(suit).length))
    return maxLength
  }


  def showSuit(suit: Suit, sb: Appendable) {
    val suit1: List[Card] = ofSuit(suit)
    if (!suit1.isEmpty) {
      sb.append(suit.toString + "-");
      suit1.foreach {
        c: Card => sb.append(c.rank + ",")
      }
    }
  }

  override def toString: String = {
    val sb: java.lang.StringBuffer = new java.lang.StringBuffer()
    sb.append("hcp=" + hcp + " ")
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
 * @param hands   four hands
 */
class Deal(dealer: Position,vul: Vulnerability, hands: Map[Position, Hand]) extends CanAppend {

  hands.values.map(f => f.setDeal(this)) // assign hands to this deal
  def handAt(position: Position): Hand = {
    hands(position)
  }


  def partner(me: Hand): Hand = handAt(me.pos.partner)

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
    var cards: List[Card] = Deck.shuffledCards;
    validate(cards)

    var inShuffle: Int = 0;
    for (pos <- Position.positions) {
      var handCards: ArrayBuffer[Card] = new ArrayBuffer[Card]()
      while (handCards.size < handSize) {
        val dealt: Card = cards.head;
        cards = cards.tail
        handCards += dealt
      }


      positionToHand += (pos -> new Hand(pos, handCards.toList.sorted))
    }
    val dealt: Map[Position, Hand] = Map[Position, Hand](positionToHand.toSeq: _*) // convert to
    val ret: Deal = new Deal(dealer,vulnerability, dealt)

    ret.validate
    return ret
  }
}

