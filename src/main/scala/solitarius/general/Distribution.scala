package solitarius.general

import com.lordjoe.bridge.{Suit, Hand}

/**
 * solitarius.general.Distribution 
 * User: Steve
 * Date: 7/12/2015
 */
object Distribution {

  def hasSingleton(h: Hand): Boolean = {
    for (suit: Suit <- Suit.suits) {
      if (h.cardsInSuit(suit) == 1)
        return true;
    }
    false;
  }

  def hasVoid(h: Hand): Boolean = {
    for (suit: Suit <- Suit.suits) {
      if (h.cardsInSuit(suit) == 0)
        return true;
    }
    false;
  }


  def arrayEqual(d1: Array[Int], d2: Array[Int]): Boolean = {
    if (d1.length != d2.length)
      return false
    for(i <- 0 until d1.length){
        if(d1(i)  != d2(i))
          return false
    }
    true
  }

  def is4441X(h: Hand): Boolean = {
    val distribution1: Array[Int] = h.distribution.toArray
     if (distribution1(0) != 4)
      return false
    if (distribution1(1) != 4)
      return false
    if (distribution1(2) != 4)
      return false
    if (distribution1(3) != 1)
      return false
    true
  }

  def is4441(h: Hand): Boolean = {
    val distribution1: Array[Int] = h.distribution.toArray
    arrayEqual(Array(1,4,4,4),distribution1)
  }

}
