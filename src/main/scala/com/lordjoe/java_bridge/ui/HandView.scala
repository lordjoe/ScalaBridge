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
package com.lordjoe.java_bridge.ui

import java.awt._
import java.awt.event._
import javax.swing.{BorderFactory, JComponent, JLabel}

import com.lordjoe.java_bridge.{Card, Hand, Position, Suit}
import solitarius.general._
import solitarius.ui.CardImages._

import scala.List

class SuitPile(suit: Suit, HandCards: List[Card]) extends BasicPile {
  HandCards.map(f => push(f))

  def ofSuit: Suit = suit

  override protected def canDrop(sequence: Sequence): Boolean = false

  override def longestDraggableSequence: Int = 1
}

class HandView(model: Hand) extends JComponent {
  setLayout(new BorderLayout())
  private val label: JLabel = new JLabel(buildLabelString)
  label.setFont(new Font("Courier New", Font.BOLD, 14));
  add(label,BorderLayout.NORTH)
  add(new HandCardView(model),BorderLayout.CENTER)
  if(model.isDealer)  setBorder(BorderFactory.createLineBorder(Color.ORANGE,3) )

   def buildLabelString: String = model.position + " HCP=" + model.hcp + " Distribution " + model.distributionString
 }




class HandCardView(model: Hand) extends JComponent {
  val marginTop = 5
  val marginRight = 5
  val marginBottom = 5
  val marginLeft = 5
  val preferredPadding = 5
  val cascadeDy = 20
  var currentDrag: Option[Drag] = None

  var suits: Array[SuitPile] = Array(
    new SuitPile(Suit.Club,  model.ofSuit(Suit.Spade)) ,
    new SuitPile(Suit.Diamond, model.ofSuit(Suit.Heart)),
    new SuitPile(Suit.Heart, model.ofSuit(Suit.Diamond)),
    new SuitPile(Suit.Spade, model.ofSuit(Suit.Club)))


  addMouseListener(myMouseListener)
  addMouseMotionListener(myMouseMotionListener)
  setPreferredSize(new Dimension(marginLeft + marginRight + 4 * (preferredPadding + cardWidth),
    marginTop + marginBottom + model.maxSuitLength * cardHeight))
  setMinimumSize(getPreferredSize)


  override def getPreferredSize = new Dimension(300, 300)

  override def paint(g: Graphics) {
    paintTableau(g)

    currentDrag.foreach { drag =>
      drawCards(g, drag.x, drag.y, drag.sequence)
    }
  }

  // make a unique color for each hand
  private def colorOfHand(): Color =
    if(model.isVulnerable) Color.RED
    else Color.GREEN

  private def paintTableau(g: Graphics) {
    val r =  getVisibleRect
    var x: Int = 0
    var y: Int = 0

    GraphicUtilities.colorComponent(this, g, colorOfHand())

    for ((pile) <- suits) {
      val (xx, yy) = gridCoordinates(x, y)
      drawPile(g, pile, xx, yy)
      x = x + 1
    }
  }

  private def horizontalPadding = {
    val width = getWidth
    val contentWidth = width - marginLeft - marginRight
    val cardWidths = Suit.numberSuits * cardWidth
    (contentWidth - cardWidths) / (Suit.numberSuits - 1)
  }

  private def verticalPadding = {
    val height = getHeight
    val contentHeight = height - marginTop - marginTop
    val cardHeights = model.maxSuitLength * cardHeight
    (contentHeight - cardHeights) / (Suit.numberSuits - 1)
  }

  private def gridCoordinates(x: Int, y: Int) = {
    val prev = 0 //cardHeight * model.maxSuitLength
    (marginLeft + (cardWidth + horizontalPadding) * x,
      marginTop + prev + (verticalPadding * y))
  }

  private def drawPile(g: Graphics, pile: Pile, x: Int, y: Int) {
    val dragged = if (currentDrag.map(_.pile) == Some(pile))
      currentDrag.get.sequence.size
    else 0

    val reallyVisible = pile.visibleCount - dragged
    val isEmpty = (pile.size - dragged) == 0

    if (isEmpty) {
      g.setColor(Color.GRAY)
      g.fillRect(x, y, cardWidth, cardHeight)
    }
    else if (pile.showAsCascade) {
      drawCascaded(g, pile, x, y)
    }
    else {
      drawNonCascaded(g, pile, x, y)
    }
  }

  private def drawCascaded(g: Graphics, pile: Pile, x: Int, startY: Int) {
    var y = startY
    for (i <- 1 to pile.hiddenCount) {
      g.drawImage(backside, x, y, this)
      y += cascadeDy
    }

    if (currentDrag.map(_.pile) == Some(pile)) {
      drawCards(g, x, y, pile.visibleCards.drop(currentDrag.get.sequence.size))
    }
    else {
      drawCards(g, x, y, pile.visibleCards)
    }
  }

  private def drawNonCascaded(g: Graphics, pile: Pile, x: Int, y: Int) {
    val dragged = if (currentDrag.map(_.pile) == Some(pile))
      currentDrag.get.sequence.size
    else 0
    val reallyVisible = pile.visibleCount - dragged

    if (reallyVisible > 0) {
      drawCard(g, pile.cards.drop(dragged).head, x, y)
    }
    else {
      g.drawImage(backside, x, y, this)
    }
  }

  private def drawCards(g: Graphics, x: Int, y: Int, cards: Seq[Card]) {
    var yy = y
    for (card <- cards.reverse) {
      drawCard(g, card, x, yy)
      yy += cascadeDy
    }
  }

  private def drawCard(g: Graphics, card: Card, x: Int, y: Int) {
    g.drawImage(cardImages(card), x, y, this)
  }

  class Drag(val pile: Pile, val sequence: Sequence, val dx: Int, val dy: Int, var mouseX: Int, var mouseY: Int) {
    def x = mouseX - dx

    def y = mouseY - dy
  }

  def positionToCardLocation(x: Int, y: Int) =
    boundsForCards.find(_._1.inBounds(x, y)).map { p =>
      val (bounds, pile, cardIndex) = p
      val (xx, yy) = bounds.relative(x, y)
      val take = if (pile.showAsCascade) pile.size - cardIndex else 1
      CardLocation(pile, take, xx, yy)
    }

  def pile(x: Int, y: Int): Option[Pile] =
    boundsForPiles.find(_._1.inBounds(x, y)).map(_._2)

  def boundsForCards: Seq[(Bounds, Pile, Int)] = for {
    (bounds, pile) <- boundsForPiles
    result <- boundsForCardsOfPile(bounds, pile)
  } yield result

  def boundsForCardsOfPile(bounds: Bounds, pile: Pile): Seq[(Bounds, Pile, Int)] =
    if (pile.showAsCascade) {
      for {
        cardIndex <- Iterator.range(pile.size - 1, -1, -1).toList
        val top = bounds.y + (cardIndex * cascadeDy)
      } yield (Bounds(bounds.x, top, cardWidth, cardHeight), pile, cardIndex)
    }
    else {
      List((bounds, pile, 0))
    }

  def boundsForPiles: Seq[(Bounds, Pile)] =

    for {
      pile <- suits
      val (xx, yy) = gridCoordinates(pile.ofSuit.suitRank, 0)
    } yield (Bounds(xx, yy, cardWidth, pileHeight(pile)), pile)

  private def pileHeight(pile: Pile) =
    if (pile.showAsCascade) cardHeight + (pile.size * cascadeDy) else cardHeight

  case class CardLocation(pile: Pile, cardCount: Int, dx: Int, dy: Int)

  private object myMouseListener extends MouseAdapter {
    override def mouseClicked(e: MouseEvent) {
      positionToCardLocation(e.getX, e.getY).filter(_.cardCount == 1).foreach { location =>
        //    model.pileClicked(location.pile, e.getClickCount)
        repaint()
      }
    }

    override def mousePressed(e: MouseEvent) {
      currentDrag = positionToCardLocation(e.getX, e.getY).flatMap { location =>
        location.pile.sequence(location.cardCount).map { seq =>
          new Drag(location.pile, seq, location.dx, location.dy, e.getX, e.getY)
        }
      }
      repaint()
    }

    override def mouseReleased(e: MouseEvent) {
      currentDrag.foreach { drag =>
        pile(e.getX, e.getY).foreach { target =>
          target.drop(drag.sequence)
        }

        currentDrag = None
        repaint()
      }
    }
  }

  private object myMouseMotionListener extends MouseMotionAdapter {
    override def mouseDragged(e: MouseEvent) {
      currentDrag.foreach { drag =>
        drag.mouseX = e.getX
        drag.mouseY = e.getY
        repaint()
      }
    }
  }

}


case class Bounds(x: Int, y: Int, w: Int, h: Int) {
  def inBounds(xx: Int, yy: Int) =
    (xx >= x) && (xx < x + w) && (yy >= y) && (yy < y + h)

  def relative(xx: Int, yy: Int) =
    (xx - x, yy - y)
}




