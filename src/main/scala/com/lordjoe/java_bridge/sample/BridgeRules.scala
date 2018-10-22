package com.lordjoe.java_bridge.sample

import com.lordjoe.java_bridge.{Suit, Hand, Deal}

/**
 * com.lordjoe.bridge.sample.BridgeRules 
 * User: Steve
 * Date: 10/5/2015
 */

trait HandSatisfies {
  def isSatisfied(x: Hand): Boolean
}

class AndSet(rules: List[HandSatisfies]) extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean = rules.forall(r => r.isSatisfied(x))
}

object PartnerCanRespond extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean = x.hcp >= 6
}


object OrdinaryOpen extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean = (x.hcp >= 11 )&& (x.hcp < 16)
}

object OpenInMajor extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean = OrdinaryOpen.isSatisfied(x) && x.maxMajorLength >= 5
}

object OpenTwoClubs extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean = x.hcp < 16 &&
    !OpenInMajor.isSatisfied(x) &&
    (x.cardsInSuit(Suit.Club) >= 6 ||
      x.cardsInSuit(Suit.Club) >= 5 &&
      x.maxMajorLength == 4)

}



object OpenOneClubThenTwoNo extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =  NoPreviousOpen.isSatisfied(x) &&
    CanOpen1Club.isSatisfied(x) && x.partner.hcp >= 14

}

object OpenOneDiamondOneMajorOneNo extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =  NoPreviousOpen.isSatisfied(x) &&
    OpenOneDiamonds.isSatisfied(x) && x.partner.hcp >= 6 &&
    x.minSuitLength > 1 &&
    x.partner.maxMajorLength >= 4 &&
    x.hcp > 13

}


object OpenWeakOneDiamond extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =  NoPreviousOpen.isSatisfied(x) &&
    OpenOneDiamonds.isSatisfied(x) &&
    x.hcp <= 13

}

object OpenOneDiamondOneMajorThen extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =  NoPreviousOpen.isSatisfied(x) &&
    OpenOneDiamonds.isSatisfied(x) && x.partner.hcp >= 6 &&
    x.minSuitLength <= 1 &&
    x.partner.maxMajorLength >= 4 &&
    x.hcp > 13

}


object TooStrongNT extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =   NoPreviousOpen.isSatisfied(x) &&
    isFlat.isSatisfied(x)     &&
    !OpenInMajor.isSatisfied(x) &&  x.minSuitLength > 1 &&
    x.maxMajorLength  < 5 &&  x.hcp > 17 && x.hcp < 20 // no 2nt opening
}

object StrongNT extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =   NoPreviousOpen.isSatisfied(x) &&
    isFlat.isSatisfied(x)     &&
    !OpenInMajor.isSatisfied(x) &&  x.minSuitLength > 1 &&
    isFlat.isSatisfied(x) &&
    x.maxMajorLength  < 5 &&  x.hcp >= 15 && x.hcp <= 18
}

object MaybeSlam extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =
  (x.hcp + x.partner.hcp) >= 30
}

object WeakNT extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =   NoPreviousOpen.isSatisfied(x) &&
    isFlat.isSatisfied(x)     &&
    !OpenInMajor.isSatisfied(x) &&  x.minSuitLength > 1 &&
     isFlat.isSatisfied(x) &&
    x.maxMajorLength  < 5 &&  (
    (x.isVulnerable &&   x.hcp >= 11 && x.hcp <= 13)   ||
      (!x.isVulnerable &&   x.hcp >= 10 && x.hcp <= 13)
    )

}

object Flannery extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =  NoPreviousOpen.isSatisfied(x) && OrdinaryOpen.isSatisfied(x) &&
    x.cardsInSuit(Suit.Heart) == 5 && x.cardsInSuit(Suit.Spade) == 4
}


object GameTry extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =  NoPreviousOpen.isSatisfied(x)  &&
    OrdinaryOpen.isSatisfied(x) &&
    x.hcp > 13 &&
    NoLongSuit.isSatisfied(x) &&
    (x.partner.hcp > 5 &&   x.partner.hcp < 10) &&
    ((x.cardsInSuit(Suit.Heart) >= 5 && x.partner.cardsInSuit(Suit.Heart) == 3) ||
      (x.cardsInSuit(Suit.Spade) >= 5 && x.partner.cardsInSuit(Suit.Spade) == 3))
}

object MajorRaise extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =  NoPreviousOpen.isSatisfied(x)  &&
    OrdinaryOpen.isSatisfied(x) &&
     NoLongSuit.isSatisfied(x) &&
    (x.partner.hcp > 5 &&   x.partner.hcp < 13) &&
    ((x.cardsInSuit(Suit.Heart) >= 5 && x.partner.cardsInSuit(Suit.Heart) == 3) ||
      (x.cardsInSuit(Suit.Spade) >= 5 && x.partner.cardsInSuit(Suit.Spade) == 3))
}

object OneMOneNT extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =  NoPreviousOpen.isSatisfied(x)  &&
    x.isDealer &&
    (x.hcp > 10 && x.hcp < 16 ) &&
    x.maxMajorLength == 5 &&
    !Flannery.isSatisfied(x) &&
    NoLongSuit.isSatisfied(x) &&
    ((x.partner.hcp > 5 &&   x.partner.hcp < 10) &&
    ((x.cardsInSuit(Suit.Heart) >= 5 && x.partner.cardsInSuit(Suit.Heart) < 3) ||
      (x.cardsInSuit(Suit.Spade) >= 5 && x.partner.cardsInSuit(Suit.Spade) < 3))   ||
    ((x.partner.hcp > 9  &&  x.partner.hcp < 13)  &&
      ((x.cardsInSuit(Suit.Heart) >= 5 && x.partner.cardsInSuit(Suit.Heart) <= 3 && x.partner.cardsInSuit(Suit.spades) < 5) ||
        (x.cardsInSuit(Suit.Spade) >= 5 && x.partner.cardsInSuit(Suit.Spade) <= 3))) )
}

object OneMOneNTPass extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =
    x.isDealer &&
       (x.hcp > 10 && x.hcp < 13 ) &&
      x.maxMajorLength == 5 &&
      !Flannery.isSatisfied(x) &&
      NoLongSuit.isSatisfied(x) &&
      (((x.partner.hcp > 5 &&   x.partner.hcp < 10) &&
    ((x.cardsInSuit(Suit.Heart) == 5 && x.partner.cardsInSuit(Suit.Heart) < 3) ||
      (x.cardsInSuit(Suit.Spade)==   5 && x.partner.cardsInSuit(Suit.Spade) < 3) ))   ||
    ((x.partner.hcp > 9  &&  x.partner.hcp < 13)  &&
      ((x.cardsInSuit(Suit.Heart) ==  5 && x.partner.cardsInSuit(Suit.Heart) <= 3 && x.partner.cardsInSuit(Suit.spades) < 5) ||
        (x.cardsInSuit(Suit.Spade)== 5 && x.partner.cardsInSuit(Suit.Spade) <= 3)))    )
}

object Is4414 extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =  NoPreviousOpen.isSatisfied(x) && OrdinaryOpen.isSatisfied(x) &&
    x.cardsInSuit(Suit.Heart) == 4 && x.cardsInSuit(Suit.Spade) == 4 &&
    x.cardsInSuit(Suit.Club) == 4  &&
    (x.cardsInSuit(Suit.Diamond) == 1)
}

object NoLongSuit extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =
    x.maxSuitLength < 7  &&
  x.nextBidder.maxSuitLength < 7 &&
  x.partner.maxSuitLength < 7   &&
  x.previousBidder.maxSuitLength < 7
}

object OpenOneDiamonds extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean = OrdinaryOpen.isSatisfied(x) &&
    !WeakNT.isSatisfied(x)     &&
    !OpenInMajor.isSatisfied(x) &&
    !OpenTwoClubs.isSatisfied(x)
   }


object CanOpenOrdinary extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =
     OrdinaryOpen.isSatisfied(x) &&
     !OpenInMajor.isSatisfied(x) &&
      !OpenTwoClubs.isSatisfied(x) &&
      !Is4414.isSatisfied(x)
}

object CanOpen2Clubs  extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =
    OrdinaryOpen.isSatisfied(x) &&
    x.cardsInSuit(Suit.Club)  >= 6
}

object CanOpen1Club  extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean = x.hcp >= 16
}


object isFlat extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =
    (x.minSuitLength > 1) && (x.maxSuitLength < 7 )
}


object CanOpen2Major extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =
    !OrdinaryOpen.isSatisfied(x) &&
      (x.maxMajorLength >= 6 && x.hcp > 4) // todo look at other major
}


object CanOpen3 extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =
    !OrdinaryOpen.isSatisfied(x) &&
      (x.maxMajorLength >= 7 || x.maxMinorLength > 7) // todo look at other major
}


object CanPreempt extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =
    !OrdinaryOpen.isSatisfied(x) &&
      CanOpen2Major.isSatisfied(x) ||
      CanOpen3.isSatisfied(x)
}

object WillOpen extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =
    x.hcp >= 13 ||
    OrdinaryOpen.isSatisfied(x) ||
      CanPreempt.isSatisfied(x) ||
      CanOpen1Club.isSatisfied(x)
}

object NoPreviousOpen extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =
    x.isDealer ||
      (!WillOpen.isSatisfied(x.previousBidder) &&
        NoPreviousOpen.isSatisfied(x.previousBidder))
}


object IsOpener extends HandSatisfies {
  override def isSatisfied(x: Hand): Boolean =
    WillOpen.isSatisfied(x) && NoPreviousOpen.isSatisfied(x)
}







