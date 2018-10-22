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
package solitarius.ui

import java.awt._

import javax.swing._
import com.lordjoe.java_bridge.ui.{BlankRectangle, HandCardView, HandView}
import com.lordjoe.java_bridge.{Deal, DoubleDummySolution, Position, Suit}
//import solitarius.general._

class DealView(model: Deal) extends JComponent {

  val marginTop = 5
  val marginRight = 5
  val marginBottom = 5
  val marginLeft = 5
  val preferredPadding = 5
  val cascadeDy = 20

  var hands: Array[HandView] = Array(
    new HandView(model.handAt(Position.East)),
    new HandView(model.handAt(Position.South)),
    new HandView(model.handAt(Position.West)),
    new HandView(model.handAt(Position.North))
  )

  setMinimumSize(getPreferredSize)


  setLayout(new GridLayout(3, 3))
  add(new BlankRectangle)
  add(hands(Position.North.asInt))
  add(new DoubleDummyView(model))
  add(hands(Position.West.asInt))
  add(new BlankRectangle)
  add(hands(Position.East.asInt))
  add(new BlankRectangle)
  add(hands(Position.South.asInt))
  add(new BlankRectangle)


  //  add(hands(Position.East.asInt), BorderLayout.EAST)
  //  add(hands(Position.South.asInt), BorderLayout.SOUTH)
  //  add(hands(Position.West.asInt), BorderLayout.WEST)
  //  add(hands(Position.North.asInt), BorderLayout.NORTH)

  override def getPreferredSize = new Dimension(
    hands(0).getPreferredSize.width +
      hands(1).getPreferredSize.width +
      hands(2).getPreferredSize.width,
    hands(0).getPreferredSize.height +
      hands(1).getPreferredSize.height +
      hands(2).getPreferredSize.height)

}


class DoubleDummyView(model: Deal) extends JComponent {
  setLayout(new BorderLayout());
  add(new DoubleDummyResultsView(new DoubleDummySolution(model)), BorderLayout.CENTER)
  add(new JLabel("  "), BorderLayout.NORTH)
  add(new JLabel("  "), BorderLayout.SOUTH)
  add(new JLabel("  "), BorderLayout.EAST)
  add(new JLabel("  "), BorderLayout.WEST)


}


class DoubleDummyResultsView(model: DoubleDummySolution) extends JComponent {
  setLayout(new GridLayout(5, 6, -1, -1))

  import javax.swing.BorderFactory

  setBackground(Color.WHITE);
  setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2))

  add(new JPanel())
  for (i <- 0 until Suit.trumps.length) {
    add(new SuitLabel(Suit.trumps(i)))
  }


  for (i <- 0 until Position.positions.length) {
    add(new PositionLabel(Position.positions(i)))
    for (s <- 0 until Suit.trumps.length) {
      add(new TricksLabel(model.getTricks(Suit.trumps(s), Position.positions(i))))
    }
  }

}

class TricksLabel(value: Int) extends JButton {

  if (value < 7) {
    setFont(new Font("Courier New", Font.BOLD, 18))
    setBackground(Color.BLUE)
    setOpaque(true)
    setForeground(Color.WHITE)
    setText((value).toString)
    setContentAreaFilled(false)
   //  setPainter(new ColorPainter(new Color(0Xff7cc242, true)))
  }
  else {
    setFont(new Font("Courier New", Font.PLAIN, 18))
    setBackground(Color.WHITE)
    setOpaque(true)
    setForeground(Color.BLACK)
    setText((value - 6).toString)
    setContentAreaFilled(false)

  }

}

class PositionLabel(model: Position) extends JButton {
  setText(model.toString)
}

class SuitLabel(model: Suit) extends JButton {
  if (model == null)
    setText("No Trump")
  else
    setText(model.toString)
}


