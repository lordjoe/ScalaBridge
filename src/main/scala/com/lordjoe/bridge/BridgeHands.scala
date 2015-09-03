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
 *
 *   com.lordjoe.bridge.Main
 */
package com.lordjoe.bridge

import javax.swing._

import solitarius.ui._

object Main {

  val frame = new JFrame("Bridge Hands")

  def main(args: Array[String]) {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
    frame.setJMenuBar(menuBar)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(900, 900)
    frame.setLocationRelativeTo(null)
    frame.setVisible(true)
    newGame(BridgeDealer.makeHands)
  }

  def newGame(deal: Deal) {
    frame.getContentPane.removeAll()
    frame.getContentPane.add(new DealView(deal))
    frame.pack()
  }

  def menuBar =
    MenuBarBuilder.buildMenuBar { menuBar =>
      menuBar.button("New Game") {
       game =>
         game.action("New Game") {
           newGame(BridgeDealer.makeHands)
         }
     }
      menuBar.submenu("Quit") { quit =>
        quit.action("Quit") {
          System.exit(0)
        }
      }
         menuBar.submenu("Help") { help =>
        help.action("About Bridge Dealer") {
          showAbout()
        }
      }
    }

  def showAbout() {
    val message =
      "Bridge Dealer 0.3\n" +
        "\n" +
        "Copyright 2015 Steven Lewis\n"

    JOptionPane.showMessageDialog(
      frame, message,
      "About Bridge Dealer", JOptionPane.INFORMATION_MESSAGE)
  }
}