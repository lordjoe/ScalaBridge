/*
  *  Copyright 2015 Steve Lewis
  *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.lordjoe.java_bridge

import solitarius.general.Utils.shuffled

import scala.collection.mutable.ArrayBuffer

sealed abstract class CardColor {
  def opposing: CardColor
}

object CardColor {

  object Black extends CardColor {
    def opposing = Red
  }

  object Red extends CardColor {
    def opposing = Black
  }

}

abstract class Suit(val name: String, val color: CardColor, val uni: Char, val suitRank: Int) extends Ordered[Suit] {
  override def toString = name.substring(0,1) // uni.toString

  override def compare(that: Suit): Int = {
    that.suitRank - suitRank
  }



}

object Suit {

  def numberSuits: Int = 4

  case object Spade extends Suit("Spade", CardColor.Black, 9824, 4)

  case object Heart extends Suit("Heart", CardColor.Red, 9829, 3)

  case object Diamond extends Suit("Diamond", CardColor.Red, 9830, 2)

  case object Club extends Suit("Club", CardColor.Black, 9827, 1)

  val cardsInSuit = 13
  val trumps = List(Club, Diamond, Heart,Spade, null)
  val suits = List(Spade, Heart, Diamond, Club)
  val majors = List(Spade, Heart)
  val minors = List(Diamond, Club)
  val clubs = Club
  val diamonds = Diamond
  val hearts = Heart
  val spades = Spade

  def fromInt(x: Integer) :  Suit = suits( 3 - x)
}

object ControlTexts {
  sealed abstract class ControlTextBase

  case class ControlText private[ControlTexts] (controlText: String,
                                                toolTipText: String)
          extends ControlTextBase

  object OkButton     extends ControlText("OK", "Save changes and dismiss")
  object CancelButton extends ControlText("Cancel", "Bail!")
}


sealed abstract class Rank(val shortName: String, val longName: String, val value: Int, val points: Int)
  extends Ordered[Rank] {
  override def toString = shortName

  override def compare(that: Rank): Int = {
    value - that.value
  }
}

object Rank {

  case object Ace extends Rank("A", "Ace", 14, 4)

  case object Deuce extends Rank("2", "2", 2, 0)

  case object Three extends Rank("3", "3", 3, 0)

  case object Four extends Rank("4", "4", 4, 0)

  case object Five extends Rank("5", "5", 5, 0)

  case object Six extends Rank("6", "6", 6, 0)

  case object Seven extends Rank("7", "7", 7, 0)

  case object Eight extends Rank("8", "8", 8, 0)

  case object Nine extends Rank("9", "9", 9, 0)

  case object Ten extends Rank("10", "10", 10, 0)

  case object Jack extends Rank("J", "Jack", 11, 1)

  case object Queen extends Rank("Q", "Queen", 12, 2)

  case object King extends Rank("K", "King", 13, 3)


  val ranks = List(Ace, Deuce, Three, Four, Five, Six, Seven, Eight, Nine,
    Ten, Jack, Queen, King)

  def fromChar(c: Char)  : Rank = {
    (c ) match {
      case 'A' => Ace
      case 'K'  => King
      case 'Q'  => Queen
      case 'J'  => Jack
      case 'T'  => Ten
      case '9'  => Nine
      case '8'  => Eight
      case '7'  => Seven
      case '6'  => Six
      case '5'  => Five
      case '4'  => Four
     case '3'  => Three
      case '2'  => Deuce
        case _  => throw new IllegalArgumentException("This is not a card " + c)
    }
  }
}

case class Card(rank: Rank, suit: Suit) extends Ordered[Card] {
  def value = rank.value

  def points = rank.points

  def color = suit.color

  override def compare(that: Card): Int = {
    if (suit == that.suit)
      that.rank.compare(rank)
    else
      suit.compare(that.suit)
  }


  override def toString = suit.uni + rank.longName // + " of " + suit.name
}

object Deck {
  val cards = {
    val cds: ArrayBuffer[Card] = new ArrayBuffer[Card]()
    for (suit <- Suit.suits)
      for (rank <- Rank.ranks)
        cds += Card(rank, suit)
    cds.toList
  }
  val hearts = ofSuit(Suit.Heart)
  val spades = ofSuit(Suit.Spade)
  val diamonds = ofSuit(Suit.Diamond)
  val clubs = ofSuit(Suit.Spade)

  def ofSuit(suit: Suit): List[Card] = Rank.ranks.map(Card(_, suit))

  def shuffledCards = shuffled(cards).toList

  def shuffledDecks(deckCount: Int) = shuffled(decks(deckCount)).toList

  def decks(count: Int): List[Card] = if (count == 0) Nil else cards ::: decks(count - 1)


}
