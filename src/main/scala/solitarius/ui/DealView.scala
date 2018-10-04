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

import java.awt.{Dimension, GridLayout}
import javax.swing.JComponent

import com.lordjoe.java_bridge.ui.{HandView, BlankRectangle}
import com.lordjoe.java_bridge.{Deal, Position}
import solitarius.general._

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
  add(new BlankRectangle)
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


