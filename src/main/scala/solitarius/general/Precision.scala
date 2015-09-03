package solitarius.general

import com.lordjoe.bridge.{Deal, Hand, Position}

/**
 * solitarius.general.Precision 
 * User: Steve
 * Date: 7/12/2015
 */
object Precision {

  def oneClubResponder(deal: Deal): Hand = {
    deal.handAt(oneClubBidder(deal).position.partner)
  }

  def oneClubBidder(deal: Deal): Hand = {
    if(!willOpen1Club(deal))
      throw new IllegalArgumentException("No one club opener")

    val declarer: Position = deal.declarerPosition
    var testPosition = declarer

    while (testPosition.next != declarer) {
      var test: Hand = deal.handAt(testPosition)
      if (test.hcp > 16)
        return test
         testPosition = testPosition.next
    }
    throw new IllegalArgumentException("No one club opener")
  }


  def willOpen1Club(deal: Deal): Boolean = {
    val declarer: Position = deal.declarerPosition
    var testPosition = declarer

    while (testPosition.next != declarer) {
      var test: Hand = deal.handAt(testPosition)
      if (test.hcp > 16)
        return true
      if (mightOpen(test))
        return false // interference
      testPosition = testPosition.next
    }
    false
  }

  def willOpen1ClubWithPositiveResponse(deal: Deal): Boolean = {
    val declarer: Position = deal.declarerPosition
    var testPosition = declarer

    while (testPosition.next != declarer) {
      var test: Hand = deal.handAt(testPosition)
      if (test.hcp > 16) {
        if (mightRespond(deal.handAt(testPosition.next)))
          return false; // interference
        if (deal.handAt(testPosition.partner).hcp > 7)
          return true;
        return false; // interference
      }
      if (mightOpen(test))
        return false // interference
      testPosition = testPosition.next
    }
    false
  }

  def willOpen1ClubWithPositiveResponseIgnoreInterference(deal: Deal): Boolean = {
    val declarer: Position = deal.declarerPosition
    var testPosition = declarer

    while (testPosition.next != declarer) {
      var test: Hand = deal.handAt(testPosition)
      if (test.hcp > 16) {
    //    if (mightRespond(deal.handAt(testPosition.next)))
    //      return false; // interference
        if (deal.handAt(testPosition.partner).hcp > 7)
          return true;
        return false; // interference
      }
  //    if (mightOpen(test))
  //      return false // interference
      testPosition = testPosition.next
    }
    false
  }


  def mightOpen(hand: Hand): Boolean = {
    if (hand.hcp > 10)
      return true
    if (hand.maxSuitLength > 6) // open 3
      return true
    if (hand.maxMajorLength > 5 && hand.hcp > 6) // open 2
      return true
    false
  }


  def mightRespond(hand: Hand): Boolean = {
    if (mightOpen(hand))
      return true
    if (hand.hcp > 8)
      return true
    if (hand.maxSuitLength > 5) // preempt
      return true
    false
  }


  def mightBid1Club(deal: Deal): Boolean = {
    for (pos <- Position.positions) {
      val h: Hand = deal.handAt(pos)
      if (h.hcp >= 16)
        return true;
    }
    false
  }


  def mightBid1ClubWithPositiveResponse(deal: Deal): Boolean = {
    for (pos <- Position.positions) {
      val h: Hand = deal.handAt(pos)
      if (h.hcp >= 16) {
        val partnerHand: Hand = deal.handAt(pos.partner)
        if (partnerHand.hcp > 8)
          return true;
      }
    }
    false
  }


}
